package com.marzec.cheatday.screen.weights.model

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestCoroutineExecutorExtension
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.toContentFlow
import com.marzec.cheatday.core.test
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.stubs.stubWeightResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.lang.Exception
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestCoroutineExecutorExtension::class])
class WeightsViewModelTest {

    val minWeight = 5.0f
    val maxPossible = 10.0f
    val target = 3.0f
    val minWeightResult = stubWeightResult(value = minWeight)
    val weights = listOf(
        stubWeightResult(value = 1.0f),
        stubWeightResult(value = 2.0f),
        stubWeightResult(value = 3.0f)
    )
    val defaultState = WeightsViewState.INITIAL

    val weightInteractor: WeightInteractor = mockk(relaxed = true)
    val mapper: WeightsMapper = mockk(relaxed = true)

    @BeforeEach
    fun before() = runBlockingTest {
        coEvery {  weightInteractor.observeMinWeight() } returns minWeightResult.toContentFlow()
        coEvery {  weightInteractor.observeMaxPossibleWeight() } returns flowOf(maxPossible)
        coEvery {  weightInteractor.observeTargetWeight() } returns flowOf(target)
        coEvery {  weightInteractor.observeWeights() } returns flowOf(Content.Data(weights))
        every { mapper.mapWeights(minWeightResult, maxPossible, target, weights) } returns emptyList()
    }

    @Test
    fun load_Data() = runBlockingTest {
        coEvery {  weightInteractor.observeWeights() } returns flowOf(Content.Data(weights))

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
    fun load_Loading() = runBlockingTest {
        coEvery {  weightInteractor.observeWeights() } returns flowOf(Content.Loading(weights))

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
    fun load_Error() = runBlockingTest {
        coEvery {  weightInteractor.observeWeights() } returns flowOf(Content.Error(Exception()))

        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.load()

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState,
                WeightsSideEffects.ShowError
            )
        )
    }

    @Test
    fun onClick_targetId() = runBlockingTest {
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
    fun onClick_maxPossibleId() = runBlockingTest {
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
    fun onClick_openWeight() = runBlockingTest {
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
    fun onLongClick() = runBlockingTest {
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
    fun onFloatingButtonClick() = runBlockingTest {
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
    fun changeTargetWeight() = runBlockingTest {
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
    fun changeTargetWeight_Error() = runBlockingTest {
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
    fun changeMaxWeight() = runBlockingTest {
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
    fun changeMaxWeight_Error() = runBlockingTest {
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
    fun goToChart() = runBlockingTest {
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
    fun removeWeight() = runBlockingTest {
        val viewModel = viewModel()
        val values = viewModel.test(this)

        viewModel.removeWeight("0")

        assertThat(values.values()).isEqualTo(
            listOf(
                defaultState
            )
        )
    }

    private fun viewModel(state: WeightsViewState = defaultState) =
        WeightsViewModel(weightInteractor, mapper, state)
}