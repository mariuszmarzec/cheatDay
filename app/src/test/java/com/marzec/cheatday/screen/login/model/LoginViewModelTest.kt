package com.marzec.cheatday.screen.login.model

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestUnconfinedCoroutineExecutorExtension
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.core.test
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.repository.LoginRepository
import com.marzec.mvi.State
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestUnconfinedCoroutineExecutorExtension::class])
internal class LoginViewModelTest {

    val loginRepository: LoginRepository = mockk()
    val defaultData = LoginData(
        login = "login",
        password = "password"
    )
    val defaultState = State.Data(defaultData)

    private val user = User(
        0, "email"
    )

    @Test
    fun onLoginButtonClicked() = test {
        val viewModel = viewModel(defaultState)
        val states = viewModel.test(this)

        coEvery {
            loginRepository.login(
                email = "login",
                password = "password"
            )
        } returns flowOf(Content.Loading(), Content.Data(user))

        viewModel.onLoginButtonClicked()

        states.isEqualTo(
            defaultState,
            State.Loading(defaultData),
            LoginSideEffects.OnLoginSuccessful
        )
    }

    @Test
    fun onLoginChanged() = test {
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
    fun onPasswordChanged() = test {
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

    private fun viewModel(state: State.Data<LoginData>) = LoginViewModel(
        loginRepository = loginRepository,
        initialState = state
    )
}
