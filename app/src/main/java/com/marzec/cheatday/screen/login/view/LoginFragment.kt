package com.marzec.cheatday.screen.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseVMFragment
import com.marzec.cheatday.screen.login.model.LoginActions
import com.marzec.cheatday.screen.login.model.LoginSideEffects
import com.marzec.cheatday.screen.login.model.LoginViewModel
import com.marzec.cheatday.screen.login.render.LoginRender
import javax.inject.Inject

class LoginFragment : BaseVMFragment<LoginViewModel>(R.layout.fragment_login) {

    @Inject
    lateinit var loginRender: LoginRender

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = super.onCreateView(inflater, container, savedInstanceState)?.apply {
        loginRender.onLoginButtonClick = { viewModel.sendAction(LoginActions.LoginButtonClick) }
        loginRender.onPasswordChange = { viewModel.sendAction(LoginActions.PasswordChanged(it)) }
        loginRender.onLoginChange = { viewModel.sendAction(LoginActions.LoginChanged(it)) }
        loginRender.init(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sideEffects.observeNonNull { effect ->
            when (effect) {
                LoginSideEffects.OnLoginSuccessful -> onLoginSuccessful()
            }
        }

        viewModel.state.observeNonNull {
            loginRender.render(it)
        }
    }

    private fun onLoginSuccessful() {
        findNavController().navigateUp()
    }

    override fun viewModelClass(): Class<out LoginViewModel> = LoginViewModel::class.java
}