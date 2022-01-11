package com.marzec.cheatday.screen.addnewresult.model

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.InstantExecutorExtension
import com.marzec.cheatday.TestUnconfinedCoroutineExecutorExtension
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.toContentFlow
import com.marzec.cheatday.core.test
import com.marzec.cheatday.extensions.EMPTY_STRING
import com.marzec.cheatday.extensions.asFlow
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.stubs.stubWeightResult
import com.marzec.mvi.State
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(value = [InstantExecutorExtension::class, TestUnconfinedCoroutineExecutorExtension::class])
internal class AddNewWeightResultViewModelTest {

    val weightInteractor = mockk<WeightInteractor>()

    val defaultData by lazy { AddWeightData.INITIAL }
    val defaultState by lazy { State.Data(defaultData) }

    private fun viewModel(
        weightInteractor: WeightInteractor = this.weightInteractor,
        defaultState: State<AddWeightData> = this.defaultState
    ) = AddNewWeightResultViewModel(
        weightInteractor,
        defaultState
    )

    @BeforeEach
    fun setup() = test {
        DateTimeUtils.setCurrentMillisFixed(0)
    }

    @Test
    fun load() = test {
        val viewModel = viewModel()
        val states = viewModel.state.test(this)

        viewModel.load(id = null)

        assertThat(states.values()).isEqualTo(
            listOf(
                defaultState
            )
        )
    }

    @Test
    fun load_withId() = test {
        coEvery { weightInteractor.getWeight(1) } returns stubWeightResult(
            value = 10f,
            date = DateTime(10)
        ).toContentFlow()

        val viewModel = viewModel()
        val states = viewModel.state.test(this)

        viewModel.load(id = "1")

        assertThat(states.values()).isEqualTo(
            listOf(
                defaultState,
                State.Data(
                    AddWeightData(
                        weightResult = stubWeightResult(value = 10f, date = DateTime(10)),
                        weight = "10.0",
                        date = DateTime(10)
                    )
                )
            )
        )
    }

    @Test
    fun load_withId_failed() = test {
        coEvery { weightInteractor.getWeight(1) } returns flowOf(Content.Error(Exception()))
        val viewModel = viewModel()
        val states = viewModel.state.test(this)

        viewModel.load(id = "1")

        assertThat(states.values()).isEqualTo(
            listOf(
                defaultState,
                State.Error(defaultData, EMPTY_STRING)
            )
        )
    }


    @Test
    fun setDate() = test {
        val viewModel = viewModel()
        val states = viewModel.state.test(this)

        viewModel.setDate(DateTime(10))

        assertThat(states.values()).isEqualTo(
            listOf(
                defaultState,
                State.Data(defaultData.copy(date = DateTime(10)))
            )
        )
    }

    @Test
    fun onDatePickerClick() = test {
        val viewModel = viewModel()
        val states = viewModel.test(this)

        viewModel.onDatePickerClick()

        assertThat(states.values()).isEqualTo(
            listOf(
                defaultState,
                AddWeightSideEffect.ShowDatePicker(DateTime(0))
            )
        )
    }

    @Test
    fun save_addNewResult() = test {
        coEvery { weightInteractor.addWeight(stubWeightResult()) } returns Content.Data(Unit)
            .asFlow()
        val viewModel = viewModel()
        val states = viewModel.test(this)

        viewModel.save()

        assertThat(states.values()).isEqualTo(
            listOf(
                defaultState,
                AddWeightSideEffect.SaveSuccess
            )
        )
    }

    @Test
    fun save_addNewResult_error() = test {
        coEvery { weightInteractor.addWeight(stubWeightResult()) } returns Content.Error<Unit>(
            Exception()
        ).asFlow()
        val viewModel = viewModel()
        val states = viewModel.test(this)

        viewModel.save()

        assertThat(states.values()).isEqualTo(
            listOf(
                defaultState,
                AddWeightSideEffect.ShowError,
                State.Error(defaultData, EMPTY_STRING)
            )
        )
    }

    @Test
    fun save_updateResult() = test {
        coEvery { weightInteractor.updateWeight(stubWeightResult(id = 1)) } returns Content.Data(
            Unit
        ).asFlow()
        val viewModel = viewModel(
            defaultState = State.Data(defaultData.copy(weightResult = stubWeightResult(id = 1)))
        )
        val states = viewModel.test(this)

        viewModel.save()

        assertThat(states.values()).isEqualTo(
            listOf(
                State.Data(defaultData.copy(weightResult = stubWeightResult(id = 1))),
                AddWeightSideEffect.SaveSuccess
            )
        )
    }

    @Test
    fun save_updateResult_error() = test {
        coEvery { weightInteractor.updateWeight(stubWeightResult(id = 1)) } returns Content.Error<Unit>(
            Exception()
        ).asFlow()
        val data = defaultData.copy(weightResult = stubWeightResult(id = 1))
        val viewModel = viewModel(
            defaultState = State.Data(data)
        )
        val states = viewModel.test(this)

        viewModel.save()

        assertThat(states.values()).isEqualTo(
            listOf(
                State.Data(data),
                AddWeightSideEffect.ShowError,
                State.Error(data, EMPTY_STRING)
            )
        )
    }

    @Test
    fun setNewWeight() = test {
        val viewModel = viewModel()
        val states = viewModel.state.test(this)

        viewModel.setNewWeight("11.0")

        assertThat(states.values()).isEqualTo(
            listOf(
                State.Data(defaultData),
                State.Data(defaultData.copy(weight = "11.0"))
            )
        )
    }
}
