package com.marzec.cheatday.screen.login

import android.view.View
import com.marzec.cheatday.R
import com.marzec.cheatday.core.getPaparazziRule
import com.marzec.cheatday.screen.login.model.LoginData
import com.marzec.cheatday.screen.login.render.LoginRender
import com.marzec.mvi.State
import org.junit.Rule
import org.junit.Test

class LoginStateTest {

    @get:Rule
    val paparazzi = getPaparazziRule()

    val initial = LoginData.INITIAL
    val initialState = State.Data(initial)

    val withMailAndPassword = LoginData.INITIAL.copy(
        login = "test@user.com",
        password = "12345678"
    )
    val withMailAndPasswordState = State.Data(withMailAndPassword)

    val loadingState = State.Loading(withMailAndPassword)

    val errorState = State.Error(withMailAndPassword, "Error has occurred")

    val renderer = LoginRender()
    
    @Test
    fun initialState() = compare(initialState)

    @Test
    fun withMailAndPasswordState() =
        compare(withMailAndPasswordState)

    @Test
    fun loadingState() =
        compare(loadingState)

    @Test
    fun errorState() = compare(errorState)

    private fun compare(state: State<LoginData>) {
        val view = paparazzi.inflate<View>(R.layout.fragment_login)
        renderer.init(view)
        renderer.render(state)
        paparazzi.snapshot(view)
    }
}
