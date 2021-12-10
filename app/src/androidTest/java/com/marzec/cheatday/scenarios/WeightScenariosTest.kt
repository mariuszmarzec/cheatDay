package com.marzec.cheatday.scenarios

import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.marzec.cheatday.R
import com.marzec.cheatday.common.addWeightsResponse
import com.marzec.cheatday.common.currentUser
import com.marzec.cheatday.common.emptyWeightsListResponse
import com.marzec.cheatday.common.fewWeightsListResponse
import com.marzec.cheatday.common.oneWeightsListResponse
import com.marzec.cheatday.common.startApplication
import com.marzec.cheatday.common.updateWeightResponse
import com.marzec.cheatday.common.updatedFewWeightsListResponse
import com.marzec.cheatday.di.MockWebDispatcher
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.screens.AddNewWeightsScreen
import com.marzec.cheatday.screens.ChartScreen
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
    fun s09_userAddsNewWeightResult() {
        before {
            server.dispatcher = MockWebDispatcher
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
                MockWebDispatcher.setResponse("/weights", emptyWeightsListResponse())
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
                MockWebDispatcher.setResponse("/weight", addWeightsResponse())
                MockWebDispatcher.setResponse("/weights", oneWeightsListResponse())
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
    @Suppress("LongMethod")
    fun s10_userUpdatesNewWeightResult() {
        before {
            server.dispatcher = MockWebDispatcher
        }.after {
            server.shutdown()
        }.run {
            step("Given user is logged in") {
                runBlocking {
                    userRepository.setCurrentUserWithAuth(currentUser)
                }
            }

            step("And user has weights results") {
                MockWebDispatcher.setResponse("/weights", fewWeightsListResponse())
            }

            step("Then User launch application") {
                startApplication()
            }

            step("And user sees home screen") {
                HomeScreen.isDisplayed()
            }

            step("And user clicks weights tab") {
                MockWebDispatcher.setResponse("/weights", oneWeightsListResponse())
                HomeScreen.weightTab.click()
            }

            step("Then user sees weights screen") {
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
                MockWebDispatcher.setResponse("/weight/1", updateWeightResponse())
                MockWebDispatcher.setResponse("/weights", updatedFewWeightsListResponse())
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

    @Test
    fun s11_userUpdateMaxPossibleWeight() {
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
                // min weight
                server.enqueue(fewWeightsListResponse())
                // average weight
                server.enqueue(fewWeightsListResponse())
                // weights
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
                WeightsScreen.isDisplayed()
            }

            step("And user clicks max possible weight row") {
                WeightsScreen {
                    weights.childAt<Item>(1) {
                        click()
                    }
                }
            }

            step("Then user sees max possible weight update dialog") {
                WeightsScreen.inputDialog {
                    message.hasText(R.string.dialog_change_max_weight_message)
                    isDisplayed()
                }
            }

            step("And user type new max possible weight") {
                WeightsScreen.typeMaxPossibleWeight("85.0")
            }

            step("And clicks ok button") {
                WeightsScreen.inputDialog.positiveButton.click()
            }

            step("Then user sees weights screen") {
                WeightsScreen.isDisplayed()
            }

            step("And user sees updated max possible weight") {
                WeightsScreen {
                    weights.childAt<Item>(1) {
                        value.hasText("85.0 kg")
                    }
                }
            }
        }
    }

    @Test
    fun s12_userUpdatesTargetWeight() {
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
                server.enqueue(oneWeightsListResponse())
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

            step("And user clicks target weight row") {
                WeightsScreen {
                    weights.childAt<Item>(2) {
                        click()
                    }
                }
            }

            step("Then user sees target weight update dialog") {
                WeightsScreen.inputDialog {
                    message.hasText(R.string.dialog_change_target_weight_message)
                    isDisplayed()
                }
            }

            step("And user type new target weight") {
                WeightsScreen.typeMaxPossibleWeight("85.0")
            }

            step("And clicks ok button") {
                WeightsScreen.inputDialog.positiveButton.click()
            }

            step("Then user sees weights screen") {
                WeightsScreen.isDisplayed()
            }

            step("And user sees updated target weight") {
                WeightsScreen {
                    weights.childAt<Item>(2) {
                        value.hasText("85.0 kg")
                    }
                }
            }
        }
    }

    @Test
    fun s13_userDisplayWeightsChart() {
        before {
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
                // min weight
                server.enqueue(fewWeightsListResponse())
                // average weight
                server.enqueue(fewWeightsListResponse())
                // weights
                server.enqueue(fewWeightsListResponse())
                HomeScreen.weightTab.click()
            }

            step("Then user sees weights screen") {
                WeightsScreen.isDisplayed()
            }

            step("And user clicks chart button") {
                WeightsScreen.chartButton.click()
            }

            step("Then user sees weight chart screen") {
                // weights
                server.enqueue(fewWeightsListResponse())
                ChartScreen.isDisplayed()
            }
        }
    }
}
