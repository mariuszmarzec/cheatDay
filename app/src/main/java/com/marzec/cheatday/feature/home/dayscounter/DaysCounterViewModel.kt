package com.marzec.cheatday.feature.home.dayscounter

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.marzec.cheatday.common.BaseViewModel
import com.marzec.cheatday.model.domain.ClickedStates
import com.marzec.cheatday.model.domain.DaysGroup
import com.marzec.cheatday.extensions.map
import com.marzec.cheatday.interactor.DaysInteractor
import com.marzec.cheatday.model.ui.DayState
import com.marzec.cheatday.repository.UserPreferencesRepository
import io.reactivex.BackpressureStrategy
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

open class DaysCounterViewModel @ViewModelInject constructor(
    private val daysInteractor: DaysInteractor,
    private val preferencesRepository: UserPreferencesRepository
) : BaseViewModel() {

    val days: LiveData<Pair<DaysGroup, ClickedStates>> by lazy {
        val days = daysInteractor.getDays().toFlowable(BackpressureStrategy.BUFFER).asFlow()
        val clickedStates = daysInteractor.observeClickedStates()
        days.combine(clickedStates) { daysGroup, states ->
            daysGroup to states
        }.asLiveData(viewModelScope.coroutineContext)
    }

    val cheatDays = days.map { (days, states) ->
        DayState(days.cheat, states.isCheatDayClicked)
    }

    val workoutDays =
        days.map { (days, states) ->
            DayState(days.workout, states.isWorkoutDayClicked)
        }

    val dietDays = days.map { (days, states) ->
        DayState(days.diet, states.isDietDayClicked)
    }

    fun onCheatDecreaseClick() {
        days.value?.first?.cheat?.let { day ->
            viewModelScope.launch {
                preferencesRepository.setWasClickedToday(day.type)
                daysInteractor.updateDay(day.copy(count = day.count.dec())).subscribeTillClear()
            }
        }
    }

    fun onDietIncreaseClick() {
        days.value?.first?.diet?.let { day ->
            viewModelScope.launch {
                preferencesRepository.setWasClickedToday(day.type)
                daysInteractor.updateDay(day.copy(count = day.count.inc())).subscribeTillClear()
            }
        }
    }

    fun onWorkoutIncreaseClick() {
        days.value?.first?.workout?.let { day ->
            viewModelScope.launch {
                preferencesRepository.setWasClickedToday(day.type)
                daysInteractor.updateDay(day.copy(count = day.count.inc())).subscribeTillClear()
            }
        }
    }
}
