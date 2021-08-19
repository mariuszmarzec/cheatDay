package com.marzec.cheatday.screen.addnewresult

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.screen.addnewresult.model.AddNewWeightResultViewModel
import com.marzec.cheatday.screen.addnewresult.model.AddWeightSideEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.jetbrains.anko.alert
import org.joda.time.DateTime

@AndroidEntryPoint
class AddNewWeightResultFragment :
    BaseFragment(R.layout.fragment_add_new_weight_result) {

    private val viewModel: AddNewWeightResultViewModel by viewModels()
    private val args: AddNewWeightResultFragmentArgs by navArgs()

    private lateinit var weightEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var button: Button


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weightEditText = view.findViewById(R.id.weight_edit_text)
        dateEditText = view.findViewById(R.id.date_edit_text)
        button = view.findViewById(R.id.button)

        dateEditText.setOnClickListener {
            viewModel.onDatePickerClick()
        }

        button.setText(
            args.weightId?.let { R.string.common_update } ?: R.string.common_add
        )

        viewModel.load(args.weightId)

        lifecycleScope.launchWhenResumed {
            viewModel.state.collect { state ->
                val newText = state.weight
                val newDate = state.date.toString(Constants.DATE_PICKER_PATTERN)
                if (weightEditText.text.toString() != newText) {
                    weightEditText.setText(newText)
                }
                if (dateEditText.text.toString() != newDate) {
                    dateEditText.setText(newDate)
                }
            }

            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    AddWeightSideEffect.SaveSuccess -> findNavController().popBackStack()
                    is AddWeightSideEffect.ShowDatePicker -> showPicker(sideEffect.date)
                    AddWeightSideEffect.ShowError -> activity?.alert {
                        titleResource = R.string.dialog_error_title_common
                        messageResource = R.string.dialog_error_message_try_later
                        isCancelable = true
                        show()
                    }

                }
            }
        }

        weightEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setNewWeight(text.toString())
        }

        button.setOnClickListener {
            viewModel.save()
        }
    }

    private fun showPicker(date: DateTime) {
        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                viewModel.setDate(DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0))
            },
            date.year,
            date.monthOfYear - 1,
            date.dayOfMonth
        ).apply { show() }
    }
}
