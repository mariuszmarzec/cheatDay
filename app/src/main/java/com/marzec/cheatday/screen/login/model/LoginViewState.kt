package com.marzec.cheatday.screen.login.model

import com.marzec.cheatday.extensions.emptyString

sealed class LoginViewState(open val loginData: LoginData) {

    data class Data(override val loginData: LoginData) : LoginViewState(loginData)
    data class Pending(override val loginData: LoginData) : LoginViewState(loginData)
    data class Error(override val loginData: LoginData, val error: String) : LoginViewState(loginData)

    companion object {

        val INITIAL = Data(
            loginData = LoginData(
                login = emptyString(),
                password = emptyString()
            )
        )
    }
}

data class LoginData(val login: String, val password: String)