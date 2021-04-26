package com.marzec.cheatday.screen.dayscounter

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.databinding.FragmentDaysCounterBinding
import com.marzec.cheatday.screen.dayscounter.model.DaysCounterViewModel
import com.marzec.cheatday.screen.weights.model.DaysSideEffects
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_days_counter.*

@AndroidEntryPoint
class DaysCounterFragment : BaseFragment() {

    private val viewModel: DaysCounterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDaysCounterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        cheatCounter.onDecreaseButtonClickListener = {
            viewModel.onCheatDecreaseClick()
        }
        dietCounter.onIncreaseButtonClickListener = {
            viewModel.onDietIncreaseClick()
        }
        workoutCounter.onIncreaseButtonClickListener = {
            viewModel.onWorkoutIncreaseClick()
        }

        viewModel.sideEffects.observeNonNull { effect ->
            when (effect) {
                DaysSideEffects.GoToLogin -> goToLogin()
            }
        }
    }

    private fun goToLogin() {

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