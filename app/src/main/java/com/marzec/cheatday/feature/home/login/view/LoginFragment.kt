package com.marzec.cheatday.feature.home.login.view

import android.os.Bundle
import android.view.View
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseVMFragment
import com.marzec.cheatday.feature.home.login.model.LoginSideEffects
import com.marzec.cheatday.feature.home.login.model.LoginViewModel

class LoginFragment : BaseVMFragment<LoginViewModel>(R.layout.fragment_login) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sideEffects.observeNonNull { effect ->
            when (effect) {
                LoginSideEffects.OnLoginSuccessful -> onLoginSuccessful()
            }
        }
    }

    private fun onLoginSuccessful() {

    }

    override fun viewModelClass(): Class<out LoginViewModel> = LoginViewModel::class.java
}