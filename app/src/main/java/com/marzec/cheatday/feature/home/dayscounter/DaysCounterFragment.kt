package com.marzec.cheatday.feature.home.dayscounter

import android.os.Bundle
import android.view.View
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_days_counter.*
import javax.inject.Inject

class DaysCounterFragment : BaseFragment(R.layout.fragment_days_counter) {

    @Inject
    lateinit var viewModel: DaysCounterViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.days.observeNonNull { days ->
            with(days) {
                dietCounter.setDay(diet)
                workoutCounter.setDay(workout)
                cheatCounter.setDay(cheat)
            }
        }

        cheatCounter.onDecreaseButtonClickListener = { _ ->
            viewModel.onCheatDecreaseClick()
        }
        dietCounter.onIncreaseButtonClickListener = { _, _ ->
            viewModel.onDietIncreaseClick()
        }
        workoutCounter.onIncreaseButtonClickListener = { _, _ ->
            viewModel.onWorkoutIncreaseClick()
        }
    }
}