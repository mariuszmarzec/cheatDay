package com.marzec.cheatday.screen.login.render

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.marzec.cheatday.R
import com.marzec.cheatday.extensions.gone
import com.marzec.cheatday.extensions.visible
import com.marzec.cheatday.screen.login.model.LoginData
import com.marzec.mvi.State
import javax.inject.Inject

class LoginRender @Inject constructor() {

    var onLoginButtonClick: () -> Unit = {}
    var onPasswordChange: (String) -> Unit = {}
    var onLoginChange: (String) -> Unit = {}

    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var progressBar: View
    private lateinit var error: TextView

    fun init(view: View) {
        val button = view.findViewById<Button>(R.id.button)
        login = view.findViewById(R.id.login)
        password = view.findViewById(R.id.password)
        progressBar = view.findViewById(R.id.progress_bar)
        error = view.findViewById(R.id.error)

        button.setOnClickListener { onLoginButtonClick() }
        login.doOnTextChanged { text, _, _, _ -> onLoginChange(text.toString()) }
        password.doOnTextChanged { text, _, _, _ -> onPasswordChange(text.toString()) }
    }

    fun render(loginViewState: State<LoginData>) {
        when (loginViewState) {
            is State.Data -> renderData(loginViewState.data)
            is State.Loading -> renderPending(loginViewState)
            is State.Error -> renderError(loginViewState)
        }
    }

    private fun renderData(loginData: LoginData) {
        progressBar.gone()
        error.gone()
        renderLoginData(loginData)
    }

    private fun renderLoginData(loginData: LoginData) {
        if (login.text.toString() != loginData.login) {
            login.setText(loginData.login)
        }
        if (password.text.toString() != loginData.password) {
            password.setText(loginData.password)
        }
    }

    private fun renderPending(state: State<LoginData>) {
        progressBar.visible()
        error.gone()
        state.data?.let(::renderLoginData)
    }

    private fun renderError(state: State.Error<LoginData>) {
        progressBar.gone()
        state.data?.let(::renderLoginData)
        error.visible()
        error.text = state.message
    }
}