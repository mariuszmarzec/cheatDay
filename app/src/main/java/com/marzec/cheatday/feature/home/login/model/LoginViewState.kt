package com.marzec.cheatday.feature.home.login.model

sealed class LoginViewState {

    data class Data(val login: String, val password: String) : LoginViewState()
    object Loading : LoginViewState()
    object Success : LoginViewState()
}