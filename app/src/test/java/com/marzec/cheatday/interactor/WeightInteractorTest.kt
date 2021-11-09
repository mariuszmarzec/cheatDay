package com.marzec.cheatday.interactor

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.toContentData
import com.marzec.cheatday.core.test
import com.marzec.cheatday.repository.UserPreferencesRepository
import com.marzec.cheatday.repository.WeightResultRepository
import com.marzec.cheatday.stubs.stubWeightResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.joda.time.DateTimeUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class WeightInteractorTest {

    private val targetRepository: UserPreferencesRepository = mockk()
    private val weightResultRepository: WeightResultRepository = mockk()
    private val daysInteractor: DaysInteractor = mockk()

    lateinit var interactor: WeightInteractor

    @BeforeEach
    fun setUp() = runBlockingTest {
        coEvery { weightResultRepository.observeLastWeight() } returns flowOf(Content.Data(null))
        coEvery { weightResultRepository.observeMinWeight() } returns flowOf(Content.Data(null))
        coEvery { targetRepository.observeMaxPossibleWeight() } returns (flowOf(0f))

        coEvery { weightResultRepository.putWeight(any()) } returns flowOf(Content.Data(Unit))
        coEvery { weightResultRepository.updateWeight(any()) } returns flowOf(Content.Data(Unit))

        coEvery { daysInteractor.incrementCheatDays(any()) } returns Unit

        interactor = WeightInteractor(
            targetRepository,
            weightResultRepository,
            daysInteractor
        )
    }

    @Test
    fun observeTargetWeight() = runBlockingTest {
        coEvery { targetRepository.observeTargetWeight() } returns flowOf(1f, 2f)

        assertThat(interactor.observeTargetWeight().test(this).values()).isEqualTo(
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
        coEvery { weightResultRepository.observeWeights() } returns flowOf(
            Content.Data(
                listOf(
                    stubWeightResult(),
                    stubWeightResult()
                )
            )
        )

        assertThat(interactor.observeWeights().test(this).values()).isEqualTo(
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
        coEvery { weightResultRepository.putWeight(stubWeightResult()) } returns flowOf(
            Content.Data(
                Unit
            )
        )

        interactor.addWeight(stubWeightResult()).test(this)

        coVerify { weightResultRepository.putWeight(stubWeightResult()) }
    }

    @Test
    fun updateWeight() = runBlockingTest {
        coEvery { weightResultRepository.putWeight(stubWeightResult()) } returns flowOf(
            Content.Data(
                Unit
            )
        )

        interactor.updateWeight(stubWeightResult())

        coVerify { weightResultRepository.updateWeight(stubWeightResult()) }
    }

    @Test
    fun `if added weight is not today, then don't change cheat days count`() = runBlockingTest {
        coEvery { weightResultRepository.putWeight(stubWeightResult(value = 90.5f)) } returns flowOf(
            Content.Data(Unit)
        )

        interactor.addWeight(
            stubWeightResult(value = 90.5f)
        ).test(this).values()

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
        @Suppress("MaxLineLength")
        fun `if new weight is greater than older and target weight, then decrease cheat day, and additionally decrease cheat day with diff from integers values of weights`() =
            runBlockingTest {
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    Content.Data(stubWeightResult(value = 93.4f))
                )
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    Content.Data(stubWeightResult(value = 91f))
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))

                interactor.addWeight(
                    stubWeightResult(value = 95.5f)
                ).test(this)

                coVerify { daysInteractor.incrementCheatDays(-3) }
            }

        @Test
        @Suppress("MaxLineLength")
        fun `if new weight is greater than older with diff smaller than 1 kg than and then target weight, then decrease cheat day`() =
            runBlockingTest {
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    Content.Data(stubWeightResult(value = 93.4f))
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    Content.Data(stubWeightResult(value = 91f))
                )

                interactor.addWeight(
                    stubWeightResult(value = 93.6f)
                ).test(this)

                coVerify { daysInteractor.incrementCheatDays(-1) }
            }

        @Test
        @Suppress("MaxLineLength")
        fun `if new weight is greater than older, but not than target weight, then don't change cheat days count`() =
            runBlockingTest {
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    Content.Data(stubWeightResult(value = 93.4f))
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(100f))
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    stubWeightResult(value = 91f).toContentData()
                )

                interactor.addWeight(
                    stubWeightResult(value = 95.5f)
                )

                coVerify(inverse = true) { daysInteractor.incrementCheatDays(any()) }
            }

        @Test
        @Suppress("MaxLineLength")
        fun `if new weight is smaller than older, then increase cheat day, and additionally increase cheat day with diff from integers values of weights`() =
            runBlockingTest {
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    stubWeightResult(value = 93.4f).toContentData()
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))
                coEvery { targetRepository.observeMaxPossibleWeight() } returns (flowOf(100f))
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    stubWeightResult(value = 91f).toContentData()
                )

                interactor.addWeight(
                    stubWeightResult(value = 91.5f)
                ).test(this)

                coVerify { daysInteractor.incrementCheatDays(3) }
            }

        @Test
        fun `if new weight is smaller than older with diff smaller than 1 kg, then increase cheat day`() =
            runBlockingTest {
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    stubWeightResult(value = 93.4f).toContentData()
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))
                coEvery { targetRepository.observeMaxPossibleWeight() } returns (flowOf(100f))
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    stubWeightResult(value = 80.0f).toContentData()
                )

                interactor.addWeight(
                    stubWeightResult(value = 93.3f)
                ).test(this)

                coVerify { daysInteractor.incrementCheatDays(1) }
            }

        @Test
        fun `if new weight is smaller than min weight, then increase cheat day with one extra day`() =
            runBlockingTest {
                coEvery { weightResultRepository.observeLastWeight() } returns flowOf(
                    stubWeightResult(value = 91.1f).toContentData()
                )
                coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))
                coEvery { weightResultRepository.observeMinWeight() } returns flowOf(
                    stubWeightResult(value = 91f).toContentData()
                )

                interactor.addWeight(
                    stubWeightResult(value = 90.5f)
                ).test(this)

                coVerify { daysInteractor.incrementCheatDays(1) }
            }

        @Test
        fun `if old value not available, then don't cheat days count`() = runBlockingTest {
            coEvery { weightResultRepository.putWeight(stubWeightResult(value = 90.5f)) } returns flowOf(
                Content.Data(Unit)
            )
            coEvery { weightResultRepository.observeLastWeight() } returns flowOf(Content.Data(null))
            coEvery { targetRepository.observeTargetWeight() } returns (flowOf(90f))
            coEvery { weightResultRepository.observeMinWeight() } returns flowOf(Content.Data(null))

            interactor.addWeight(
                stubWeightResult(value = 90.5f)
            )

            coVerify(inverse = true) { daysInteractor.incrementCheatDays(any()) }
        }
    }
}