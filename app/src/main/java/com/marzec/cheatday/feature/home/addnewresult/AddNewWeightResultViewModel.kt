package com.marzec.cheatday.feature.home.addnewresult

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.marzec.cheatday.common.BaseViewModel
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.domain.WeightResult
import com.marzec.cheatday.interactor.WeightInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

class AddNewWeightResultViewModel @Inject constructor(
    private val weightInteractor: WeightInteractor
) : BaseViewModel() {

    private val weightResult = MutableLiveData<WeightResult>()

    val weight = MutableLiveData<String>()
    val date = MutableLiveData<String>()

    fun load(id: Long?) {
        if (id != null) {
            viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
                weightInteractor.getWeight(id)?.let {
                    weightResult.postValue(it)
                    weight.postValue(it.value.toString())
                    date.postValue(it.date.toString())
                }
            }
        } else {
            val default = WeightResult(0, 0.0f, DateTime.now())
            weightResult.postValue(default)
            date.postValue(default.date.toString(Constants.DATE_PICKER_PATTERN))
        }
    }

    fun save() {

    }
}
