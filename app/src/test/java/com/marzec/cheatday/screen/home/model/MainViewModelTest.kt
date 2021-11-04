package com.marzec.cheatday.screen.home.model

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestCoroutineExecutorExtension
import com.marzec.cheatday.core.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestCoroutineExecutorExtension::class])
internal class MainViewModelTest {

    val mainInteractor: MainInteractor = mockk()
    val defualtState = MainState.INITIAL

    @Test
    fun loadState() = runBlockingTest {
        every { mainInteractor.observeIfUserLogged() } returns flowOf(false, true)
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.loadState()

        assertThat(values.values()).isEqualTo(
            listOf(
                defualtState,
                defualtState.copy(isUserLogged = false),
                defualtState.copy(isUserLogged = true)
            )
        )
    }

    private fun viewModel(state: MainState = defualtState) = MainViewModel(mainInteractor, state)
}
