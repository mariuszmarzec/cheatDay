package com.marzec.cheatday.screen.home.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.marzec.cheatday.OpenForTesting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@OpenForTesting
@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainInteractor: MainInteractor
) : ViewModel() {

    private val context = viewModelScope.coroutineContext + Dispatchers.IO

    val isUserLogged: LiveData<Boolean> = liveData(context) {
        mainInteractor.observeIfUserLogged().collect { isLogged ->
            this@liveData.emit(isLogged)
        }
    }
}