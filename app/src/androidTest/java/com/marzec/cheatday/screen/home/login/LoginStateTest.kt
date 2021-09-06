package com.marzec.cheatday.screen.home.login

import com.karumi.shot.ScreenshotTest
import com.marzec.cheatday.common.compareStateScreenshot
import com.marzec.cheatday.screen.login.model.LoginViewState
import com.marzec.cheatday.screen.login.view.LoginFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginStateTest : ScreenshotTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun defaultState() = compareStateScreenshot<LoginFragment>(LoginViewState.INITIAL)
}