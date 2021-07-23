package com.marzec.cheatday.screen.chart.model

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestCoroutineExecutorExtension
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.core.values
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.stubs.stubWeightResult
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestCoroutineExecutorExtension::class])
internal class ChartsViewModelTest {

    val weightInteractor = mockk<WeightInteractor>()

    val defaultState = ChartsState(emptyList())

    @Test
    fun loadData() {
        every { weightInteractor.observeWeights() } returns flowOf(
            Content.Data(listOf(stubWeightResult()))
        )

        val viewModel = viewModel()
        val values = viewModel.values()

        viewModel.load()

        assertThat(values).isEqualTo(
            listOf(
                defaultState,
                defaultState.copy(listOf(stubWeightResult()))
            )
        )
    }

    @Test
    fun loadData_error() {
        every { weightInteractor.observeWeights() }returns flowOf(
            Content.Error(Exception())
        )

        val viewModel = viewModel()

        viewModel.load()

        assertThat(viewModel.values()).isEqualTo(
            listOf(
                defaultState,
                ChartsSideEffect.ShowErrorDialog
            )
        )
    }

    private fun viewModel(): ChartsViewModel = ChartsViewModel(weightInteractor, defaultState)
}