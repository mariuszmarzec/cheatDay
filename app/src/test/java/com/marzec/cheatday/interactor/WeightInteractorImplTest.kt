package com.marzec.cheatday.interactor

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.core.values
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.repository.UserPreferencesRepository
import com.marzec.cheatday.repository.UserRepository
import com.marzec.cheatday.repository.WeightResultRepository
import com.marzec.cheatday.stubs.stubWeightResult
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyAll
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.joda.time.DateTimeUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class WeightInteractorTest {

    private val userRepository: UserRepository = mockk()
    private val targetRepository: UserPreferencesRepository = mockk()
    private val weightResultRepository: WeightResultRepository = mockk()
    private val daysInteractor: DaysInteractor = mockk()

    lateinit var interactor: WeightInteractor

    @BeforeEach
    fun setUp() = runBlockingTest {
        coEvery { weightResultRepository.observeLastWeight() } returns (flowOf(null))
        coEvery { weightResultRepository.observeMinWeight() } returns (flowOf(null))
        coEvery { targetRepository.observeMaxPossibleWeight() } returns (flowOf(0f))

        coEvery { weightResultRepository.putWeight(any()) } returns Content.Data(Unit)
        coEvery { weightResultRepository.updateWeight(any()) } returns Content.Data(Unit)

        coEvery { daysInteractor.incrementCheatDays(any()) } returns Unit

        interactor = WeightInteractor(
            userRepository,
            targetRepository,
            weightResultRepository,
            daysInteractor
        )
    }

    @Test
    fun observeTargetWeight() = runBlockingTest {
        coEvery { targetRepository.observeTargetWeight() } returns flowOf(1f, 2f)

        assertThat(interactor.observeTargetWeight().values(this)).isEqualTo(
            listOf(1f, 2f)
        )
    }

    @Test
    fun setTargetWeight() = runBlockingTest {
        coEvery { targetRepository.setTargetWeight(1f) } returns Unit

        interactor.setTargetWeight(1f)

        coVerify { targetRepository.setTargetWeight(1f) }
    }

    @Test
    fun observeWeights() = runBlockingTest {
        coEvery { userRepository.observeCurrentUser() } returns flowOf(User("user_id", "mail"))
        coEvery { weightResultRepository.observeWeights() } returns
                Content.Data(
                    listOf(
                        stubWeightResult(),
                        stubWeightResult()
                    )
                )

        assertThat(interactor.observeWeights().values(this)).isEqualTo(
            listOf(
                Content.Data(
                    listOf(
                        stubWeightResult(),
                        stubWeightResult()
                    )
                )
            )
        )
    }

    @Test
    fun addWeight() = runBlockingTest {
        coEvery { weightResultRepository.putWeight(stubWeightResult()) } returns Content.Data(Unit)
        coEvery { userRepository.getCurrentUser() } returns (User("user_id", "mail"))

        interactor.addWeight(stubWeightResult())

        coVerify { weightResultRepository.putWeight(stubWeightResult()) }
    }

    @Test
    fun updateWeight() = runBlockingTest {
        coEvery { weightResultRepository.putWeight(stubWeightResult()) } returns Content.Data(Unit)
        coEvery { userRepository.getCurrentUser() } returns (User("user_id", "mail"))

        interactor.updateWeight(stubWeightResult())

        coVerify { weightResultRepository.updateWeight(stubWeightResult()) }
    }

    @Test
    fun `if added weight is not today, then don't change cheat days count`() = runBlockingTest {
        coEvery { weightResultRepository.putWeight(stubWeightResult(value = 90.5f)) } returns Content.Data(
            Unit
        )
        coEvery { userRepository.getCurrentUser() } returns (User("user_id", "mail"))

        interactor.addWeight(
            stubWeightResult(value = 90.5f)
        )

        coVerify { weightResultRepository.putWeight(stubWeightResult(value = 90.5f)) }
        coVerify(inverse = true) { daysInteractor.incrementCheatDays(any()) }
    }

    @Nested
    inner class AddedTodayWeight {

        @BeforeEach
        fun setUp() = runBlockingTest {
            DateTimeUtils.setCurrentMillisFixed(0)
        }

        @Test
        fun `if new weight is greater than older and target weight, then decrease cheat day, and additionally decrease cheat day with diff from integers values of weights`() =
            runBlockingTest {
                coEvery { userRepository.getCurrentUser() } returns (User("user_id", "mail"))
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    stubWeightResult(value = 93.4f)
                )
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    stubWeightResult(value = 91f)
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))

                interactor.addWeight(
                    stubWeightResult(value = 95.5f)
                )

                coVerify { daysInteractor.incrementCheatDays(-3) }
            }

        @Test
        fun `if new weight is greater than older with diff smaller than 1 kg than and then target weight, then decrease cheat day`() =
            runBlockingTest {
                coEvery { userRepository.getCurrentUser() } returns (User("user_id", "mail"))
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    stubWeightResult(value = 93.4f)
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    stubWeightResult(value = 91f)
                )

                interactor.addWeight(
                    stubWeightResult(value = 93.6f)
                )

                coVerify { daysInteractor.incrementCheatDays(-1) }
            }

        @Test
        fun `if new weight is greater than older, but not than target weight, then don't change cheat days count`() =
            runBlockingTest {
                coEvery { userRepository.getCurrentUser() } returns (User("user_id", "mail"))
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    stubWeightResult(value = 93.4f)
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(100f))
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    stubWeightResult(value = 91f)
                )

                interactor.addWeight(
                    stubWeightResult(value = 95.5f)
                )

                coVerify(inverse = true) { daysInteractor.incrementCheatDays(any()) }
            }

        @Test
        fun `if new weight is smaller than older, then increase cheat day, and additionally increase cheat day with diff from integers values of weights`() =
            runBlockingTest {
                coEvery { userRepository.getCurrentUser() } returns (User("user_id", "mail"))
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    stubWeightResult(value = 93.4f)
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))
                coEvery { targetRepository.observeMaxPossibleWeight() } returns (flowOf(100f))
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    stubWeightResult(value = 91f)
                )

                interactor.addWeight(
                    stubWeightResult(value = 91.5f)
                )

                coVerify { daysInteractor.incrementCheatDays(3) }
            }

        @Test
        fun `if new weight is smaller than older with diff smaller than 1 kg, then increase cheat day`() =
            runBlockingTest {
                coEvery { userRepository.getCurrentUser() } returns (User("user_id", "mail"))
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    stubWeightResult(value = 93.4f)
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))
                coEvery { targetRepository.observeMaxPossibleWeight() } returns (flowOf(100f))
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    stubWeightResult(value = 80.0f)
                )

                interactor.addWeight(
                    stubWeightResult(value = 93.3f)
                )

                coVerify { daysInteractor.incrementCheatDays(1) }
            }

        @Test
        fun `if new weight is smaller than min weight, then increase cheat day with one extra day`() =
            runBlockingTest {
                coEvery { userRepository.getCurrentUser() } returns (User("user_id", "mail"))
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    stubWeightResult(value = 91.1f)
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    stubWeightResult(value = 91f)
                )

                interactor.addWeight(
                    stubWeightResult(value = 90.5f)
                )

                coVerify { daysInteractor.incrementCheatDays(1) }
            }

        @Test
        fun `if old value not available, then don't cheat days count`() = runBlockingTest {
            coEvery { weightResultRepository.putWeight(stubWeightResult(value = 90.5f)) } returns Content.Data(
                Unit
            )
            coEvery { userRepository.getCurrentUser() } returns (User("user_id", "mail"))
            coEvery { weightResultRepository.observeLastWeight() } returns (flowOf(null))
            coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))
            coEvery { weightResultRepository.observeMinWeight() } returns (flowOf(null))

            interactor.addWeight(
                stubWeightResult(value = 90.5f)
            )

            coVerify(inverse = true) { daysInteractor.incrementCheatDays(any()) }
        }
    }
}