package com.marzec.cheatday.screen.dayscounter.model

import com.marzec.cheatday.interactor.DaysInteractor
import com.marzec.cheatday.model.domain.ClickedStates
import com.marzec.cheatday.model.domain.DaysGroup
import com.marzec.cheatday.repository.LoginRepository
import com.marzec.cheatday.repository.UserPreferencesRepository
import com.marzec.mvi.StoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

@HiltViewModel
class DaysCounterViewModel @Inject constructor(
    private val daysInteractor: DaysInteractor,
    private val preferencesRepository: UserPreferencesRepository,
    private val loginRepository: LoginRepository,
    defaultState: DaysCounterState
) : StoreViewModel<DaysCounterState, Unit>(defaultState) {

    fun loading() = intent<Pair<DaysGroup, ClickedStates>> {
        onTrigger {
            val days = daysInteractor.observeDays()
            val clickedStates = daysInteractor.observeClickedStates()
            days.combine(clickedStates) { daysGroup, states ->
                daysGroup to states
            }
        }
        reducer {
            val (days, clickedState) = resultNonNull()
            state.copy(
                diet = state.diet.copy(
                    day = days.diet,
                    clicked = clickedState.isDietDayClicked
                ),
                cheat = state.cheat.copy(
                    day = days.cheat,
                    clicked = clickedState.isCheatDayClicked
                ),
                workout = state.workout.copy(
                    day = days.workout,
                    clicked = clickedState.isWorkoutDayClicked
                )
            )
        }
    }

    fun onCheatDecreaseClick() = intent<Unit> {
        onTrigger {
            flow {
                val day = state.cheat.day
                preferencesRepository.setWasClickedToday(day.type)
                daysInteractor.updateDay(day.copy(count = day.count.dec()))
                emit(Unit)
            }
        }
    }

    fun onDietIncreaseClick() = intent<Unit> {
        onTrigger {
            flow {
                val day = state.diet.day
                preferencesRepository.setWasClickedToday(day.type)
                daysInteractor.updateDay(day.copy(count = day.count.inc()))
                emit(Unit)
            }
        }
    }

    fun onWorkoutIncreaseClick() = intent<Unit> {
        onTrigger {
            flow {
                val day = state.workout.day
                preferencesRepository.setWasClickedToday(day.type)
                daysInteractor.updateDay(day.copy(count = day.count.inc()))
                emit(Unit)
            }
        }
    }

    fun onLogoutClick() = intent<Unit> {
        onTrigger {
            flow {
                loginRepository.logout()
            }
        }
    }
}
