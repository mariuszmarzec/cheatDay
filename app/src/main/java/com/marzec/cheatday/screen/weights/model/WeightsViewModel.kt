package com.marzec.cheatday.screen.weights.model

import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.asErrorAndReturn
import com.marzec.cheatday.api.combineContentsFlows
import com.marzec.cheatday.api.toContentData
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.screen.weights.model.WeightsMapper.Companion.MAX_POSSIBLE_ID
import com.marzec.cheatday.screen.weights.model.WeightsMapper.Companion.TARGET_ID
import com.marzec.cheatday.screen.weights.model.WeightsMapper.Companion.WEEK_AVERAGE_ID
import com.marzec.mvi.State
import com.marzec.mvi.StoreViewModel
import com.marzec.mvi.reduceContentAsSideAction
import com.marzec.mvi.reduceDataWithContent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@HiltViewModel
class WeightsViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    defaultState: State<WeightsData>
) : StoreViewModel<State<WeightsData>, WeightsSideEffects>(defaultState) {

    fun load() = intent<Content<WeightsData>> {
        onTrigger {
            loadData()
        }

        reducer {
            state.reduceDataWithContent(resultNonNull()) { it }
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
                WEEK_AVERAGE_ID -> null
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

    fun removeWeight(id: String) = intent<Content<Unit>> {
        onTrigger { weightInteractor.removeWeight(id.toLong()) }

        reducer { state.reduceContentAsSideAction(resultNonNull()) }

        sideEffect {
            val content = resultNonNull()
            if (content is Content.Data) {
                load()
            } else {
                content.asErrorAndReturn { sideEffectsInternal.emit(WeightsSideEffects.ShowError) }
            }
        }
    }

    private suspend fun loadData(): Flow<Content<WeightsData>> = combineContentsFlows(
        weightInteractor.observeMinWeight(),
        weightInteractor.observeWeekAverage(),
        weightInteractor.observeMaxPossibleWeight().map { it.toContentData() },
        weightInteractor.observeTargetWeight().map { it.toContentData() },
        weightInteractor.observeWeights()
    ) { minWeight, weekAverage, maxPossibleWeight, targetWeight, weights ->
        WeightsData(minWeight, weekAverage, maxPossibleWeight, targetWeight, weights)
    }
}
