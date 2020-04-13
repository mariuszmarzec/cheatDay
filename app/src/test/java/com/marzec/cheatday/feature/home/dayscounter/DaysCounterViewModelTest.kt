package com.marzec.cheatday.feature.home.dayscounter

import androidx.lifecycle.Observer
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.domain.Day
import com.marzec.cheatday.domain.DaysGroup
import com.marzec.cheatday.interactor.DaysInteractor
import com.marzec.cheatday.stubs.DayStub
import com.marzec.cheatday.stubs.DaysGroupStub
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
class DaysCounterViewModelTest {

    val daysInteractor: DaysInteractor = mock()
    lateinit var viewModel: DaysCounterViewModel

    @BeforeEach
    fun setUp() {
        whenever(daysInteractor.getDays()) doReturn Observable.never()
        whenever(daysInteractor.updateDay(any())) doReturn Completable.never()
        viewModel = DaysCounterViewModel(daysInteractor)
    }

    @Test
    fun getDays() {
        val subject = PublishSubject.create<DaysGroup>()
        val observer = mock<Observer<in DaysGroup>>()
        whenever(daysInteractor.getDays()) doReturn subject

        viewModel.days.observeForever(observer)

        verify(daysInteractor).getDays()

        subject.onNext(DaysGroupStub.create())
        subject.onNext(DaysGroupStub.create(DayStub.create(count = 1L)))

        verify(observer).onChanged(DaysGroupStub.create())
        verify(observer).onChanged(DaysGroupStub.create(DayStub.create(count = 1L)))
    }

    @Test
    fun onCheatDecreaseClick() {
        whenever(daysInteractor.updateDay(any())) doReturn Completable.complete()
        whenever(daysInteractor.getDays()) doReturn Observable.just(
            DaysGroupStub.create(
                DayStub.create(type = Day.Type.CHEAT),
                DayStub.create(type = Day.Type.WORKOUT),
                DayStub.create(type = Day.Type.DIET)
            )
        )
        viewModel.days.observeForever(mock())

        viewModel.onCheatDecreaseClick()
        daysInteractor.updateDay(DayStub.create(type = Day.Type.CHEAT, count = -1))
    }

    @Test
    fun onDietIncreaseClick() {
        whenever(daysInteractor.updateDay(any())) doReturn Completable.complete()
        whenever(daysInteractor.getDays()) doReturn Observable.just(
            DaysGroupStub.create(
                DayStub.create(type = Day.Type.CHEAT),
                DayStub.create(type = Day.Type.WORKOUT),
                DayStub.create(type = Day.Type.DIET)
            )
        )
        viewModel.days.observeForever(mock())

        viewModel.onDietIncreaseClick()
        daysInteractor.updateDay(DayStub.create(type = Day.Type.WORKOUT, count = -1))
    }

    @Test
    fun onWorkoutIncreaseClick() {
        whenever(daysInteractor.updateDay(any())) doReturn Completable.complete()
        whenever(daysInteractor.getDays()) doReturn Observable.just(
            DaysGroupStub.create(
                DayStub.create(type = Day.Type.CHEAT),
                DayStub.create(type = Day.Type.WORKOUT),
                DayStub.create(type = Day.Type.DIET)
            )
        )
        viewModel.days.observeForever(mock())

        viewModel.onWorkoutIncreaseClick()
        daysInteractor.updateDay(DayStub.create(type = Day.Type.DIET, count = -1))
    }
}