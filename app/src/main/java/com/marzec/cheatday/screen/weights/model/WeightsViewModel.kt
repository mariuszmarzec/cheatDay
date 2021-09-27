package com.marzec.cheatday.screen.weights.model

import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.asErrorAndReturn
import com.marzec.cheatday.api.combineContentsFlows
import com.marzec.cheatday.api.toContentData
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
import kotlinx.coroutines.flow.map

data class WeightsData(
    val minWeight: WeightResult?,
    val maxPossibleWeight: Float,
    val targetWeight: Float,
    val weights: List<WeightResult>
)

@HiltViewModel
class WeightsViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    private val mapper: WeightsMapper,
    defaultState: WeightsViewState
) : StoreViewModel<WeightsViewState, WeightsSideEffects>(defaultState) {

    fun load() = intent<Content<WeightsData>> {
        onTrigger {
            loadData()
        }

        reducer {
            reduceData()
        }

        emitSideEffect {
            resultNonNull().asErrorAndReturn { WeightsSideEffects.ShowError }
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

    fun removeWeight(id: String) = intent<Content<WeightsData>> {
        onTrigger {
            weightInteractor.removeWeight(id.toLong())
            loadData() // REMOVE LOADING
        }

        reducer {
            reduceData()
        }

        emitSideEffect {
            resultNonNull().asErrorAndReturn { WeightsSideEffects.ShowError }
        }
    }

    private suspend fun loadData(): Flow<Content<WeightsData>> = combineContentsFlows(
        weightInteractor.observeMinWeight(),
        weightInteractor.observeMaxPossibleWeight().map { it.toContentData() },
        weightInteractor.observeTargetWeight().map { it.toContentData() },
        weightInteractor.observeWeights()
    ) { minWeight, maxPossibleWeight, targetWeight, weights ->
        WeightsData(minWeight, maxPossibleWeight, targetWeight, weights)
    }

    // TODO mapper should be use in renderer
    private fun IntentBuilder.IntentContext<WeightsViewState, Content<WeightsData>>.reduceData(): WeightsViewState {
        return when (val content = resultNonNull()) {
            is Content.Data -> {
                val (min, maxPossible, target, weights) = content.data
                state.copy(list = mapper.mapWeights(min, maxPossible, target, weights))
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
