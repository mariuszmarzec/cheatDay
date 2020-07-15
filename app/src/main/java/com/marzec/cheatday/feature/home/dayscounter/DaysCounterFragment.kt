package com.marzec.cheatday.feature.home.dayscounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marzec.cheatday.common.BaseVMFragment
import com.marzec.cheatday.databinding.FragmentDaysCounterBinding
import kotlinx.android.synthetic.main.fragment_days_counter.*

class DaysCounterFragment : BaseVMFragment<DaysCounterViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDaysCounterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cheatCounter.onDecreaseButtonClickListener = {
            viewModel.onCheatDecreaseClick()
        }
        dietCounter.onIncreaseButtonClickListener = {
            viewModel.onDietIncreaseClick()
        }
        workoutCounter.onIncreaseButtonClickListener = {
            viewModel.onWorkoutIncreaseClick()
        }
    }

    override fun viewModelClass(): Class<out DaysCounterViewModel> {
        return DaysCounterViewModel::class.java
    }
}