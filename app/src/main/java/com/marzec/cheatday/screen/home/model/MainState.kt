package com.marzec.cheatday.screen.home.model

data class MainState(val isUserLogged: Boolean?) {
    companion object {
        val INITIAL = MainState(isUserLogged = null)
    }
}
