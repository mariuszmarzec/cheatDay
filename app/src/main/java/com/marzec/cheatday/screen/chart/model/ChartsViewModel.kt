package com.marzec.cheatday.screen.chart.model

import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.asErrorAndReturn
import com.marzec.cheatday.api.combineContentsFlows
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.mvi.State
import com.marzec.mvi.StoreViewModel
import com.marzec.mvi.reduceData
import com.marzec.mvi.reduceDataWithContent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    defaultState: State<ChartsData>
) : StoreViewModel<State<ChartsData>, ChartsSideEffect>(defaultState) {

    fun load() = intent<Content<Pair<List<WeightResult>, List<WeightResult>>>> {
        onTrigger {
            combineContentsFlows(
                weightInteractor.observeWeights(),
                weightInteractor.observeAverageWeights()
            ) { weights, averageWeights ->
                weights to averageWeights
            }
        }

        reducer {
            state.reduceDataWithContent(resultNonNull()) {
                val data = this ?: ChartsData.INITIAL
                data.copy(weights = it.first, averageWeights = it.second)
            }
        }
        emitSideEffect {
            resultNonNull().asErrorAndReturn { ChartsSideEffect.ShowErrorDialog }
        }
    }

    fun onWeightChartTypeSelected() = intent<Unit> {
        reducer {
            state.reduceData { copy(showAverage = false) }
        }
    }

    fun onAverageWeightChartTypeSelected() = intent<Unit> {
        reducer {
            state.reduceData { copy(showAverage = true) }
        }
    }
}
