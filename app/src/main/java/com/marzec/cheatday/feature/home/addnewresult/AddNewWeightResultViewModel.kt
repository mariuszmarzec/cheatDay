package com.marzec.cheatday.feature.home.addnewresult

import androidx.lifecycle.*
import com.marzec.cheatday.common.BaseViewModel
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.common.SingleLiveEvent
import com.marzec.cheatday.domain.WeightResult
import com.marzec.cheatday.extensions.emptyString
import com.marzec.cheatday.interactor.WeightInteractor
import com.marzec.cheatday.viewmodel.AssistedSavedStateViewModelFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

class AddNewWeightResultViewModel @AssistedInject constructor(
    private val weightInteractor: WeightInteractor,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val weightResult = MutableLiveData<WeightResult>()
    private val dateInternal = MutableLiveData<DateTime>()
    private val loadingInternal = MutableLiveData<Boolean>()

    val weight = savedStateHandle.getLiveData<String>(STATE_WEIGHT, emptyString())

    val date: LiveData<String> = dateInternal.map { it.toString(Constants.DATE_PICKER_PATTERN) }

    val showDatePickerEvent = SingleLiveEvent<DateTime>()

    val error = SingleLiveEvent<Unit>()

    val saveSuccess = SingleLiveEvent<Unit>()

    val loading: LiveData<Boolean>
        get() = loadingInternal

    fun load(id: Long?) {
        if (id != null) {
            viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
                weightInteractor.getWeight(id)?.let {
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
                } else {
                    weightInteractor.addWeight(weightResult)
                }
                loadingInternal.postValue(false)
                saveSuccess.postValue(Unit)
            }
        } else {
            error.call()
        }
    }

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<AddNewWeightResultViewModel>

    companion object {
        private const val STATE_WEIGHT = "STATE_WEIGHT"
    }
}
