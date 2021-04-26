package com.marzec.cheatday.screen.addnewresult.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.common.SingleLiveEvent
import com.marzec.cheatday.extensions.emptyString
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.model.domain.WeightResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime

@HiltViewModel
class AddNewWeightResultViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val weightResult = MutableLiveData<WeightResult>()
    private val dateInternal = MutableLiveData<DateTime>()
    private val loadingInternal = MutableLiveData<Boolean>()

    val weight = savedStateHandle.getLiveData(STATE_WEIGHT, emptyString())

    val date: LiveData<String> = dateInternal.map { it.toString(Constants.DATE_PICKER_PATTERN) }

    val showDatePickerEvent = SingleLiveEvent<DateTime>()

    val error = SingleLiveEvent<Unit>()

    val saveSuccess = SingleLiveEvent<Unit>()

    val loading: LiveData<Boolean>
        get() = loadingInternal

    fun load(id: String?) {
        if (id != null) {
            val weightId = id.toLong()
            viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
                weightInteractor.getWeight(weightId)?.let {
                    weightResult.postValue(it)
                    weight.postValue(it.value.toString())
                    dateInternal.postValue(it.date)
                }
            }
        } else {
            val default = WeightResult(0, 0.0f, DateTime.now())
            weightResult.postValue(default)
            dateInternal.postValue(default.date)
        }
    }

    fun setDate(date: DateTime) {
        dateInternal.value = date
    }

    fun onDatePickerClick() {
        dateInternal.value?.let { date ->
            showDatePickerEvent.value = date
        }
    }

    fun save() {
        val date = dateInternal.value
        val weight = weight.value?.toFloatOrNull()
        val currentWeight = this.weightResult.value
        if (weight != null && weight > 0 && date != null && currentWeight != null) {
            val weightResult = currentWeight.copy(date = date, value = weight)
            viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
                loadingInternal.postValue(true)
                if (weightResult.id != 0L) {
                    weightInteractor.updateWeight(weightResult)
                    loadingInternal.postValue(false)
                    saveSuccess.postValue(Unit)
                } else {
                    putNewWeight(weightResult)
                }
            }
        } else {
            error.call()
        }
    }

    private suspend fun putNewWeight(weightResult: WeightResult) {
        when (weightInteractor.addWeight(weightResult)) {
            is Content.Data -> {
                loadingInternal.postValue(false)
                saveSuccess.postValue(Unit)
            }
            is Content.Error -> {
                error.call()
            }
        }
    }

    companion object {
        private const val STATE_WEIGHT = "STATE_WEIGHT"
    }
}
