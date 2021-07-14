package com.marzec.cheatday.screen.addnewresult.model

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestCoroutineExecutorExtension
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.core.values
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.stubs.stubWeightResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.lang.Exception
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class, TestCoroutineExecutorExtension::class])
internal class AddNewWeightResultViewModelTest {

    val weightInteractor = mockk<WeightInteractor>()

    private fun viewModel(
        weightInteractor: WeightInteractor = this.weightInteractor,
        defaultState: AddWeightState = AddWeightState.INITIAL
    ) = AddNewWeightResultViewModel(
        weightInteractor,
        defaultState
    )

    @BeforeEach
    fun setup() {
        DateTimeUtils.setCurrentMillisFixed(0)
    }

    @Test
    fun load() {
        val viewModel = viewModel()
        val states = viewModel.state.values()

        viewModel.load(id = null)

        assertThat(states).isEqualTo(
            listOf(
                AddWeightState.INITIAL
            )
        )
    }

    @Test
    fun load_withId() {
        coEvery { weightInteractor.getWeight(1) } returns stubWeightResult(
            value = 10f,
            date = DateTime(10)
        )
        val viewModel = viewModel()
        val states = viewModel.state.values()

        viewModel.load(id = "1")

        assertThat(states).isEqualTo(
            listOf(
                AddWeightState.INITIAL,
                AddWeightState(
                    weightResult = stubWeightResult(value = 10f, date = DateTime(10)),
                    weight = "10.0",
                    date = DateTime(10)
                )
            )
        )
    }

    @Test
    fun load_withId_failed() {
        coEvery { weightInteractor.getWeight(1) } returns null
        val viewModel = viewModel()
        val states = viewModel.state.values()

        viewModel.load(id = "1")

        assertThat(states).isEqualTo(
            listOf(
                AddWeightState.INITIAL
            )
        )
    }


    @Test
    fun setDate() {
        val viewModel = viewModel()
        val states = viewModel.state.values()

        viewModel.setDate(DateTime(10))

        assertThat(states).isEqualTo(
            listOf(
                AddWeightState.INITIAL,
                AddWeightState.INITIAL.copy(date = DateTime(10))
            )
        )
    }

    @Test
    fun onDatePickerClick() {
        val viewModel = viewModel()
        val states = viewModel.values()

        viewModel.onDatePickerClick()

        assertThat(states).isEqualTo(
            listOf(
                AddWeightState.INITIAL,
                AddWeightSideEffect.ShowDatePicker(DateTime(0))
            )
        )
    }

    @Test
    fun save_addNewResult() {
        coEvery { weightInteractor.addWeight(stubWeightResult()) } returns Content.Data(Unit)
        val viewModel = viewModel()
        val states = viewModel.values()

        viewModel.save()

        assertThat(states).isEqualTo(
            listOf(
                AddWeightState.INITIAL,
                AddWeightSideEffect.SaveSuccess
            )
        )
    }

    @Test
    fun save_addNewResult_error() {
        coEvery { weightInteractor.addWeight(stubWeightResult()) } returns Content.Error(
            Exception()
        )
        val viewModel = viewModel()
        val states = viewModel.values()

        viewModel.save()

        assertThat(states).isEqualTo(
            listOf(
                AddWeightState.INITIAL,
                AddWeightSideEffect.ShowError
            )
        )
    }

    @Test
    fun save_updateResult() {
        coEvery { weightInteractor.updateWeight(stubWeightResult(id = 1)) } returns Content.Data(
            Unit
        )
        val viewModel = viewModel(
            defaultState = AddWeightState.INITIAL.copy(weightResult = stubWeightResult(id = 1))
        )
        val states = viewModel.values()

        viewModel.save()

        assertThat(states).isEqualTo(
            listOf(
                AddWeightState.INITIAL.copy(weightResult = stubWeightResult(id = 1)),
                AddWeightSideEffect.SaveSuccess
            )
        )
    }

    @Test
    fun save_updateResult_error() {
        coEvery { weightInteractor.updateWeight(stubWeightResult(id = 1)) } returns Content.Error(
            Exception()
        )
        val viewModel = viewModel(
            defaultState = AddWeightState.INITIAL.copy(weightResult = stubWeightResult(id = 1))
        )
        val states = viewModel.values()

        viewModel.save()

        assertThat(states).isEqualTo(
            listOf(
                AddWeightState.INITIAL.copy(weightResult = stubWeightResult(id = 1)),
                AddWeightSideEffect.ShowError
            )
        )
    }

    @Test
    fun setNewWeight() {
        val viewModel = viewModel()
        val states = viewModel.state.values()

        viewModel.setNewWeight("11.0")

        assertThat(states).isEqualTo(
            listOf(
                AddWeightState.INITIAL,
                AddWeightState.INITIAL.copy(weight = "11.0")
            )
        )

    }
}