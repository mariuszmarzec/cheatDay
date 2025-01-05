package com.marzec.cheatday.screen.home.model

import com.marzec.mvi.StoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainInteractor: MainInteractor,
    defaultState: MainState
) : StoreViewModel<MainState, Unit>(defaultState) {

    fun loadState() = intent<Pair<Boolean, Boolean>> {
        onTrigger { mainInteractor.observeMainState() }

        reducer {
            state.copy(
                isUserLogged = resultNonNull().first,
                isBottomNavigationVisible = resultNonNull().first && resultNonNull().second,
                isCounterEnabled = resultNonNull().second
            )
        }
    }
}
