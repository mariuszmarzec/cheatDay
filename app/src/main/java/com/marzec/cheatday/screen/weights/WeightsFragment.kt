package com.marzec.cheatday.screen.weights

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.common.StateObserver
import com.marzec.cheatday.common.StateObserver.Companion.testState
import com.marzec.cheatday.extensions.DialogInputOptions
import com.marzec.cheatday.extensions.DialogOptions
import com.marzec.cheatday.extensions.showAnswerDialog
import com.marzec.cheatday.extensions.showErrorDialog
import com.marzec.cheatday.extensions.showInputDialog
import com.marzec.cheatday.screen.weights.model.WeightsData
import com.marzec.cheatday.screen.weights.model.WeightsSideEffects
import com.marzec.cheatday.screen.weights.model.WeightsViewModel
import com.marzec.mvi.State
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class WeightsFragment : BaseFragment(R.layout.fragment_weights), StateObserver<State<WeightsData>> {

    @Inject
    lateinit var renderer: WeightsRenderer

    private val viewModel: WeightsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.also { view ->
            renderer.onClickListener = { viewModel.onClick(it) }
            renderer.onLongClickListener = { viewModel.onLongClick(it) }
            renderer.onFloatingButtonClick = { viewModel.onFloatingButtonClick() }
            renderer.onTryAgainButtonClickListener = { viewModel.load() }
            renderer.init(view)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        observeState(viewModel.state, renderer::render)

        viewModel.sideEffects.observe { effect ->
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
        savedInstanceState ?: testState ?: viewModel.load()
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

    override fun bindStateObserver(
        stateFlow: Flow<State<WeightsData>>,
        action: (State<WeightsData>) -> Unit
    ) {
        stateFlow.observe(action)
    }
}

