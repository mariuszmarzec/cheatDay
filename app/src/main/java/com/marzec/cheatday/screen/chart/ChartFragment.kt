package com.marzec.cheatday.screen.chart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.extensions.showErrorDialog
import com.marzec.cheatday.screen.chart.model.ChartsSideEffect
import com.marzec.cheatday.screen.chart.model.ChartsViewModel
import com.marzec.cheatday.screen.chart.renderer.ChartRenderer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChartFragment : BaseFragment(R.layout.fragment_chart) {

    private val viewModel: ChartsViewModel by viewModels()

    @Inject
    lateinit var chartRenderer: ChartRenderer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chartRenderer.onTryAgainButtonClickListener = {
            viewModel.load()
        }
        chartRenderer.onWeightsChartChipClickListener = {
            viewModel.onWeightChartTypeSelected()
        }
        chartRenderer.onAverageChartChipClickListener = {
            viewModel.onAverageWeightChartTypeSelected()
        }
        chartRenderer.init(view)

        viewModel.state.observeOnResume(chartRenderer::render)

        viewModel.sideEffects.observeOnResume { sideEffect ->
            when (sideEffect) {
                ChartsSideEffect.ShowErrorDialog -> {
                    requireContext().showErrorDialog()
                }
            }
        }

        viewModel.load()
    }
}
