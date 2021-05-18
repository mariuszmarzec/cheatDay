package com.marzec.cheatday.screen.login.model

import com.marzec.cheatday.api.Content
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.repository.LoginRepository
import com.marzec.mvi.StoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    initialState: LoginViewState
) : StoreViewModel<LoginViewState, LoginSideEffects>(
    initialState
) {

    fun onLoginButtonClicked() = intent<Content<User>> {
        onTrigger {
            flow {
                emit(Content.Loading())
                emit(loginRepository.login(state.loginData.login, state.loginData.password))
            }
        }

        reducer {
            when (val result = resultNonNull()) {
                is Content.Data<*> -> LoginViewState.Data(state.loginData.copy())
                is Content.Error<*> -> LoginViewState.Error(state.loginData.copy(), error = result.exception.message.toString())
                is Content.Loading -> LoginViewState.Pending(state.loginData.copy())
            }
        }

        emitSideEffect {
            (state as? LoginViewState.Data)?.let { LoginSideEffects.OnLoginSuccessful }
        }
    }

    fun onLoginChanged(login: String) = intent<Unit> {
        reducer {
            val loginData = state.loginData.copy(login = login)
            when (state) {
                is LoginViewState.Data -> state.copy(loginData = loginData)
                is LoginViewState.Pending -> state.copy(loginData = loginData)
                is LoginViewState.Error -> state.copy(loginData = loginData)
            }
        }
    }

    fun onPasswordChanged(password: String) = intent<Unit> {
        reducer {
            val loginData = state.loginData.copy(password = password)
            when (state) {
                is LoginViewState.Data -> state.copy(loginData = loginData)
                is LoginViewState.Pending -> state.copy(loginData = loginData)
                is LoginViewState.Error -> state.copy(loginData = loginData)
            }
        }
    }
}