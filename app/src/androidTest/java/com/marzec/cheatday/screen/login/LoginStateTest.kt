package com.marzec.cheatday.screen.login

import androidx.test.rule.GrantPermissionRule
import android.Manifest
import com.karumi.shot.ScreenshotTest
import com.marzec.cheatday.common.PolicySetupRule
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
    var runtimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var policySetupRule = PolicySetupRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    val initial = LoginData.INITIAL
    val initialState = State.Data(initial)

    val withMailAndPassword = LoginData.INITIAL.copy(
        login = "test@user.com",
        password = "12345678"
    )
    val withMailAndPasswordState = State.Data(withMailAndPassword)

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
