package com.marzec.cheatday.feature.home.weights

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.marzec.cheatday.OpenForTesting
import com.marzec.cheatday.common.BaseViewModel
import com.marzec.cheatday.common.SingleLiveEvent
import com.marzec.cheatday.extensions.combine
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.feature.home.weights.WeightsMapper.Companion.TARGET_ID
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.view.model.ListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@OpenForTesting
class WeightsViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    private val mapper: WeightsMapper
) : BaseViewModel() {

    val goToAddResultScreen = SingleLiveEvent<Unit>()

    val showTargetWeightDialog = SingleLiveEvent<Unit>()

    val goToChartAction = SingleLiveEvent<Unit>()

    val showError = SingleLiveEvent<Unit>()

    private val context = viewModelScope.coroutineContext + Dispatchers.IO

    @InternalCoroutinesApi
    val list: LiveData<List<ListItem>> = liveData(context) {
        combine(
            weightInteractor.observeMinWeight(),
            weightInteractor.observeTargetWeight(),
            weightInteractor.observeWeights()
        ).collect { (min, target, weights) ->
            this@liveData.emit(mapper.mapWeights(min, target, weights))
        }
    }

    fun onClick(listId: String) {
        if (listId == TARGET_ID) {
            showTargetWeightDialog.call()
        }
    }

    fun onFloatingButtonClick() {
        goToAddResultScreen.call()
    }

    fun changeTargetWeight(newTargetWeight: String) {
        viewModelScope.launch {
            newTargetWeight.toFloatOrNull()?.let { weight ->
                weightInteractor.setTargetWeight(weight)
            } ?: showError.call()
        }
    }

    fun goToChart() {
        goToChartAction.call()
    }
}
