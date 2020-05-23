package com.marzec.cheatday.feature.home.addnewresult

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.databinding.FragmentAddNewWeightResultBinding
import com.marzec.cheatday.feature.home.weights.WeightsFragment
import kotlinx.android.synthetic.main.fragment_add_new_weight_result.*
import org.jetbrains.anko.alert
import org.joda.time.DateTime
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

        viewModel.saveSuccess.observe {
            replaceFragment<WeightsFragment>()
        }

        viewModel.error.observe {
            activity?.alert {
                titleResource = R.string.dialog_error_title_common
                messageResource = R.string.dialog_error_message_try_later
                isCancelable = true
                show()
            }
        }

        viewModel.showDatePickerEvent.observeNonNull { date ->
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    viewModel.setDate(DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0))
                },
                date.year,
                date.monthOfYear - 1,
                date.dayOfMonth
            )

            datePickerDialog.show()
        }

        dateEditText.setOnClickListener {
            viewModel.onDatePickerClick()
        }

        button.setOnClickListener { viewModel.save() }
    }
}

