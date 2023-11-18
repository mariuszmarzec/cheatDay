package com.marzec.cheatday.scenarios

import androidx.test.rule.GrantPermissionRule
import android.Manifest
import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.marzec.cheatday.common.currentUser
import com.marzec.cheatday.common.startApplication
import com.marzec.cheatday.di.DataStoreModule
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.screens.HomeScreen
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UninstallModules(DataStoreModule::class)
@HiltAndroidTest
class CountersScenariosTest : TestCase() {

    @get:Rule
    var runtimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepository: UserRepository

    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    val Context.userPreferencesStore: DataStore<Preferences> by preferencesDataStore(
        "test_pref",
        produceMigrations = { _ ->
            listOf(
                object : DataMigration<Preferences> {
                    override suspend fun cleanUp() = Unit

                    override suspend fun shouldMigrate(currentData: Preferences): Boolean = true

                    override suspend fun migrate(currentData: Preferences): Preferences =
                        currentData.toMutablePreferences().apply { clear() }

                }
            )
        }
    )

    @BindValue
    val dataStore: DataStore<Preferences> = context.userPreferencesStore

    @Before
    fun init() {
        hiltRule.inject()
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
