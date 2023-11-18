package com.marzec.cheatday.scenarios

import android.Manifest
import androidx.test.rule.GrantPermissionRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.marzec.cheatday.common.currentUser
import com.marzec.cheatday.common.startApplication
import com.marzec.cheatday.db.AppDatabase
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.screens.HomeScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CountersScenariosTest : TestCase() {

    @get:Rule
    var runtimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var database: AppDatabase

    @Before
    fun init() {
        hiltRule.inject()
        database.clearAllTables()
    }

    @Test
    fun s04_userIncreaseDietCounter() {
        run {
            step("Given user is logged in") {
                runBlocking {
                    userRepository.setCurrentUserWithAuth(currentUser)
                }
            }

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

    @Test
    fun s05_userIncreaseWorkoutCounter() {
        run {
            step("Given user is logged in") {
                runBlocking {
                    userRepository.setCurrentUserWithAuth(currentUser)
                }
            }

            step("Then User launch application") {
                startApplication()
            }

            step("And user sees home screen") {
                HomeScreen.isDisplayed()
            }

            step("And user clicks increase button for workout") {
                HomeScreen.workoutDayCounter.performIncrease()
            }

            step("Then user sees counter value increased") {
                HomeScreen.workoutDayCounter.isValueEqual("1/3")
            }
        }
    }

    @Test
    fun s06_userDecreaseCheatDayCounter() {
        run {
            step("Given user is logged in") {
                runBlocking {
                    userRepository.setCurrentUserWithAuth(currentUser)
                }
            }

            step("Then User launch application") {
                startApplication()
            }

            step("And user sees home screen") {
                HomeScreen.isDisplayed()
            }

            step("And user clicks decrease button for cheat day") {
                HomeScreen.cheatDayCounter.performDecrease()
            }

            step("Then user sees counter value decreased") {
                HomeScreen.cheatDayCounter.isValueEqual("-1")
            }
        }
    }

    @Test
    fun s07_userIncreaseCheatDayCounterByIncreasingDiet() {
        run {
            step("Given user is logged in") {
                runBlocking {
                    userRepository.setCurrentUserWithAuth(currentUser)
                }
            }

            step("Then User launch application") {
                startApplication()
            }

            step("And user sees home screen") {
                HomeScreen.isDisplayed()
            }

            step("And user clicks increase button for diet 5 times") {
                repeat(5) {
                    HomeScreen.dietDayCounter.performIncrease()
                }
            }

            step("Then user sees counter value decreased") {
                HomeScreen.cheatDayCounter.isValueEqual("1")
            }
        }
    }

    @Test
    fun s08_userIncreaseCheatDayCounterByIncreasingWorkout() {
        run {
            step("Given user is logged in") {
                runBlocking {
                    userRepository.setCurrentUserWithAuth(currentUser)
                }
            }

            step("Then User launch application") {
                startApplication()
            }

            step("And user sees home screen") {
                HomeScreen.isDisplayed()
            }

            step("And user clicks increase button for workout 3 times") {
                repeat(3) {
                    HomeScreen.workoutDayCounter.performIncrease()
                }
            }

            step("Then user sees counter value decreased") {
                HomeScreen.cheatDayCounter.isValueEqual("1")
            }
        }
    }
}
