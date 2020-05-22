package com.marzec.cheatday.feature.home.addnewresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.marzec.cheatday.common.BaseViewModel
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.common.SingleLiveEvent
import com.marzec.cheatday.domain.WeightResult
import com.marzec.cheatday.interactor.WeightInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

class AddNewWeightResultViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor
) : BaseViewModel() {

    private val weightResult = MutableLiveData<WeightResult>()
    private val dateInternal = MutableLiveData<DateTime>()
    private val loadingInternal = MutableLiveData<Boolean>()

    val weight = MutableLiveData<String>()

    val date: LiveData<String> = dateInternal.map { it.toString(Constants.DATE_PICKER_PATTERN) }

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
}
