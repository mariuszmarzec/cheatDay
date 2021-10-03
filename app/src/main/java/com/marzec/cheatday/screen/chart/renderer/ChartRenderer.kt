package com.marzec.cheatday.screen.chart.renderer

import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.marzec.cheatday.R
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.extensions.gone
import com.marzec.cheatday.extensions.visible
import com.marzec.cheatday.screen.chart.model.ChartsData
import com.marzec.cheatday.view.ErrorView
import com.marzec.mvi.State
import javax.inject.Inject

class ChartRenderer @Inject constructor() {

    private lateinit var chart: LineChart
    private lateinit var errorView: ErrorView
    private lateinit var progressBar: View

    var onTryAgainButtonClickListener: () -> Unit = { }

    fun init(view: View) {
        this.chart = view.findViewById(R.id.chart)
        this.errorView = view.findViewById(R.id.error_view)
        this.progressBar = view.findViewById(R.id.progress_bar)
        val color = chart.context.getColor(R.color.colorPrimaryDark)

        progressBar.background = null
        errorView.setOnButtonClickListener { onTryAgainButtonClickListener() }

        chart.xAxis.run {
            setDrawGridLines(false)
            spaceMin = 5f
            position = XAxis.XAxisPosition.BOTTOM
            textColor = color
        }

        chart.axisLeft.textColor = color
        chart.axisRight.textColor = color
    }

    fun render(state: State<ChartsData>) {
        when (state) {
            is State.Data -> {
                chart.visible()
                errorView.gone()
                progressBar.gone()
                render(state.data)
            }
            is State.Error -> {
                chart.gone()
                errorView.visible()
                progressBar.gone()
                errorView.setErrorMessage(state.message)
            }
            is State.Loading -> {
                chart.gone()
                errorView.gone()
                progressBar.visible()
            }
        }
    }

    private fun render(data: ChartsData) {
        val weights = data.weights
        val idToWeight = weights.mapIndexed { index, weight ->
            index to weight.date.toString(Constants.DATE_PICKER_PATTERN)
        }.toMap()

        val values =
            weights.mapIndexed { index, weight -> Entry(index.toFloat(), weight.value) }
        val lineDataSet = LineDataSet(values, "")

        lineDataSet.setColors(chart.context.getColor(R.color.chartDataSet))
        lineDataSet.valueTextColor = chart.context.getColor(R.color.colorPrimaryDark)
        lineDataSet.valueTextSize = 10f
        chart.data = LineData(lineDataSet)
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return idToWeight[value.toInt()].orEmpty()
            }
        }
        chart.invalidate()
    }
}