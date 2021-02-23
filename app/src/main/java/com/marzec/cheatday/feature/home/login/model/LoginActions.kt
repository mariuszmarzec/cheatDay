package com.marzec.cheatday.feature.home.login.model

sealed class LoginActions {
    object LoginButtonClick : LoginActions()
}