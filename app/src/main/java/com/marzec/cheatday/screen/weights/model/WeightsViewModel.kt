package com.marzec.cheatday.screen.weights.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marzec.cheatday.OpenForTesting
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.common.SingleLiveEvent
import com.marzec.cheatday.extensions.combine
import com.marzec.cheatday.screen.weights.model.WeightsMapper.Companion.TARGET_ID
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.screen.weights.model.WeightsMapper.Companion.MAX_POSSIBLE_ID
import com.marzec.cheatday.view.model.ListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@OpenForTesting
@HiltViewModel
class WeightsViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    private val mapper: WeightsMapper
) : ViewModel() {

    val goToAddResultScreen = SingleLiveEvent<Unit>()

    val showTargetWeightDialog = SingleLiveEvent<Unit>()

    val showMaxPossibleWeightDialog = SingleLiveEvent<Unit>()

    val goToChartAction = SingleLiveEvent<Unit>()

    val showError = SingleLiveEvent<Unit>()

    val openWeightAction = SingleLiveEvent<String>()

    val showRemoveDialog = SingleLiveEvent<String>()

    private val _list = MutableLiveData<List<ListItem>>()
    val list: LiveData<List<ListItem>>
        get() = _list

    fun load() {
        viewModelScope.launch {
            combine(
                weightInteractor.observeMinWeight(),
                weightInteractor.observeMaxPossibleWeight(),
                weightInteractor.observeTargetWeight(),
                weightInteractor.observeWeights()
            ).collect { (min, maxPossible, target, content) ->
                when (content) {
                    is Content.Data -> {
                        _list.postValue(mapper.mapWeights(min, maxPossible, target, content.data))
                    }
                    is Content.Error -> {
                        Log.e(this.javaClass.simpleName, content.exception.toString(), content.exception)
                    }
                }
            }
        }
    }

    fun onClick(listId: String) {
        when (listId) {
            TARGET_ID -> {
                showTargetWeightDialog.call()
            }
            MAX_POSSIBLE_ID -> {
                showMaxPossibleWeightDialog.call()
            }
            else -> {
                openWeightAction.postValue(listId)
            }
        }
    }

    fun onLongClick(listId: String) {
        showRemoveDialog.value = listId
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

    fun changeMaxWeight(maxWeight: String) {
        viewModelScope.launch {
            maxWeight.toFloatOrNull()?.let { weight ->
                weightInteractor.setMaxPossibleWeight(weight)
            } ?: showError.call()
        }
    }

    fun goToChart() {
        goToChartAction.call()
    }

    fun removeWeight(id: String) {
        viewModelScope.launch {
            weightInteractor.removeWeight(id.toLong())
            load()
        }
    }
}
