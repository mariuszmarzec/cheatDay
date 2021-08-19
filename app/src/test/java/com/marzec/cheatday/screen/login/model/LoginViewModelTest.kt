package com.marzec.cheatday.screen.login.model

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestCoroutineExecutorExtension
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.core.test
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestCoroutineExecutorExtension::class])
internal class LoginViewModelTest {

    val loginRepository: LoginRepository = mockk()
    val defaultData = LoginData(
        login = "login",
        password = "password"
    )
    val defaultState = LoginViewState.Data(defaultData)

    private val user = User(
        0, "email"
    )

    @Test
    fun onLoginButtonClicked() = runBlockingTest {
        val viewModel = viewModel(defaultState)
        val states = viewModel.test(this)

        coEvery {
            loginRepository.login(
                email = "login",
                password = "password"
            )
        } returns Content.Data(user)

        viewModel.onLoginButtonClicked()

        assertThat(states.values()).isEqualTo(
            listOf(
                defaultState,
                LoginViewState.Pending(defaultData),
                LoginSideEffects.OnLoginSuccessful,
                defaultState
            )
        )
    }

    @Test
    fun onLoginChanged() = runBlockingTest {
        val viewModel = viewModel(defaultState)
        val states = viewModel.test(this)

        viewModel.onLoginChanged("changed")

        assertThat(states.values()).isEqualTo(
            listOf(
                defaultState,
                defaultState.copy(defaultData.copy(login = "changed"))
            )
        )
    }

    @Test
    fun onPasswordChanged() = runBlockingTest {
        val viewModel = viewModel(defaultState)
        val states = viewModel.test(this)

        viewModel.onPasswordChanged("changed")

        assertThat(states.values()).isEqualTo(
            listOf(
                defaultState,
                defaultState.copy(defaultData.copy(password = "changed"))
            )
        )
    }

    private fun viewModel(state: LoginViewState.Data) = LoginViewModel(
        loginRepository = loginRepository,
        initialState = state
    )
}
