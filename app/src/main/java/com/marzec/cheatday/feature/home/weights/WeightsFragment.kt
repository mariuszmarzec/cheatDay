package com.marzec.cheatday.feature.home.weights

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseVMFragment
import com.marzec.cheatday.extensions.DialogOptions
import com.marzec.cheatday.extensions.showErrorDialog
import com.marzec.cheatday.extensions.showInputDialog
import com.marzec.cheatday.feature.home.addnewresult.AddNewWeightResultFragment
import com.marzec.cheatday.view.labeledRowView
import com.marzec.cheatday.view.model.LabeledRowItem
import kotlinx.android.synthetic.main.fragment_weights.*
import kotlinx.coroutines.InternalCoroutinesApi


class WeightsFragment : BaseVMFragment<WeightsViewModel>(R.layout.fragment_weights) {

    @InternalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.list.observeNonNull { items ->
            recyclerView.withModels {
                items.forEach { item ->
                    when (item) {
                        is LabeledRowItem -> {
                            labeledRowView {
                                id(item.id)
                                label(item.label)
                                value(item.value)
                                onClickListener { _, _, _, _ ->
                                    viewModel.onClick(item.id)
                                }
                            }
                        }
                    }
                }
            }
        }

        viewModel.goToAddResultScreen.observe {
            findNavController().navigate(WeightsFragmentDirections.actionWeightsToAddWeight())
        }

        viewModel.showTargetWeightDialog.observe {
            showTargetWeightDialog()
        }

        viewModel.showError.observe {
            requireActivity().showErrorDialog()
        }

        viewModel.goToChartAction.observe {
            findNavController().navigate(R.id.action_weights_to_chart)
        }

        floatingButton.setOnClickListener {
            viewModel.onFloatingButtonClick()
        }
    }

    private fun showTargetWeightDialog() {
        requireActivity().showInputDialog(
            DialogOptions(
                title = getString(R.string.weights_target_label),
                message = getString(R.string.dialog_change_target_weight_message),
                negativeButton = getString(R.string.cancel),
                positiveButton = getString(R.string.ok),
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                onInputText = { newTargetWeight -> viewModel.changeTargetWeight(newTargetWeight) }
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weights, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.chart -> viewModel.goToChart().run { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun viewModelClass() = WeightsViewModel::class.java
}