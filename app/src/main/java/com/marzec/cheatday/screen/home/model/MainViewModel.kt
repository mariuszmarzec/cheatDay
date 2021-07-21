package com.marzec.cheatday.screen.home.model

import com.marzec.cheatday.OpenForTesting
import com.marzec.mvi.StoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OpenForTesting
@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainInteractor: MainInteractor,
    defaultState: MainState
) : StoreViewModel<MainState, Unit>(defaultState) {

    fun loadState() = intent<Boolean> {
        onTrigger { mainInteractor.observeIfUserLogged() }

        reducer { state.copy(isUserLogged = resultNonNull()) }
    }
}

data class MainState(val isUserLogged: Boolean)

