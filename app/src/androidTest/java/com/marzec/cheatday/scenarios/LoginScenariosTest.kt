package com.marzec.cheatday.scenarios

import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.marzec.cheatday.api.Api
import com.marzec.cheatday.common.startApplication
import com.marzec.cheatday.di.ApiUrlsModule
import com.marzec.cheatday.di.CheatDayApiUrl
import com.marzec.cheatday.di.LoginApiUrl
import com.marzec.cheatday.screens.HomeScreen
import com.marzec.cheatday.screens.LoginScreen
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(ApiUrlsModule::class)
class LoginScenariosTest : TestCase() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    val server = MockWebServer()

    @BindValue
    @LoginApiUrl
    @JvmField
    val apiUrl: String = server.url("/").toString()

    @BindValue
    @CheatDayApiUrl
    @JvmField
    val apiCheatUrl: String = server.url("/").toString()

    @Test
    fun loginInApplication() {
        before {
            server.enqueue(
                MockResponse()
                    .setHeader(Api.Headers.AUTHORIZATION, "token")
                    .setBody(
                        """
                {
                    "id": "1", 
                    "email": "test@mail.com"
                }
            """.trimIndent()
                    )
            )
        }.after {
            server.shutdown()
        }.run {
            step("User launch application") {
                startApplication()
            }

            step("And user sees login screen") {
                LoginScreen.isDisplayed()
            }

            step("Then user types login") {
                LoginScreen.typeLogin("login")
            }

            step("And user types password") {
                LoginScreen.typePassword("password")
            }

            step("And user click login button") {
                LoginScreen.loginButton.click()
            }

            step("Then user see home screen") {
                HomeScreen.isDisplayed()
            }
        }
    }
}
