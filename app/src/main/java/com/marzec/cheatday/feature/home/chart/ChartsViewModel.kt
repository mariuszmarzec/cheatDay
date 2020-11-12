package com.marzec.cheatday.feature.home.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.marzec.cheatday.common.BaseViewModel
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.model.domain.WeightResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ChartsViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    ) : BaseViewModel() {

    private val context = viewModelScope.coroutineContext + Dispatchers.IO

    @InternalCoroutinesApi
    val weights: LiveData<List<WeightResult>> = liveData(context) {
        weightInteractor.observeWeights().collect { emit(it) }
    }
}