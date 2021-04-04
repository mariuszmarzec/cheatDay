package com.marzec.cheatday.screen.addnewresult

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseVMFragment
import com.marzec.cheatday.databinding.FragmentAddNewWeightResultBinding
import com.marzec.cheatday.screen.addnewresult.model.AddNewWeightResultViewModel
import kotlinx.android.synthetic.main.fragment_add_new_weight_result.*
import org.jetbrains.anko.alert
import org.joda.time.DateTime

class AddNewWeightResultFragment :
    BaseVMFragment<AddNewWeightResultViewModel>(R.layout.fragment_add_new_weight_result) {

    private val args: AddNewWeightResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddNewWeightResultBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        binding.button.setText(
            args.weightId?.let { R.string.common_update } ?: R.string.common_add
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load(args.weightId)

        viewModel.saveSuccess.observe {
            findNavController().popBackStack()
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
                { _, year, monthOfYear, dayOfMonth ->
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

        button.setOnClickListener {
            viewModel.save()
        }
    }

    override fun viewModelClass(): Class<out AddNewWeightResultViewModel> =
        AddNewWeightResultViewModel::class.java
}

