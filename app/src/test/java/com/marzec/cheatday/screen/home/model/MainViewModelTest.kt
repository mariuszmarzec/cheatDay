package com.marzec.cheatday.screen.home.model

import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestStandardCoroutineExecutorExtension
import com.marzec.cheatday.core.test
import com.marzec.mvi.Store4Impl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestStandardCoroutineExecutorExtension::class])
internal class MainViewModelTest {

    val dispatcher = UnconfinedTestDispatcher()
    val scope = TestScope(dispatcher)

    val mainInteractor: MainInteractor = mockk()
    val defualtState = MainState.INITIAL

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        Store4Impl.stateThread = dispatcher
    }

    @Test
    fun loadState() {
        val initialState = defualtState.copy(isUserLogged = false, isBottomNavigationVisible = false)
        scope.runTest {
            every { mainInteractor.observeMainState() } returns
                    flowOf(false to true, true to true).onEach {
                        advanceUntilIdle()
                    }
            val viewModel = viewModel(state = initialState)
            val values = viewModel.test(this)

            viewModel.loadState()

            values.isEqualTo(
                initialState,
                initialState.copy(isUserLogged = true, isBottomNavigationVisible = true)
            )
        }
    }

    private fun viewModel(state: MainState = defualtState) = MainViewModel(mainInteractor, state)
}
