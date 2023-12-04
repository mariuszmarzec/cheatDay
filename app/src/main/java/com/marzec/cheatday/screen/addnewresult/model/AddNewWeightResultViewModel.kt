package com.marzec.cheatday.screen.addnewresult.model

import com.marzec.cheatday.api.Content
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.model.domain.UpdateWeight
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toUpdate
import com.marzec.mvi.State
import com.marzec.mvi.StoreViewModel
import com.marzec.mvi.reduceContentNoChanges
import com.marzec.mvi.reduceData
import com.marzec.mvi.reduceDataWithContent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import org.joda.time.DateTime

@HiltViewModel
class AddNewWeightResultViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    defaultState: State<AddWeightData>
) : StoreViewModel<State<AddWeightData>, AddWeightSideEffect>(defaultState) {

    fun load(id: String?) = intent<Content<WeightResult>> {
        onTrigger {
            id?.toLong()?.let {
                weightInteractor.getWeight(it)
            }
        }

        reducer {
            result?.let {
                state.reduceDataWithContent(it) { weightResult ->
                    val data = this ?: AddWeightData.INITIAL
                    data.copy(
                        weightResult = weightResult,
                        weight = weightResult.value.toString(),
                        date = weightResult.date
                    )
                }
            } ?: state
        }
    }

    fun setDate(date: DateTime) = intent<Unit> {
        reducer {
            state.reduceData {
                copy(date = date)
            }
        }
    }

    fun onDatePickerClick() = intent<Unit> {
        emitSideEffect {
            state.ifDataAvailable { AddWeightSideEffect.ShowDatePicker(date) }
        }
    }

    fun save() = intent<Content<Unit>> {
        onTrigger {
            state.ifDataAvailable {
                if (weightResult.id != 0L) {
                    weightInteractor.updateWeight(
                        weightResult.id,
                        UpdateWeight(
                            value = (weight.toFloatOrNull() ?: 0f).toUpdate(weightResult.value),
                            date = date.toUpdate(weightResult.date)
                        )
                    )
                } else {
                    addNewWeight()
                }
            }
        }

        reducer { state.reduceContentNoChanges(resultNonNull()) }

        emitSideEffect {
            when (resultNonNull()) {
                is Content.Data -> AddWeightSideEffect.SaveSuccess
                is Content.Error -> AddWeightSideEffect.ShowError
                is Content.Loading -> null
            }
        }
    }

    private suspend fun AddWeightData.addNewWeight(): Flow<Content<Unit>> =
        weightInteractor.addWeight(
            weightResult.copy(
                value = weight.toFloat(),
                date = date
            )
        )

    fun setNewWeight(newWeight: String) = intent<Unit> {
        reducer {
            state.reduceData {
                copy(weight = newWeight)
            }
        }
    }
}
