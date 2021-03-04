package com.marzec.cheatday.feature.home.login.model

import android.service.autofill.UserData
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.repository.LoginRepository
import com.marzec.mvi.Intent
import com.marzec.mvi.StoreViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : StoreViewModel<LoginViewState, LoginActions, LoginSideEffects>(
    LoginViewState.Data(LoginData(login = "", password = ""))
) {
    init {
        intents = mapOf(
            LoginActions.LoginButtonClick::class to Intent(
                onTrigger = {
                    loginRepository.login(it.loginData.login, it.loginData.password)
                },
                reducer = { action: Any, result: Any?, state: LoginViewState ->
                    action as LoginActions.LoginButtonClick
                    result as Content<User>
                    when (result) {
                        is Content.Data<*> -> LoginViewState.Data(state.loginData.copy())
                        is Content.Error<*> -> LoginViewState.Error(state.loginData.copy(), error = result.exception.message.toString())
                    }
                },
                sideEffect = { action: Any, result: Any?, state: LoginViewState ->
                    if (state is LoginViewState.Data) {
                        LoginSideEffects.OnLoginSuccessful
                    } else {
                        null
                    }
                }
            ),
            LoginActions.LoginChanged::class to Intent(
                reducer = { action: Any, _: Any?, state: LoginViewState ->
                    action as LoginActions.LoginChanged
                    val loginData = state.loginData.copy(login = action.login)
                    when (state) {
                        is LoginViewState.Data -> state.copy(loginData = loginData)
                        is LoginViewState.Pending -> state.copy(loginData = loginData)
                        is LoginViewState.Error -> state.copy(loginData = loginData)
                    }
                }
            ),
            LoginActions.PasswordChanged::class to Intent(
                reducer = { action: Any, _: Any?, state: LoginViewState ->
                    action as LoginActions.PasswordChanged
                    val loginData = state.loginData.copy(password = action.password)
                    when (state) {
                        is LoginViewState.Data -> state.copy(loginData = loginData)
                        is LoginViewState.Pending -> state.copy(loginData = loginData)
                        is LoginViewState.Error -> state.copy(loginData = loginData)
                    }
                }
            )
        )
    }
}