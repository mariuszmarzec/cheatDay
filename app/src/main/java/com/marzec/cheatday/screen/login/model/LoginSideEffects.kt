package com.marzec.cheatday.screen.login.model

sealed class LoginSideEffects {
    object OnLoginSuccessful : LoginSideEffects()
}