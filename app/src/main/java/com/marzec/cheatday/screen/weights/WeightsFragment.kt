package com.marzec.cheatday.screen.weights

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.extensions.DialogInputOptions
import com.marzec.cheatday.extensions.DialogOptions
import com.marzec.cheatday.extensions.showAnswerDialog
import com.marzec.cheatday.extensions.showErrorDialog
import com.marzec.cheatday.extensions.showInputDialog
import com.marzec.cheatday.screen.weights.model.WeightsSideEffects
import com.marzec.cheatday.screen.weights.model.WeightsViewModel
import com.marzec.cheatday.view.labeledRowView
import com.marzec.cheatday.view.model.LabeledRowItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class WeightsFragment : BaseFragment(R.layout.fragment_weights) {

    private val viewModel: WeightsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val recyclerView = view.findViewById<EpoxyRecyclerView>(R.id.recycler_view)
        val floatingButton = view.findViewById<FloatingActionButton>(R.id.floating_button)

        lifecycleScope.launchWhenResumed {

            viewModel.state.collect { state ->
                recyclerView.withModels {
                    state.list.forEach { item ->
                        when (item) {
                            is LabeledRowItem -> {
                                labeledRowView {
                                    id(item.id)
                                    label(item.label)
                                    value(item.value)
                                    onClickListener { _, _, _, _ ->
                                        viewModel.onClick(item.id)
                                    }
                                    onLongClickListener { _, _, _, _ ->
                                        viewModel.onLongClick(item.id)
                                        true
                                    }
                                }
                            }
                        }
                    }
                }
            }

            viewModel.sideEffects.collect { effect ->
                when (effect) {
                    WeightsSideEffects.GoToAddResultScreen -> {
                        findNavController().navigate(
                            WeightsFragmentDirections.actionWeightsToAddWeight(
                                null
                            )
                        )
                    }
                    WeightsSideEffects.GoToChartAction -> {
                        findNavController().navigate(R.id.action_weights_to_chart)
                    }
                    is WeightsSideEffects.OpenWeightAction -> {
                        findNavController().navigate(
                            WeightsFragmentDirections.actionWeightsToAddWeight(effect.id)
                        )
                    }
                    WeightsSideEffects.ShowError -> {
                        requireActivity().showErrorDialog()
                    }
                    WeightsSideEffects.ShowMaxPossibleWeightDialog -> {
                        showMaxPossibleWeightDialog()
                    }
                    is WeightsSideEffects.ShowRemoveDialog -> {
                        showIfRemoveDialog(effect.id)
                    }
                    WeightsSideEffects.ShowTargetWeightDialog -> {
                        showTargetWeightDialog()
                    }
                }
            }
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
            DialogInputOptions(
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
            DialogInputOptions(
                title = getString(R.string.weights_max_possible_label),
                message = getString(R.string.dialog_change_max_weight_message),
                negativeButton = getString(R.string.cancel),
                positiveButton = getString(R.string.ok),
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                onInputText = { newTargetWeight -> viewModel.changeMaxWeight(newTargetWeight) }
            )
        )
    }

    private fun showIfRemoveDialog(id: String) {
        requireActivity().showAnswerDialog(
            DialogOptions(
                title = getString(R.string.remove_weight_dialog_title),
                message = getString(R.string.remove_weight_dialog_message),
                negativeButton = getString(R.string.no),
                positiveButton = getString(R.string.yes),
                onPositiveButtonClicked = { viewModel.removeWeight(id) }
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
}