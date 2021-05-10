package com.marzec.cheatday.screen.home.dayscounter

import androidx.lifecycle.Observer
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.domain.DaysGroup
import com.marzec.cheatday.interactor.DaysInteractor
import com.marzec.cheatday.stubs.stubDay
import com.marzec.cheatday.stubs.stubDaysGroup
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

//@ExtendWith(InstantExecutorExtension::class)
//class DaysCounterViewModelTest {
//
//    val daysInteractor: DaysInteractor = mock()
//    lateinit var viewModel: DaysCounterViewModel
//
//    @BeforeEach
//    fun setUp() {
//        whenever(daysInteractor.observeDays()) doReturn Observable.never()
//        whenever(daysInteractor.updateDay(any())) doReturn Completable.never()
//        viewModel = DaysCounterViewModel(daysInteractor)
//    }
//
//    @Test
//    fun getDays() {
//        val subject = PublishSubject.create<DaysGroup>()
//        val observer = mock<Observer<in DaysGroup>>()
//        whenever(daysInteractor.observeDays()) doReturn subject
//
//        viewModel.days.observeForever(observer)
//
//        verify(daysInteractor).observeDays()
//
//        subject.onNext(stubDaysGroup())
//        subject.onNext(stubDaysGroup(stubDay(count = 1L)))
//
//        verify(observer).onChanged(stubDaysGroup())
//        verify(observer).onChanged(stubDaysGroup(stubDay(count = 1L)))
//    }
//
//    @Test
//    fun onCheatDecreaseClick() {
//        whenever(daysInteractor.updateDay(any())) doReturn Completable.complete()
//        whenever(daysInteractor.observeDays()) doReturn Observable.just(
//            stubDaysGroup(
//                stubDay(type = Day.Type.CHEAT),
//                stubDay(type = Day.Type.WORKOUT),
//                stubDay(type = Day.Type.DIET)
//            )
//        )
//        viewModel.days.observeForever(mock())
//
//        viewModel.onCheatDecreaseClick()
//        daysInteractor.updateDay(stubDay(type = Day.Type.CHEAT, count = -1))
//    }
//
//    @Test
//    fun onDietIncreaseClick() {
//        whenever(daysInteractor.updateDay(any())) doReturn Completable.complete()
//        whenever(daysInteractor.observeDays()) doReturn Observable.just(
//            stubDaysGroup(
//                stubDay(type = Day.Type.CHEAT),
//                stubDay(type = Day.Type.WORKOUT),
//                stubDay(type = Day.Type.DIET)
//            )
//        )
//        viewModel.days.observeForever(mock())
//
//        viewModel.onDietIncreaseClick()
//        daysInteractor.updateDay(stubDay(type = Day.Type.WORKOUT, count = -1))
//    }
//
//    @Test
//    fun onWorkoutIncreaseClick() {
//        whenever(daysInteractor.updateDay(any())) doReturn Completable.complete()
//        whenever(daysInteractor.observeDays()) doReturn Observable.just(
//            stubDaysGroup(
//                stubDay(type = Day.Type.CHEAT),
//                stubDay(type = Day.Type.WORKOUT),
//                stubDay(type = Day.Type.DIET)
//            )
//        )
//        viewModel.days.observeForever(mock())
//
//        viewModel.onWorkoutIncreaseClick()
//        daysInteractor.updateDay(stubDay(type = Day.Type.DIET, count = -1))
//    }
//}