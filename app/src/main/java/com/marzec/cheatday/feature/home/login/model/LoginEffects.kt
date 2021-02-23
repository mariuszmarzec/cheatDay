package com.marzec.cheatday.feature.home.login.model

sealed class LoginEffects {
    object OnLoginSuccessful : LoginEffects()
}