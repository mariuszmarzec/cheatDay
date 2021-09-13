package com.marzec.cheatday.screen.addnewresult.model

import com.marzec.cheatday.api.Content
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.mvi.StoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.flow
import org.joda.time.DateTime

@HiltViewModel
class AddNewWeightResultViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    defaultState: AddWeightState
) : StoreViewModel<AddWeightState, AddWeightSideEffect>(defaultState) {

    fun load(id: String?) = intent<Content<WeightResult>> {
        onTrigger {
            id?.toLong()?.let {
                weightInteractor.getWeight(it)
            }
        }

        reducer {
            when (val res = resultNonNull()) {
                is Content.Data -> {
                    val weightResult = res.data
                    state.copy(
                        weightResult = weightResult,
                        weight = weightResult.value.toString(),
                        date = weightResult.date
                    )

                }
                is Content.Error -> state
                is Content.Loading -> state
            }
        }
    }

    fun setDate(date: DateTime) = intent<Unit> {
        reducer {
            state.copy(date = date)
        }
    }

    fun onDatePickerClick() = intent<Unit> {
        emitSideEffect {
            AddWeightSideEffect.ShowDatePicker(state.date)
        }
    }

    fun save() = intent<Content<Unit>> {
        onTrigger {
            val weightResult = state.weightResult.copy(
                date = state.date,
                value = state.weight.toFloat()
            )
            if (state.weightResult.id != 0L) {
                weightInteractor.updateWeight(weightResult)
            } else {
                weightInteractor.addWeight(weightResult)
            }
        }

        emitSideEffect {
            when (resultNonNull()) {
                is Content.Data -> AddWeightSideEffect.SaveSuccess
                is Content.Error -> AddWeightSideEffect.ShowError
                is Content.Loading -> null
            }
        }
    }

    fun setNewWeight(newWeight: String) = intent<Unit> {
        reducer {
            state.copy(weight = newWeight)
        }
    }
}
