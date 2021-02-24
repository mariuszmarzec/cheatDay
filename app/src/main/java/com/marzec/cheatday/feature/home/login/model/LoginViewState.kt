package com.marzec.cheatday.feature.home.login.model

sealed class LoginViewState {

    data class Data(val loginData: LoginData) : LoginViewState()
    data class Pending(val loginData: LoginData) : LoginViewState()
    data class Error(val loginData: LoginData, val error: String) : LoginViewState()
}

data class LoginData(val login: String, val password: String)