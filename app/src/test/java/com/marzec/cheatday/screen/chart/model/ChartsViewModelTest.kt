package com.marzec.cheatday.screen.chart.model

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestUnconfinedCoroutineExecutorExtension
import com.marzec.content.Content
import com.marzec.cheatday.core.test
import com.marzec.cheatday.extensions.EMPTY_STRING
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.stubs.stubWeightResult
import com.marzec.mvi.State
import com.marzec.mvi.Store4Impl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestUnconfinedCoroutineExecutorExtension::class])
internal class ChartsViewModelTest {

    val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    val scope = TestScope(dispatcher)

    val weightInteractor = mockk<WeightInteractor>()

    val defaultState: State<ChartsData> = State.Loading()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        Store4Impl.stateThread = dispatcher
    }

    @Test
    fun loadData()  = scope.runTest {
        coEvery { weightInteractor.observeWeights() } returns flowOf(
            Content.Data(listOf(stubWeightResult()))
        )
        coEvery { weightInteractor.observeAverageWeights() } returns flowOf(
            Content.Data(listOf(stubWeightResult()))
        )

        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.load()

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                State.Data(ChartsData(listOf(stubWeightResult()), listOf(stubWeightResult())))
            )
        )
    }

    @Test
    fun loadData_error()  = scope.runTest {
        coEvery { weightInteractor.observeWeights() } returns flowOf(
            Content.Error(Exception())
        )
        coEvery { weightInteractor.observeAverageWeights() } returns flowOf(
            Content.Data(listOf(stubWeightResult()))
        )

        val viewModel = viewModel()
        val test = viewModel.test(this)

        viewModel.load()

        assertThat(test.values()).isEqualTo(
            listOf(
                defaultState,
                State.Error(null, message = EMPTY_STRING),
                ChartsSideEffect.ShowErrorDialog
            )
        )
    }

    @Nested
    inner class SwitchingChart {

        @Test
        fun switchIntoAverageChart()  = scope.runTest {
            val defaultState: State<ChartsData> = State.Loading(
                ChartsData(
                    weights = emptyList(),
                    averageWeights = emptyList(),
                    showAverage = false
                )
            )

            coEvery { weightInteractor.observeWeights() } returns flowOf(
                Content.Data(listOf(stubWeightResult()))
            )
            coEvery { weightInteractor.observeAverageWeights() } returns flowOf(
                Content.Data(listOf(stubWeightResult()))
            )


            val viewModel = viewModel(defaultState)
            val values = viewModel.test(this)

            viewModel.onAverageWeightChartTypeSelected()

            assertThat(values.values()).isEqualTo(
                listOf(
                    defaultState,
                    State.Loading(
                        ChartsData(
                            weights = emptyList(), averageWeights = emptyList(),
                            showAverage = true
                        )
                    ),
                )
            )
        }

        @Test
        fun switchIntoNormalChart()  = scope.runTest {
            val defaultState: State<ChartsData> = State.Loading(
                ChartsData(
                    weights = emptyList(),
                    averageWeights = emptyList(),
                    showAverage = true
                )
            )
            coEvery { weightInteractor.observeWeights() } returns flowOf(
                Content.Data(listOf(stubWeightResult()))
            )
            coEvery { weightInteractor.observeAverageWeights() } returns flowOf(
                Content.Data(listOf(stubWeightResult()))
            )


            val viewModel = viewModel(defaultState)
            val values = viewModel.test(this)

            viewModel.onWeightChartTypeSelected()

            assertThat(values.values()).isEqualTo(
                listOf(
                    defaultState,
                    State.Loading(
                        ChartsData(
                            weights = emptyList(), averageWeights = emptyList(),
                            showAverage = false
                        )
                    )
                )
            )
        }
    }

    private fun viewModel(defaultState: State<ChartsData> = this.defaultState): ChartsViewModel =
        ChartsViewModel(weightInteractor, defaultState)
}
