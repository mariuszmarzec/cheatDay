package com.marzec.cheatday.screen.dayscounter

import android.view.View
import com.marzec.cheatday.R
import com.marzec.cheatday.screen.dayscounter.model.DaysCounterState
import com.marzec.cheatday.view.CounterView
import javax.inject.Inject

class DaysCounterRenderer @Inject constructor() {

    private lateinit var cheatCounter: CounterView
    private lateinit var dietCounter: CounterView
    private lateinit var workoutCounter: CounterView

    var onDecreaseCheatButtonClickListener: () -> Unit = { }
    var onIncreaseDietButtonClickListener: () -> Unit = { }
    var onIncreaseDaysButtonClickListener: () -> Unit = { }

    fun init(view: View) {
        cheatCounter = view.findViewById(R.id.cheat_counter)
        dietCounter = view.findViewById(R.id.diet_counter)
        workoutCounter = view.findViewById(R.id.workout_counter)

        cheatCounter.onDecreaseButtonClickListener = {
            onDecreaseCheatButtonClickListener()
        }
        dietCounter.onIncreaseButtonClickListener = {
            onIncreaseDietButtonClickListener()
        }
        workoutCounter.onIncreaseButtonClickListener = {
            onIncreaseDaysButtonClickListener()
        }
    }

    fun render(state: DaysCounterState) {
        dietCounter.setDay(state.diet.day)
        setClickedState(dietCounter, state.diet.clicked)

        cheatCounter.setDay(state.cheat.day)
        setClickedState(cheatCounter, state.cheat.clicked)

        workoutCounter.setDay(state.workout.day)
        setClickedState(workoutCounter, state.workout.clicked)
    }

    private fun setClickedState(counterView: CounterView, isClicked: Boolean) {
        val context = counterView.context
        counterView.buttonColor = context.getColor(
            if (isClicked) R.color.colorPrimary else R.color.colorAccent
        )
    }
}
