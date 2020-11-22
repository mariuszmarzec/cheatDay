package com.marzec.cheatday.feature.home.dayscounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.databinding.FragmentDaysCounterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_days_counter.*

@AndroidEntryPoint
class DaysCounterFragment : BaseFragment() {

    private val viewModel: DaysCounterViewModel by viewModels()

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
}