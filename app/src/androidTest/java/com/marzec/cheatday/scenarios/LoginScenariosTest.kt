package com.marzec.cheatday.scenarios

import android.Manifest
import androidx.test.rule.GrantPermissionRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.marzec.cheatday.common.currentUser
import com.marzec.cheatday.common.loginResponse
import com.marzec.cheatday.common.logoutResponse
import com.marzec.cheatday.common.startApplication
import com.marzec.cheatday.di.ApiUrlsModule
import com.marzec.cheatday.di.Method
import com.marzec.cheatday.di.MockWebDispatcher
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.screens.HomeScreen
import com.marzec.cheatday.screens.LoginScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(ApiUrlsModule::class)
class LoginScenariosTest : TestCase() {

    @get:Rule
    var runtimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var server: MockWebServer

    @Inject
    lateinit var userRepository: UserRepository

    @Before
    fun init() {
        hiltRule.inject()
        runBlocking { userRepository.clearCurrentUser() }
    }

    @Test
    fun s01_loginInApplication() {
        before {
            server.dispatcher = MockWebDispatcher
            MockWebDispatcher.setResponse(
                Method.POST,
                "/login",
                loginResponse()
            )
        }.after {
            MockWebDispatcher.clear()
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

    @Test
    fun s02_loginInApplicationFailed() {
        before {
            server.dispatcher = MockWebDispatcher
            MockWebDispatcher.setResponse(
                Method.POST,
                "/login",
                MockResponse().setResponseCode(404)
            )
        }.after {
            MockWebDispatcher.clear()
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

            step("Then user sees error message") {
                LoginScreen.errorMessage.isDisplayed()
            }
        }
    }

    @Test
    fun s03_logoutFromApplication() {
        before {
            server.dispatcher = MockWebDispatcher
            MockWebDispatcher.setResponse(
                Method.GET,
                "/logout",
                logoutResponse()
            )
        }.after {
            MockWebDispatcher.clear()
        }.run {
            step("Given user is logged in") {
                runBlocking {
                    userRepository.setCurrentUserWithAuth(currentUser)
                }
            }

            step("Then user launch application") {
                startApplication()
            }

            step("And user sees home screen") {
                HomeScreen.isDisplayed()
            }

            step("And user click logout button") {
                HomeScreen.logoutButton.click()
            }

            step("And user sees login screen") {
                LoginScreen.isDisplayed()
            }
        }
    }
}
