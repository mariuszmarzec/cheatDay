package com.marzec.cheatday.feature.home.login.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    initialState: LoginViewState
) : ViewModel() {

    private val stateInternal = MutableLiveData(initialState)

    val state: LiveData<LoginViewState>
        get() = stateInternal

    private val sideEffectsInternal = MutableLiveData<LoginSideEffects>()

    val sideEffects: LiveData<LoginSideEffects>
        get() = sideEffectsInternal

    fun sendAction(action: LoginActions) {
        when (action) {
            LoginActions.LoginButtonClick -> onLoginButtonClick()
            is LoginActions.LoginChanged -> onLoginChange(action.login)
            is LoginActions.PasswordChanged -> onPasswordChange(action.password)
        }
    }

    private fun onPasswordChange(password: String) {
        (state.value!! as? LoginViewState.Data)?.let { currentState ->
            stateInternal.value = currentState.copy(
                loginData = currentState.loginData.copy(
                    password = password
                )
            )
        }
    }

    private fun onLoginChange(login: String) {
        (state.value!! as? LoginViewState.Data)?.let { currentState ->
            stateInternal.value = currentState.copy(
                loginData = currentState.loginData.copy(
                    login = login
                )
            )
        }
    }

    private fun onLoginButtonClick() {
        sideEffectsInternal.value = LoginSideEffects.OnLoginSuccessful
    }
}