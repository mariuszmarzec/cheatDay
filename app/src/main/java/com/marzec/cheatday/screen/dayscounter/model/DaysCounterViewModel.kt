package com.marzec.cheatday.screen.dayscounter.model

import androidx.lifecycle.*
import com.marzec.cheatday.OpenForTesting
import com.marzec.cheatday.extensions.map
import com.marzec.cheatday.screen.weights.model.DaysSideEffects
import com.marzec.cheatday.interactor.DaysInteractor
import com.marzec.cheatday.model.domain.ClickedStates
import com.marzec.cheatday.model.domain.DaysGroup
import com.marzec.cheatday.model.ui.DayState
import com.marzec.cheatday.repository.LoginRepository
import com.marzec.cheatday.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OpenForTesting
class DaysCounterViewModel @Inject constructor(
    private val daysInteractor: DaysInteractor,
    private val preferencesRepository: UserPreferencesRepository,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val sideEffectsInternal = MutableLiveData<DaysSideEffects>()

    val sideEffects: LiveData<DaysSideEffects>
        get() = sideEffectsInternal

    val days: LiveData<Pair<DaysGroup, ClickedStates>> by lazy {
        val days = daysInteractor.observeDays()
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
                daysInteractor.updateDay(day.copy(count = day.count.dec()))
            }
        }
    }

    fun onDietIncreaseClick() {
        days.value?.first?.diet?.let { day ->
            viewModelScope.launch {
                preferencesRepository.setWasClickedToday(day.type)
                daysInteractor.updateDay(day.copy(count = day.count.inc()))
            }
        }
    }

    fun onWorkoutIncreaseClick() {
        days.value?.first?.workout?.let { day ->
            viewModelScope.launch {
                preferencesRepository.setWasClickedToday(day.type)
                daysInteractor.updateDay(day.copy(count = day.count.inc()))
            }
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            loginRepository.logout()
        }
    }
}