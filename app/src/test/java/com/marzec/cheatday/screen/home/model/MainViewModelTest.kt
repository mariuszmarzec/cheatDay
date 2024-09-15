package com.marzec.cheatday.screen.home.model


import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestStandardCoroutineExecutorExtension
import com.marzec.cheatday.core.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestStandardCoroutineExecutorExtension::class])
internal class MainViewModelTest {

    val dispatcher = UnconfinedTestDispatcher()
    val scope = TestScope(dispatcher)

    val mainInteractor: MainInteractor = mockk()
    val defualtState = MainState.INITIAL

    @Test
    fun loadState() = scope.runTest() {
        every { mainInteractor.observeIfUserLogged() } returns
                flowOf(false, true).onEach { advanceUntilIdle() }
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.loadState()

        values.isEqualTo(
            defualtState,
            defualtState.copy(isUserLogged = false),
            defualtState.copy(isUserLogged = true)
        )
    }

    private fun viewModel(state: MainState = defualtState) = MainViewModel(mainInteractor, state)
}
