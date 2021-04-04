 package com.marzec.cheatday.screen.weights

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseVMFragment
import com.marzec.cheatday.extensions.DialogOptions
import com.marzec.cheatday.extensions.showErrorDialog
import com.marzec.cheatday.extensions.showInputDialog
import com.marzec.cheatday.screen.weights.model.WeightsViewModel
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
            findNavController().navigate(WeightsFragmentDirections.actionWeightsToAddWeight(null))
        }

        viewModel.showTargetWeightDialog.observe {
            showTargetWeightDialog()
        }

        viewModel.showMaxPossibleWeightDialog.observe {
            showMaxPossibleWeightDialog()
        }

        viewModel.showError.observe {
            requireActivity().showErrorDialog()
        }

        viewModel.openWeightAction.observe {
            findNavController().navigate(
                WeightsFragmentDirections.actionWeightsToAddWeight(it)
            )
        }

        viewModel.goToChartAction.observe {
            findNavController().navigate(R.id.action_weights_to_chart)
        }

        floatingButton.setOnClickListener {
            viewModel.onFloatingButtonClick()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.load()
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

    private fun showMaxPossibleWeightDialog() {
        requireActivity().showInputDialog(
            DialogOptions(
                title = getString(R.string.weights_max_possible_label),
                message = getString(R.string.dialog_change_max_weight_message),
                negativeButton = getString(R.string.cancel),
                positiveButton = getString(R.string.ok),
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                onInputText = { newTargetWeight -> viewModel.changeMaxWeight(newTargetWeight) }
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