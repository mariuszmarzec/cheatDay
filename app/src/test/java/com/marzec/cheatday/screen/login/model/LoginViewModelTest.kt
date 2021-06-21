package com.marzec.cheatday.screen.login.model

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestCoroutineExecutorExtension
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.core.values
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.math.log
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
        "uuid", "email"
    )

    @Test
    fun onLoginButtonClicked() = runBlockingTest {
        val viewModel = viewModel(defaultState)
        val states = viewModel.state.values(viewModel.sideEffects)

        coEvery {
            loginRepository.login(
                email = "login",
                password = "password"
            )
        } returns Content.Data(user)

        viewModel.onLoginButtonClicked()

        assertThat(states).isEqualTo(
            listOf(
                defaultState,
                LoginViewState.Pending(defaultData),
                LoginSideEffects.OnLoginSuccessful,
                defaultState
            )
        )
    }

    @Test
    fun onLoginChanged() {
        val viewModel = viewModel(defaultState)
        val states = viewModel.state.values()

        viewModel.onLoginChanged("changed")

        assertThat(states).isEqualTo(
            listOf(
                defaultState,
                defaultState.copy(defaultData.copy(login = "changed"))
            )
        )
    }

    @Test
    fun onPasswordChanged() {
        val viewModel = viewModel(defaultState)
        val states = viewModel.state.values()

        viewModel.onPasswordChanged("changed")

        assertThat(states).isEqualTo(
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
