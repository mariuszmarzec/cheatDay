package com.marzec.cheatday.screen.home.login

import com.karumi.shot.ScreenshotTest
import com.marzec.cheatday.api.toContentData
import com.marzec.cheatday.common.compareStateScreenshot
import com.marzec.cheatday.screen.login.model.LoginData
import com.marzec.cheatday.screen.login.view.LoginFragment
import com.marzec.mvi.State
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginStateTest : ScreenshotTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    val initial = LoginData.INITIAL
    val initialState = initial.toContentData()

    val withMailAndPassword = LoginData.INITIAL.copy(
        login = "test@user.com",
        password = "12345678"
    )
    val withMailAndPasswordState = withMailAndPassword.toContentData()

    val loadingState = State.Loading(withMailAndPassword)

    val errorState = State.Error(withMailAndPassword, "Error has occurred")

    @Test
    fun initialState() = compareStateScreenshot<LoginFragment>(initialState)

    @Test
    fun withMailAndPasswordState() =
        compareStateScreenshot<LoginFragment>(withMailAndPasswordState)

    @Test
    fun loadingState() =
        compareStateScreenshot<LoginFragment>(loadingState)

    @Test
    fun errorState() = compareStateScreenshot<LoginFragment>(errorState)
}