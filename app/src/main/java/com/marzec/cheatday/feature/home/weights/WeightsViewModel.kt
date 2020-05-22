package com.marzec.cheatday.feature.home.weights

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.marzec.cheatday.common.BaseViewModel
import com.marzec.cheatday.common.SingleLiveEvent
import com.marzec.cheatday.domain.WeightResult
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.view.model.ListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WeightsViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    private val mapper: WeightsMapper
) : BaseViewModel() {

    private val minAndTargetFlow: Flow<Pair<WeightResult?, Float>>
        get() = weightInteractor.observeMinWeight().combine(weightInteractor.observeTargetWeight()) { min, target ->
            min to target
        }

    val goToAddResultScreen = SingleLiveEvent<Unit>()

    @InternalCoroutinesApi
    val list: LiveData<List<ListItem>> = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
        minAndTargetFlow.combine(weightInteractor.observeWeights()) { (min, target), weights ->
            Triple(min, target, weights)
        }.collect { (min, target, weights) ->
            this@liveData.emit(mapper.mapWeights(min, target, weights))
        }
    }

    fun onClick(listId: String) {

    }

    fun onFloatingButtonClick() {
        goToAddResultScreen.call()
    }
}
