package com.marzec.cheatday.screen.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.screen.login.model.LoginData
import com.marzec.cheatday.screen.login.model.LoginSideEffects
import com.marzec.cheatday.screen.login.model.LoginViewModel
import com.marzec.cheatday.screen.login.render.LoginRender
import com.marzec.mvi.State
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var loginRender: LoginRender

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = super.onCreateView(inflater, container, savedInstanceState)?.apply {
        loginRender.onLoginButtonClick = { viewModel.onLoginButtonClicked() }
        loginRender.onPasswordChange = { viewModel.onPasswordChanged(it) }
        loginRender.onLoginChange = { viewModel.onLoginChanged(it) }
        loginRender.init(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sideEffects.observe { effect ->
            when (effect) {
                LoginSideEffects.OnLoginSuccessful -> onLoginSuccessful()
            }
        }

        viewModel.state.observe {
            loginRender.render(it)
        }
    }

    private fun onLoginSuccessful() {
        findNavController().navigateUp()
    }
}
