package com.marzec.cheatday.feature.home.dayscounter

import androidx.lifecycle.LiveData
import com.marzec.cheatday.common.BaseViewModel
import com.marzec.cheatday.domain.DaysGroup
import com.marzec.cheatday.extensions.toLiveData
import com.marzec.cheatday.interactor.DaysInteractor
import javax.inject.Inject

class DaysCounterViewModel @Inject constructor(
    private val daysInteractor: DaysInteractor
) : BaseViewModel() {

    val days: LiveData<DaysGroup> by lazy {
        daysInteractor.getDays().toLiveData()
    }

    fun onCheatDecreaseClick() {
        days.value?.cheat?.let { day ->
            daysInteractor.updateDay(day.copy(count = day.count.dec())).subscribeTillClear()
        }
    }

    fun onDietIncreaseClick() {
        days.value?.diet?.let { day ->
            daysInteractor.updateDay(day.copy(count = day.count.inc())).subscribeTillClear()
        }
    }

    fun onWorkoutIncreaseClick() {
        days.value?.workout?.let { day ->
            daysInteractor.updateDay(day.copy(count = day.count.inc())).subscribeTillClear()
        }
    }
}
