package com.marzec.cheatday.screen.login.model

import com.marzec.cheatday.extensions.EMPTY_STRING

data class LoginData(val login: String, val password: String) {
    companion object {

        val INITIAL = LoginData(
            login = EMPTY_STRING,
            password = EMPTY_STRING
        )
    }
}
