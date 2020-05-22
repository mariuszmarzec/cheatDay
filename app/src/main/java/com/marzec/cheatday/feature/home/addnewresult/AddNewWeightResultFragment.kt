package com.marzec.cheatday.feature.home.addnewresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.databinding.FragmentAddNewWeightResultBinding
import com.marzec.cheatday.databinding.FragmentDaysCounterBinding
import kotlinx.android.synthetic.main.fragment_add_new_weight_result.*
import javax.inject.Inject

class AddNewWeightResultFragment : BaseFragment(R.layout.fragment_add_new_weight_result) {

    @Inject
    lateinit var viewModel: AddNewWeightResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddNewWeightResultBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load(null)

        dateEditText.setOnClickListener {
        }

    }
}

