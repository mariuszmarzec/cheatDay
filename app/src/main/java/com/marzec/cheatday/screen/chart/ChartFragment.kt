package com.marzec.cheatday.screen.chart

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.marzec.cheatday.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate.rgb
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.extensions.showErrorDialog
import com.marzec.cheatday.screen.chart.model.ChartsSideEffect
import com.marzec.cheatday.screen.chart.model.ChartsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class ChartFragment : BaseFragment(R.layout.fragment_chart) {

    private val viewModel: ChartsViewModel by viewModels()

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chart = view.findViewById<LineChart>(R.id.chart)

        val color = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        chart.xAxis.run {
            setDrawGridLines(false)
            spaceMin = 5f
            position = XAxis.XAxisPosition.BOTTOM
            textColor = color
        }

        chart.axisLeft.textColor = color
        chart.axisRight.textColor = color

        viewModel.state.observe { state ->
            val weights = state.weights
            val idToWeight = weights.mapIndexed { index, weight ->
                index to weight.date.toString(Constants.DATE_PICKER_PATTERN)
            }.toMap()

            val values =
                weights.mapIndexed { index, weight -> Entry(index.toFloat(), weight.value) }
            val lineDataSet = LineDataSet(values, "")

            lineDataSet.setColors(rgb("#2ecc71"))
            lineDataSet.valueTextColor = color
            lineDataSet.valueTextSize = 10f
            chart.data = LineData(lineDataSet)
            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return idToWeight[value.toInt()].orEmpty()
                }
            }
            chart.invalidate()
        }

        viewModel.sideEffects.observeNonNull { sideEffect ->
            when (sideEffect) {
                ChartsSideEffect.ShowErrorDialog -> {
                    requireContext().showErrorDialog()
                }
            }
        }

        viewModel.load()
    }
}
