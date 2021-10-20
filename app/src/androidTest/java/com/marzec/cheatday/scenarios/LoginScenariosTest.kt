package com.marzec.cheatday.scenarios

import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.marzec.cheatday.common.startApplication
import com.marzec.cheatday.screens.HomeScreen
import com.marzec.cheatday.screens.LoginScreen
import dagger.hilt.android.testing.HiltAndroidRule
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test

class LoginScenariosTest : TestCase() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var server: MockWebServer

    @Test
    fun loginInApplication() {
        before {
            server = MockWebServer()
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
