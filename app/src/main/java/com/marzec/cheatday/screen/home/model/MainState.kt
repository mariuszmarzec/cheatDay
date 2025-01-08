package com.marzec.cheatday.screen.home.model

data class MainState(
    val isUserLogged: Boolean,
    val isBottomNavigationVisible: Boolean,
    val isCounterEnabled: Boolean
) {
    companion object {
        val INITIAL = MainState(
            isUserLogged = false,
            isBottomNavigationVisible = true,
            isCounterEnabled = true
        )
    }
}
