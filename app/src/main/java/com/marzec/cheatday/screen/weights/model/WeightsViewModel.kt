package com.marzec.cheatday.screen.weights.model

import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.asErrorAndReturn
import com.marzec.cheatday.api.dataOrNull
import com.marzec.cheatday.api.unwrapContent
import com.marzec.cheatday.extensions.Quadruple
import com.marzec.cheatday.extensions.combine
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.screen.weights.model.WeightsMapper.Companion.MAX_POSSIBLE_ID
import com.marzec.cheatday.screen.weights.model.WeightsMapper.Companion.TARGET_ID
import com.marzec.mvi.IntentBuilder
import com.marzec.mvi.StoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

typealias WeightsData = Quadruple<WeightResult?, Float, Float, Content<List<WeightResult>>>

@HiltViewModel
class WeightsViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    private val mapper: WeightsMapper,
    defaultState: WeightsViewState
) : StoreViewModel<WeightsViewState, WeightsSideEffects>(defaultState) {

    fun load() = intent<WeightsData> {
        onTrigger {
            loadData()
        }

        reducer {
            reduceData()
        }

        emitSideEffect {
            resultNonNull().ob4.asErrorAndReturn { WeightsSideEffects.ShowError }
        }
    }

    fun onClick(listId: String) = intent<Unit> {
        emitSideEffect {
            when (listId) {
                TARGET_ID -> {
                    WeightsSideEffects.ShowTargetWeightDialog
                }
                MAX_POSSIBLE_ID -> {
                    WeightsSideEffects.ShowMaxPossibleWeightDialog
                }
                else -> {
                    WeightsSideEffects.OpenWeightAction(listId)
                }
            }
        }
    }

    fun onLongClick(listId: String) = intent<Unit> {
        emitSideEffect { WeightsSideEffects.ShowRemoveDialog(listId) }
    }

    fun onFloatingButtonClick() = intent<Unit> {
        emitSideEffect { WeightsSideEffects.GoToAddResultScreen }
    }

    fun changeTargetWeight(newTargetWeight: String) = intent<Unit> {
        onTrigger {
            newTargetWeight.toFloatOrNull()?.let { weight ->
                flow {
                    emit(weightInteractor.setTargetWeight(weight))
                }
            }
        }

        emitSideEffect {
            if (result == null) WeightsSideEffects.ShowError else null
        }
    }


    fun changeMaxWeight(maxWeight: String) = intent<Unit> {
        onTrigger {
            maxWeight.toFloatOrNull()?.let { weight ->
                flow {
                    emit(weightInteractor.setMaxPossibleWeight(weight))
                }
            }
        }

        emitSideEffect {
            if (result == null) WeightsSideEffects.ShowError else null
        }
    }

    fun goToChart() = intent<Unit> {
        emitSideEffect { WeightsSideEffects.GoToChartAction }
    }

    fun removeWeight(id: String) = intent<WeightsData> {
        onTrigger {
            weightInteractor.removeWeight(id.toLong())
            loadData()
        }

        reducer {
            reduceData()
        }

        emitSideEffect {
            resultNonNull().ob4.asErrorAndReturn { WeightsSideEffects.ShowError }
        }
    }

    // TODO Extract flow content
    private fun loadData(): Flow<WeightsData> = combine(
        weightInteractor.observeMinWeight().unwrapContent(),
        weightInteractor.observeMaxPossibleWeight(),
        weightInteractor.observeTargetWeight(),
        weightInteractor.observeWeights()
    )

    // TODO mapper should be use in renderer
    private fun IntentBuilder.IntentContext<WeightsViewState, WeightsData>.reduceData(): WeightsViewState {
        val (min, maxPossible, target, content) = resultNonNull()
        return when (content) {
            is Content.Data -> {
                state.copy(list = mapper.mapWeights(min, maxPossible, target, content.data))
            }
            is Content.Error -> {
                state.copy()
            }
            is Content.Loading -> {
                state.copy()
            }
        }
    }
}
