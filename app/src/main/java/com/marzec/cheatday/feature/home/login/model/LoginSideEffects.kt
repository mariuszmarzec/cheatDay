package com.marzec.cheatday.feature.home.login.model

sealed class LoginSideEffects {
    object OnLoginSuccessful : LoginSideEffects()
}