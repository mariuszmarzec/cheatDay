package com.marzec.cheatday.screen.addnewresult

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.databinding.FragmentAddNewWeightResultBinding
import com.marzec.cheatday.screen.addnewresult.model.AddNewWeightResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.alert
import org.joda.time.DateTime

@AndroidEntryPoint
class AddNewWeightResultFragment :
    BaseFragment(R.layout.fragment_add_new_weight_result) {

    private val viewModel: AddNewWeightResultViewModel by viewModels()
    private val args: AddNewWeightResultFragmentArgs by navArgs()

    private lateinit var dateEditText: EditText
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddNewWeightResultBinding.inflate(inflater, container, false)
        dateEditText = binding.root.findViewById(R.id.weight_edit_text)
        button = binding.root.findViewById(R.id.button)

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
}
