package com.marzec.cheatday.screen.weights.model

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestUnconfinedCoroutineExecutorExtension
import com.marzec.content.Content
import com.marzec.content.toContentFlow
import com.marzec.cheatday.core.test
import com.marzec.cheatday.extensions.EMPTY_STRING
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.stubs.stubWeightResult
import com.marzec.mvi.State
import io.mockk.coEvery
import io.mockk.mockk
import java.lang.Exception
import kotlinx.coroutines.flow.flowOf

import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestUnconfinedCoroutineExecutorExtension::class])
class WeightsViewModelTest {

    val minWeight = 5.0f
    val weekAverage = 6.0f
    val maxPossible = 10.0f
    val target = 3.0f
    val minWeightResult = stubWeightResult(value = minWeight)
    val weights = listOf(
        stubWeightResult(value = 1.0f),
        stubWeightResult(value = 2.0f),
        stubWeightResult(value = 3.0f)
    )
    val defaultState = State.Loading<WeightsData>()

    val weightInteractor: WeightInteractor = mockk(relaxed = true)

    @BeforeEach
    fun before() = test {
        coEvery { weightInteractor.observeMinWeight() } returns minWeightResult.toContentFlow()
        coEvery { weightInteractor.observeWeekAverage() } returns weekAverage.toContentFlow()
        coEvery { weightInteractor.observeMaxPossibleWeight() } returns flowOf(maxPossible)
        coEvery { weightInteractor.observeTargetWeight() } returns flowOf(target)
        coEvery { weightInteractor.observeWeights() } returns flowOf(Content.Data(weights))
    }

    @Test
    fun load_Data() = test {
        coEvery { weightInteractor.observeWeights() } returns flowOf(Content.Data(weights))

        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.load()

        values.isEqualTo(
            defaultState,
            State.Data(
                WeightsData(
                    minWeight = minWeightResult,
                    weekAverage = weekAverage,
                    maxPossibleWeight = maxPossible,
                    targetWeight = target,
                    weights = weights
                )
            )
        )
    }

    @Test
    fun load_Loading() = test {
        coEvery { weightInteractor.observeWeights() } returns flowOf(Content.Loading(weights))

        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.load()

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState
            )
        )
    }

    @Test
    fun load_Error() = test {
        coEvery { weightInteractor.observeWeights() } returns flowOf(Content.Error(Exception()))

        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.load()

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                State.Error(null, message = EMPTY_STRING)
            )
        )
    }

    @Test
    fun onClick_targetId() = test {
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.onClick(WeightsMapper.TARGET_ID)

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                WeightsSideEffects.ShowTargetWeightDialog
            )
        )
    }

    @Test
    fun onClick_maxPossibleId() = test {
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.onClick(WeightsMapper.MAX_POSSIBLE_ID)

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                WeightsSideEffects.ShowMaxPossibleWeightDialog
            )
        )
    }

    @Test
    fun onClick_openWeight() = test {
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.onClick("0")

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                WeightsSideEffects.OpenWeightAction("0")
            )
        )
    }

    @Test
    fun onLongClick() = test {
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.onLongClick("0")

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                WeightsSideEffects.ShowRemoveDialog("0")
            )
        )
    }

    @Test
    fun onFloatingButtonClick() = test {
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.onFloatingButtonClick()

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                WeightsSideEffects.GoToAddResultScreen
            )
        )
    }

    @Test
    fun changeTargetWeight() = test {
        coEvery { weightInteractor.setTargetWeight(5.0f) } returns Unit
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.changeTargetWeight("5.0f")

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState
            )
        )
    }

    @Test
    fun changeTargetWeight_Error() = test {
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.changeTargetWeight("")

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                WeightsSideEffects.ShowError
            )
        )
    }

    @Test
    fun changeMaxWeight() = test {
        coEvery { weightInteractor.setMaxPossibleWeight(5.0f) } returns Unit
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.changeMaxWeight("5.0f")

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState
            )
        )
    }

    @Test
    fun changeMaxWeight_Error() = test {
        coEvery { weightInteractor.setMaxPossibleWeight(5.0f) } returns Unit
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.changeMaxWeight("")

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                WeightsSideEffects.ShowError
            )
        )
    }

    @Test
    fun goToChart() = test {
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.goToChart()

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                WeightsSideEffects.GoToChartAction
            )
        )
    }

    @Test
    fun removeWeight() = test {
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.removeWeight("0")

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState
            )
        )
    }

    private fun viewModel(state: State<WeightsData> = defaultState) =
        WeightsViewModel(weightInteractor, state)
}
