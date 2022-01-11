package com.marzec.cheatday.screen.home.model


import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestStandardCoroutineExecutorExtension
import com.marzec.cheatday.core.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestStandardCoroutineExecutorExtension::class])
internal class MainViewModelTest {

    val mainInteractor: MainInteractor = mockk()
    val defualtState = MainState.INITIAL

    @Test
    fun loadState() = runTest(StandardTestDispatcher()) {
        every { mainInteractor.observeIfUserLogged() } returns
                flowOf(false, true).onEach { advanceUntilIdle() }
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.loadState()
        advanceUntilIdle()

        values.isEqualTo(
            defualtState,
            defualtState.copy(isUserLogged = false),
            defualtState.copy(isUserLogged = true)
        )
    }

    private fun viewModel(state: MainState = defualtState) = MainViewModel(mainInteractor, state)
}
