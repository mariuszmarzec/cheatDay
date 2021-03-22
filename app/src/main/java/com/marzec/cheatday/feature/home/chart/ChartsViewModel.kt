package com.marzec.cheatday.feature.home.chart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.model.domain.WeightResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlinx.coroutines.launch

class ChartsViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    ) : ViewModel() {

    private val _weights = MutableLiveData<List<WeightResult>>()

    val weights: LiveData<List<WeightResult>>
    get() = _weights

    fun load() {
        viewModelScope.launch {
            weightInteractor.observeWeights().collect { content ->
                when (content) {
                    is Content.Data -> {
                        _weights.postValue(content.data)
                    }
                    is Content.Error -> {
                        Log.e(this.javaClass.simpleName, content.exception.toString(), content.exception)
                    }
                }
            }

        }
    }
}