package com.marzec.cheatday.screen.dayscounter.model

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestUnconfinedCoroutineExecutorExtension
import com.marzec.cheatday.core.test
import com.marzec.cheatday.interactor.DaysInteractor
import com.marzec.cheatday.model.domain.ClickedStates
import com.marzec.cheatday.repository.LoginRepository
import com.marzec.cheatday.repository.UserPreferencesRepository
import com.marzec.cheatday.stubs.stubDaysGroup
import com.marzec.mvi.Store4Impl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestUnconfinedCoroutineExecutorExtension::class])
class DaysCounterViewModelTest {

    val dispatcher = UnconfinedTestDispatcher()
    val scope = TestScope(dispatcher)

    val daysInteractor: DaysInteractor = mockk(relaxed = true)
    val preferencesRepository: UserPreferencesRepository = mockk(relaxed = true)
    val loginRepository: LoginRepository = mockk(relaxed = true)
    val defaultState: DaysCounterState = DaysCounterState.DEFAULT_STATE

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        Store4Impl.stateThread = dispatcher
    }

    @Test
    fun loading() = scope.runTest {
        coEvery { daysInteractor.observeDays() } returns flowOf(
            stubDaysGroup(
                cheat = defaultState.cheat.day.copy(
                    count = 1
                ),
                workOut = defaultState.workout.day,
                diet = defaultState.diet.day

            )
        )
        coEvery { daysInteractor.observeClickedStates() } returns flowOf(
            ClickedStates(
                isCheatDayClicked = false,
                isWorkoutDayClicked = false,
                isDietDayClicked = false
            )
        )
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.loading()

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                defaultState.copy(
                    cheat = defaultState.cheat.copy(day = defaultState.cheat.day.copy(count = 1))
                )
            )
        )
    }

    @Test
    fun onCheatDecreaseClick() = scope.runTest {
        val viewModel = viewModel()

        viewModel.onCheatDecreaseClick()

        coVerify { daysInteractor.updateDay(defaultState.cheat.day.copy(count = -1)) }
    }

    @Test
    fun onDietIncreaseClick() = scope.runTest {
        val viewModel = viewModel()

        viewModel.onDietIncreaseClick()

        coVerify { daysInteractor.updateDay(defaultState.diet.day.copy(count = 1)) }
    }

    @Test
    fun onWorkoutIncreaseClick() = scope.runTest {
        val viewModel = viewModel()

        viewModel.onWorkoutIncreaseClick()

        coVerify { daysInteractor.updateDay(defaultState.workout.day.copy(count = 1)) }
    }

    private fun viewModel() = DaysCounterViewModel(
        daysInteractor,
        preferencesRepository,
        loginRepository,
        defaultState
    )
}
