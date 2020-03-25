package com.marzec.cheatday.interactor

import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.domain.Day
import com.marzec.cheatday.domain.DaysGroup
import com.marzec.cheatday.domain.User
import com.marzec.cheatday.repository.DayRepository
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.stubs.DayStub
import com.marzec.cheatday.stubs.DaysGroupStub
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DaysInteractorImplTest {

    private val userRepository: UserRepository = mock()
    private val daysRepository: DayRepository = mock()

    lateinit var interactor: DaysInteractorImpl

    @BeforeEach
    fun setUp() {
        whenever(userRepository.getCurrentUser()) doReturn Single.just(User("user_id", "user@email.com"))
        whenever(daysRepository.getDaysByUser("user_id")) doReturn Observable.empty()
        whenever(daysRepository.update(any(), any())) doReturn Completable.complete()

        interactor = DaysInteractorImpl(
            userRepository,
            daysRepository
        )
    }

    @Test
    fun getDays() {
        val subject = BehaviorSubject.create<DaysGroup>()
        whenever(daysRepository.getDaysByUser("user_id")) doReturn subject

        val days = interactor.getDays().test()

        subject.onNext(DaysGroupStub.create())
        subject.onNext(DaysGroupStub.create(DayStub.create(count = 10)))

        days.assertValueCount(2)
            .assertValueAt(0, DaysGroupStub.create())
            .assertValueAt(1, DaysGroupStub.create(DayStub.create(count = 10)))

        verify(userRepository).getCurrentUser()
        verify(daysRepository).getDaysByUser("user_id")
    }

    @Test
    fun updateDay_cheatDay() {
        interactor.updateDay(DayStub.create(type = Day.Type.CHEAT)).test().assertComplete()

        verify(userRepository).getCurrentUser()
        verify(daysRepository).update("user_id", DayStub.create(type = Day.Type.CHEAT))
    }

    @Test
    fun updateDay_workoutDay_withoutCheatUpdate() {
        interactor.updateDay(DayStub.create(type = Day.Type.WORKOUT)).test().assertComplete()

        verify(userRepository).getCurrentUser()
        verify(daysRepository).update("user_id", DayStub.create(type = Day.Type.WORKOUT))
    }

    @Test
    fun updateDay_workoutDay_withCheatUpdate() {
        whenever(daysRepository.getDaysByUser("user_id")) doReturn Observable.just(
            DaysGroupStub.create()
        )

        interactor.updateDay(DayStub.create(type = Day.Type.WORKOUT, count = 3L)).test().assertComplete()

        verify(userRepository, times(3)).getCurrentUser()
        verify(daysRepository).update("user_id", DayStub.create(type = Day.Type.WORKOUT, count = 3L))
        verify(daysRepository).update("user_id", DayStub.create(type = Day.Type.CHEAT, count = 1))
    }

    @Test
    fun updateDay_dietDay_withCheatUpdate() {
        whenever(daysRepository.getDaysByUser("user_id")) doReturn Observable.just(
            DaysGroupStub.create()
        )

        interactor.updateDay(DayStub.create(type = Day.Type.DIET, count = 5L)).test().assertComplete()

        verify(userRepository, times(3)).getCurrentUser()
        verify(daysRepository).update("user_id", DayStub.create(type = Day.Type.DIET, count = 5L))
        verify(daysRepository).update("user_id", DayStub.create(type = Day.Type.CHEAT, count = 1))
    }

    @Test
    fun updateDay_workoutDay_withoutDietUpdate() {
        interactor.updateDay(DayStub.create(type = Day.Type.DIET)).test().assertComplete()

        verify(userRepository).getCurrentUser()
        verify(daysRepository).update("user_id", DayStub.create(type = Day.Type.DIET))
    }

    @Test
    fun getMaxDietDays() {
        interactor.getMaxDietDays().test().assertValue(Constants.MAX_DIET_DAYS)
    }

    @Test
    fun getMaxWorkoutDays() {
        interactor.getMaxWorkoutDays().test().assertValue(Constants.MAX_WORKOUT_DAYS)
    }
}