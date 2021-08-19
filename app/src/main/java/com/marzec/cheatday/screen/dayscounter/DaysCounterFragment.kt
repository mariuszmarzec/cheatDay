package com.marzec.cheatday.screen.dayscounter

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.screen.dayscounter.model.DaysCounterViewModel
import com.marzec.cheatday.view.CounterView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DaysCounterFragment : BaseFragment(R.layout.fragment_days_counter) {

    private val viewModel: DaysCounterViewModel by viewModels()

    private lateinit var cheatCounter: CounterView
    private lateinit var dietCounter: CounterView
    private lateinit var workoutCounter: CounterView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        cheatCounter = view.findViewById(R.id.cheat_counter)
        dietCounter = view.findViewById(R.id.diet_counter)
        workoutCounter = view.findViewById(R.id.workout_counter)

        cheatCounter.onDecreaseButtonClickListener = {
            viewModel.onCheatDecreaseClick()
        }
        dietCounter.onIncreaseButtonClickListener = {
            viewModel.onDietIncreaseClick()
        }
        workoutCounter.onIncreaseButtonClickListener = {
            viewModel.onWorkoutIncreaseClick()
        }

        lifecycleScope.launchWhenResumed {
            viewModel.state.collect { state ->
                dietCounter.setDay(state.diet.day)
                setClickedState(dietCounter, state.diet.clicked)

                cheatCounter.setDay(state.cheat.day)
                setClickedState(cheatCounter, state.cheat.clicked)

                workoutCounter.setDay(state.workout.day)
                setClickedState(workoutCounter, state.workout.clicked)
            }
        }
        viewModel.loading()
    }

    private fun setClickedState(counterView: CounterView, isClicked: Boolean) {
        val context = counterView.context
        counterView.setButtonColor(context.getColor(
            if (isClicked) R.color.colorPrimary else R.color.colorAccent)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.days, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> viewModel.onLogoutClick().run { true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}