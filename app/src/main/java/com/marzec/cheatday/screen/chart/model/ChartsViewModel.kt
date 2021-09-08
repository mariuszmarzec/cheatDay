package com.marzec.cheatday.screen.chart.model

import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.asErrorAndReturn
import com.marzec.cheatday.api.getMessage
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.mvi.StoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    defaultState: ChartsState
) : StoreViewModel<ChartsState, ChartsSideEffect>(defaultState) {

    fun load() = intent<Content<List<WeightResult>>> {
        onTrigger { weightInteractor.observeWeights() }

        reducer {
            when (val content = resultNonNull()) {
                is Content.Data -> {
                    state.copy(weights = content.data)
                }
                else -> state
            }
        }
        emitSideEffect {
            resultNonNull().asErrorAndReturn { ChartsSideEffect.ShowErrorDialog }
        }
    }
}
