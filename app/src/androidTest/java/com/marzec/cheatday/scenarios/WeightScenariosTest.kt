package com.marzec.cheatday.scenarios

import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.marzec.cheatday.common.addWeightsResponse
import com.marzec.cheatday.common.currentUser
import com.marzec.cheatday.common.emptyWeightsListResponse
import com.marzec.cheatday.common.fewWeightsListResponse
import com.marzec.cheatday.common.oneWeightsListResponse
import com.marzec.cheatday.common.startApplication
import com.marzec.cheatday.common.updateWeightResponse
import com.marzec.cheatday.common.updatedFewWeightsListResponse
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.screens.AddNewWeightsScreen
import com.marzec.cheatday.screens.HomeScreen
import com.marzec.cheatday.screens.Item
import com.marzec.cheatday.screens.UpdateWeightsScreen
import com.marzec.cheatday.screens.WeightsScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class WeightScenariosTest : TestCase() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var server: MockWebServer

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun userAddsNewWeightResult() {
        before {
            server.enqueue(emptyWeightsListResponse())
            server.enqueue(emptyWeightsListResponse())
            server.enqueue(emptyWeightsListResponse())

            server.enqueue(emptyWeightsListResponse())
            server.enqueue(addWeightsResponse())
            server.enqueue(oneWeightsListResponse())
            server.enqueue(oneWeightsListResponse())
        }.after {
            server.shutdown()
        }.run {
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

            step("And user clicks weights tab") {
                HomeScreen.weightTab.click()
            }

            step("Then user sees weights screen") {
                WeightsScreen.isDisplayed()
            }

            step("And user clicks add new weight button") {
                WeightsScreen.floatingButton.click()
            }

            step("Then user sees add new weight screen") {
                AddNewWeightsScreen.isDisplayed()
            }

            step("And user type weight value") {
                AddNewWeightsScreen.typeWeight("84.5")
            }

            step("And user clicks add button") {
                AddNewWeightsScreen.button.click()
            }

            step("Then user sees weights screen") {
                WeightsScreen.isDisplayed()
            }

            step("And user sees new weight result added") {
                WeightsScreen.weights {
                    childAt<Item>(position = 3) {
                        value.hasText("84.5 kg")
                    }
                }
            }
        }
    }

    @Test
    fun userUpdatesNewWeightResult() {
        before {
        }.after {
            server.shutdown()
        }.run {
            step("Given user is logged in") {
                runBlocking {
                    userRepository.setCurrentUserWithAuth(currentUser)
                }
            }

            step("And user has weights results") {
                server.enqueue(fewWeightsListResponse())
                server.enqueue(fewWeightsListResponse())
            }

            step("Then User launch application") {
                startApplication()
            }

            step("And user sees home screen") {
                HomeScreen.isDisplayed()
            }

            step("And user clicks weights tab") {
                HomeScreen.weightTab.click()
            }

            step("Then user sees weights screen") {
                server.enqueue(oneWeightsListResponse())
                WeightsScreen.isDisplayed()
            }

            step("And user sees weight results") {
                WeightsScreen.weights {
                    childAt<Item>(position = 3) {
                        value.hasText("84.5 kg")
                    }
                }
            }

            step("And user clicks on weight result") {
                WeightsScreen.weights {
                    childAt<Item>(position = 3) {
                        click()
                    }
                }
            }

            step("Then user sees update weight screen") {
                UpdateWeightsScreen.isDisplayed()
            }


            step("And user type weight value") {
                UpdateWeightsScreen.typeWeight("83.0")
            }

            step("And user clicks update button") {
                server.enqueue(updateWeightResponse())
                server.enqueue(updatedFewWeightsListResponse())
                server.enqueue(updatedFewWeightsListResponse())
                server.enqueue(updatedFewWeightsListResponse())
                server.enqueue(updatedFewWeightsListResponse())
                UpdateWeightsScreen.button.click()
            }

            step("Then user sees weights screen") {
                WeightsScreen.isDisplayed()
            }

            step("And user sees weight result updated") {
                WeightsScreen.weights {
                    childAt<Item>(position = 3) {
                        value.hasText("83.0 kg")
                    }
                }
            }
        }
    }
}