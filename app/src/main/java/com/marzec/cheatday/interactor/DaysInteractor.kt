package com.marzec.cheatday.interactor

import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.extensions.combine
import com.marzec.cheatday.model.domain.ClickedStates
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.domain.DaysGroup
import com.marzec.cheatday.repository.DayRepository
import com.marzec.cheatday.repository.UserPreferencesRepository
import com.marzec.cheatday.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DaysInteractorImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val daysRepository: DayRepository,
    private val preferencesRepository: UserPreferencesRepository
): DaysInteractor {
    override fun getDays(): Observable<DaysGroup> {
        return userRepository.getCurrentUser().flatMapObservable { user ->
            daysRepository.getDaysByUser(user.uuid)
        }
    }

    override fun updateDay(day: Day): Completable {
        return userRepository.getCurrentUser().flatMapCompletable { user ->
            when (day.type) {
                Day.Type.CHEAT -> {
                    daysRepository.update(user.uuid, day)
                }
                Day.Type.WORKOUT -> {
                    updateDayWithCheatIfNeeded(user.uuid, day, Constants.MAX_WORKOUT_DAYS.toLong())
                }
                Day.Type.DIET -> {
                    updateDayWithCheatIfNeeded(user.uuid, day, Constants.MAX_DIET_DAYS.toLong())
                }
            }
        }
    }

    private fun updateDayWithCheatIfNeeded(
        userId: String,
        day: Day,
        maxCount: Long
    ): Completable {
        return if (day.count > 0 && day.count.rem(maxCount) == 0L) {
            incrementCheatDays(daysCount = 1)
        } else {
            Completable.complete()
        }.andThen(daysRepository.update(userId, day))
    }

    override fun getMaxDietDays(): Single<Int> {
        return Single.just(Constants.MAX_DIET_DAYS)
    }

    override fun getMaxWorkoutDays(): Single<Int> {
        return Single.just(Constants.MAX_WORKOUT_DAYS)
    }

    override fun incrementCheatDays(daysCount: Int): Completable {
        return getDays().firstOrError().map { it.cheat.copy(count = it.cheat.count + daysCount) }
            .flatMapCompletable { cheatDays ->
                updateDay(cheatDays)
            }
    }

    override fun observeClickedStates(): Flow<ClickedStates> {
        val cheatFlow = preferencesRepository.observeWasClickToday(Day.Type.CHEAT)
        val workoutFlow = preferencesRepository.observeWasClickToday(Day.Type.WORKOUT)
        val dietFlow = preferencesRepository.observeWasClickToday(Day.Type.DIET)
        return combine(cheatFlow, workoutFlow, dietFlow) { cheatClicked, workoutClicked, dietClicked ->
            ClickedStates(cheatClicked, workoutClicked, dietClicked)
        }
    }

    override suspend fun isStateSettled(): Boolean {
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

interface DaysInteractor {

    fun getDays(): Observable<DaysGroup>

    fun updateDay(day: Day): Completable

    fun getMaxDietDays(): Single<Int>

    fun getMaxWorkoutDays(): Single<Int>

    fun incrementCheatDays(daysCount: Int): Completable

    fun observeClickedStates(): Flow<ClickedStates>

    suspend fun isStateSettled(): Boolean
}