package com.marzec.cheatday.screen.home.login

import com.karumi.shot.ScreenshotTest
import com.marzec.cheatday.common.compareStateScreenshot
import com.marzec.cheatday.screen.login.model.LoginViewState
import com.marzec.cheatday.screen.login.view.LoginFragment
import org.junit.Test

class LoginStateTest : ScreenshotTest {

    @Test
    fun defaultState() = compareStateScreenshot<LoginFragment>(LoginViewState.INITIAL)
}