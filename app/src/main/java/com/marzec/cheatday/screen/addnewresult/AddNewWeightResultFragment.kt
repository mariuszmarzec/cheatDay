package com.marzec.cheatday.screen.addnewresult

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.screen.addnewresult.model.AddNewWeightResultViewModel
import com.marzec.cheatday.screen.addnewresult.model.AddWeightSideEffect
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.jetbrains.anko.alert
import org.joda.time.DateTime

@AndroidEntryPoint
class AddNewWeightResultFragment : BaseFragment(R.layout.fragment_add_new_weight_result) {

    @Inject
    lateinit var renderer: AddNewWeightResultRenderer

    private val viewModel: AddNewWeightResultViewModel by viewModels()
    private val args: AddNewWeightResultFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderer.onDatePickerClick = { viewModel.onDatePickerClick() }
        renderer.onNewWeightChanged = { viewModel.setNewWeight(it) }
        renderer.onButtonClick = { viewModel.save() }
        renderer.init(args.weightId, view)

        viewModel.load(args.weightId)

        viewModel.state.observeOnResume(renderer::render)

        viewModel.sideEffects.observeOnResume { sideEffect ->
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

