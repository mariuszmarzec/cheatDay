package com.marzec.cheatday.screen.login.model

import com.marzec.cheatday.extensions.emptyString


data class LoginData(val login: String, val password: String) {
    companion object {

        val INITIAL = LoginData(
            login = emptyString(),
            password = emptyString()
        )
    }
}