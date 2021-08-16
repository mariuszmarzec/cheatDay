package com.marzec.cheatday.interactor

import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.extensions.combine
import com.marzec.cheatday.model.domain.ClickedStates
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.domain.DaysGroup
import com.marzec.cheatday.repository.DayRepository
import com.marzec.cheatday.repository.UserPreferencesRepository
import com.marzec.cheatday.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

class DaysInteractor @Inject constructor(
    private val userRepository: UserRepository,
    private val daysRepository: DayRepository,
    private val preferencesRepository: UserPreferencesRepository
) {

    fun observeDays(): Flow<DaysGroup> {
        return userRepository.observeCurrentUser().flatMapMerge { user ->
            daysRepository.observeDaysByUser(user.id)
        }
    }

    suspend fun updateDay(day: Day) {
        val user = userRepository.getCurrentUser()
        when (day.type) {
            Day.Type.CHEAT -> {
                daysRepository.update(user.id, day)
            }
            Day.Type.WORKOUT -> {
                updateDayWithCheatIfNeeded(user.id, day, Constants.MAX_WORKOUT_DAYS.toLong())
            }
            Day.Type.DIET -> {
                updateDayWithCheatIfNeeded(user.id, day, Constants.MAX_DIET_DAYS.toLong())
            }
        }
    }

    private suspend fun updateDayWithCheatIfNeeded(userId: Long, day: Day, maxCount: Long) {
        if (day.count > 0 && day.count.rem(maxCount) == 0L) {
            incrementCheatDays(daysCount = 1)
        }
        daysRepository.update(userId, day)
    }

    suspend fun getMaxDietDays(): Int = Constants.MAX_DIET_DAYS

    suspend fun getMaxWorkoutDays(): Int = Constants.MAX_WORKOUT_DAYS

    suspend fun incrementCheatDays(daysCount: Int) {
        observeDays().first().let {
            val cheatDays = it.cheat.copy(count = it.cheat.count + daysCount)
            updateDay(cheatDays)
        }
    }

    fun observeClickedStates(): Flow<ClickedStates> {
        val cheatFlow = preferencesRepository.observeWasClickToday(Day.Type.CHEAT)
        val workoutFlow = preferencesRepository.observeWasClickToday(Day.Type.WORKOUT)
        val dietFlow = preferencesRepository.observeWasClickToday(Day.Type.DIET)
        return combine(cheatFlow, workoutFlow, dietFlow).map { (cheatClicked, workoutClicked, dietClicked) ->
            ClickedStates(cheatClicked, workoutClicked, dietClicked)
        }
    }

    suspend fun isStateSettled(): Boolean {
        val cheatClicked = preferencesRepository.observeWasClickToday(Day.Type.CHEAT).first()
        val workoutClicked = preferencesRepository.observeWasClickToday(Day.Type.WORKOUT).first()
        val dietClicked = preferencesRepository.observeWasClickToday(Day.Type.DIET).first()
        return when {
            dietClicked || cheatClicked -> workoutClicked
            workoutClicked -> dietClicked || cheatClicked
            else -> false
        }
    }
}
