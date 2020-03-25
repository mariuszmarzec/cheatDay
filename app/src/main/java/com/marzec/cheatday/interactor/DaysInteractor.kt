package com.marzec.cheatday.interactor

import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.domain.Day
import com.marzec.cheatday.domain.DaysGroup
import com.marzec.cheatday.repository.DayRepository
import com.marzec.cheatday.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class DaysInteractorImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val daysRepository: DayRepository
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
            updateCheatDays()
        } else {
            Completable.complete()
        }.andThen(daysRepository.update(userId, day))
    }

    private fun updateCheatDays(): Completable {
        return getDays().firstOrError().map { it.cheat.copy(count = it.cheat.count.inc()) }
            .flatMapCompletable { cheatDays ->
                updateDay(cheatDays)
            }
    }

    override fun getMaxDietDays(): Single<Int> {
        return Single.just(Constants.MAX_DIET_DAYS)
    }

    override fun getMaxWorkoutDays(): Single<Int> {
        return Single.just(Constants.MAX_WORKOUT_DAYS)
    }

}

interface DaysInteractor {

    fun getDays(): Observable<DaysGroup>

    fun updateDay(day: Day): Completable

    fun getMaxDietDays(): Single<Int>

    fun getMaxWorkoutDays(): Single<Int>
}