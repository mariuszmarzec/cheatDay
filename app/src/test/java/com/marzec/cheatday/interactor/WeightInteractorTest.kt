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
    fun setUp() = test {
        coEvery { weightResultRepository.observeLastWeight() } returns flowOf(Content.Data(null))
        coEvery { weightResultRepository.observeMinWeight() } returns flowOf(Content.Data(null))
        coEvery { targetRepository.observeMaxPossibleWeight() } returns (flowOf(0f))

        coEvery { weightResultRepository.putWeight(any()) } returns flowOf(
            Content.Data(
                stubWeightResult()
            )
        )
        coEvery { weightResultRepository.updateWeight(any()) } returns flowOf(Content.Data(Unit))

        coEvery { daysInteractor.incrementCheatDays(any()) } returns Unit

        interactor = WeightInteractor(
            targetRepository,
            weightResultRepository,
            daysInteractor
        )
    }

    @Test
    fun `given target weight is available, when target weight is observed, then value is returned as flow`() = test {
        coEvery { targetRepository.observeTargetWeight() } returns flowOf(1f, 2f)

        assertThat(interactor.observeTargetWeight().test(this).values()).isEqualTo(
            listOf(1f, 2f)
        )
    }

    @Test
    fun `when target weight is updated, then target weight is called`() = test {
        coEvery { targetRepository.setTargetWeight(1f) } returns Unit

        interactor.setTargetWeight(1f)

        coVerify { targetRepository.setTargetWeight(1f) }
    }

    @Test
    fun observeWeights() = test {
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

    @Nested
    inner class WeekAverage {

        @Test
        fun `given seven or more weights are available, when observing average weight, then week average is returned`() = test {
            coEvery { weightResultRepository.observeWeights() } returns flowOf(
                Content.Data(
                    listOf(
                        stubWeightResult(value = 10f),
                        stubWeightResult(value = 1f),
                        stubWeightResult(value = 1f),
                        stubWeightResult(value = 10f),
                        stubWeightResult(value = 1f),
                        stubWeightResult(value = 2f),
                        stubWeightResult(value = 3f),
                        stubWeightResult(value = 12f),
                        stubWeightResult(value = 11f),
                        stubWeightResult(value = 10f)
                    )
                )
            )

            interactor.observeWeekAverage().test(this).isEqualTo(
                Content.Data(4f)
            )
        }

        @Test
        fun `given seven or more weights are available, when observing average weight, then no data are available`() = test {
            coEvery { weightResultRepository.observeWeights() } returns flowOf(
                Content.Data(
                    listOf(
                        stubWeightResult(value = 10f),
                        stubWeightResult(value = 0f),
                        stubWeightResult(value = 1f),
                        stubWeightResult(value = 10f),
                        stubWeightResult(value = 3f),
                    )
                )
            )

            interactor.observeWeekAverage().test(this).isEqualTo(
                Content.Data(null)
            )
        }

        @Test
        fun `given seven or more weights are available, when observing average weights, then they are returned`() = test {
            coEvery { weightResultRepository.observeWeights() } returns flowOf(
                Content.Data(
                    listOf(
                        stubWeightResult(value = 56f),
                        stubWeightResult(value = 7f),
                        stubWeightResult(value = 7f),
                        stubWeightResult(value = 7f),
                        stubWeightResult(value = 7f),
                        stubWeightResult(value = 7f),
                        stubWeightResult(value = 7f),
                        stubWeightResult(value = 7f),
                        stubWeightResult(value = 7f),
                    )
                )
            )

            assertThat(interactor.observeAverageWeights().test(this).values()).isEqualTo(
                listOf(
                    Content.Data(
                        listOf(
                            stubWeightResult(value = 14f),
                            stubWeightResult(value = 7f),
                            stubWeightResult(value = 7f)
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `when adding weight, then weight result repository is called`() = test {
        coEvery { weightResultRepository.putWeight(stubWeightResult()) } returns flowOf(
            Content.Data(
                stubWeightResult()
            )
        )

        interactor.addWeight(stubWeightResult()).test(this)

        coVerify { weightResultRepository.putWeight(stubWeightResult()) }
    }

    @Test
    fun `when updating weight, then weight result repository is called`() = test {
        coEvery { weightResultRepository.updateWeight(stubWeightResult()) } returns flowOf(
            Content.Data(Unit)
        )

        interactor.updateWeight(stubWeightResult())

        coVerify { weightResultRepository.updateWeight(stubWeightResult()) }
    }

    @Test
    fun `when added weight is not today, then don't change cheat days count`() = test {
        coEvery { weightResultRepository.putWeight(stubWeightResult(value = 90.5f)) } returns flowOf(
            Content.Data(stubWeightResult(value = 90.5f))
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
        fun setUp() = test {
            DateTimeUtils.setCurrentMillisFixed(0)
        }

        @Test
        @Suppress("MaxLineLength")
        fun `given new weight is greater than older and target weight, when adding new weight, then decrease cheat day, and additionally decrease cheat day with diff from integers values of weights`() =
            test {
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
        fun `given new weight is greater than older with diff smaller than 1 kg than and then target weight, when adding new weight, then decrease cheat day`() =
            test {
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
        fun `given new weight is greater than older, but not than target weight, when adding new weight, then don't change cheat days count`() =
            test {
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
        fun `given new weight is smaller than older, when adding new weight, then increase cheat day and additionally increase cheat day with diff from integers values of weights`() =
            test {
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
        fun `given new weight is smaller than older with diff smaller than 1 kg, when adding new weight, then increase cheat day`() =
            test {
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
        fun `given new weight is smaller than min weight, when adding new weight, then increase cheat day with one extra day`() =
            test {
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
        fun `given old value not available, when adding new weight, then don't cheat days count`() = test {
            coEvery { weightResultRepository.putWeight(stubWeightResult(value = 90.5f)) } returns flowOf(
                Content.Data(stubWeightResult(value = 90.5f))
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
