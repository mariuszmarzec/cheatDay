package com.marzec.cheatday.feature.home.login.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    private val sideEffectsInternal = MutableLiveData<LoginEffects>()

    val sideEffects: LiveData<LoginEffects>
        get() = sideEffectsInternal

    fun sendAction(action: LoginActions) {
        when (action) {
            LoginActions.LoginButtonClick -> onLoginButtonClick()
        }
    }

    private fun onLoginButtonClick() {
        sideEffectsInternal.value = LoginEffects.OnLoginSuccessful
    }
}