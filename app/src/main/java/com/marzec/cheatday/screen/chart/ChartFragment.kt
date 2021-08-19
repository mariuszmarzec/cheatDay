package com.marzec.cheatday.screen.chart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.extensions.showErrorDialog
import com.marzec.cheatday.screen.chart.model.ChartsSideEffect
import com.marzec.cheatday.screen.chart.model.ChartsViewModel
import com.marzec.cheatday.screen.chart.renderer.ChartRenderer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ChartFragment : BaseFragment(R.layout.fragment_chart) {

    private val viewModel: ChartsViewModel by viewModels()

    @Inject
    lateinit var chartRenderer: ChartRenderer

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chartRenderer.init(view.findViewById(R.id.chart))

        lifecycleScope.launchWhenResumed {
            viewModel.state.collect { state ->
                val weights = state.weights
                chartRenderer.render(weights)
            }

            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    ChartsSideEffect.ShowErrorDialog -> {
                        requireContext().showErrorDialog()
                    }
                }
            }
        }

        viewModel.load()
    }
}
