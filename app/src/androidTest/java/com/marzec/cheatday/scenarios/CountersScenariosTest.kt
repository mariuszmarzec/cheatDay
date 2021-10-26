package com.marzec.cheatday.scenarios

import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.marzec.cheatday.common.currentUser
import com.marzec.cheatday.common.startApplication
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.db.dao.UserDao
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.repository.DayRepository
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.screens.HomeScreen
import com.marzec.cheatday.screens.LoginScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CountersScenariosTest : TestCase() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userDao: UserDao

    @Before
    fun init() {
        hiltRule.inject()
        runBlocking {
            userRepository.setCurrentUserWithAuth(currentUser)
        }
    }

    @Test
    fun userIncreaseDietCounter() {
        run {

            step("Then User launch application") {
                startApplication()
            }

            step("And user sees home screen") {
                HomeScreen.isDisplayed()
            }

            step("And user clicks increase button for diet") {
                HomeScreen.dietDayCounter.performIncrease()
            }

            step("Then user sees counter value increased") {
                HomeScreen.dietDayCounter.isValueEqual("1/5")
            }
        }
    }
}