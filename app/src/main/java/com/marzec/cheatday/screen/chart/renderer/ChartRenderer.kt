package com.marzec.cheatday.screen.chart.renderer

import android.view.View
import android.widget.LinearLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.marzec.cheatday.R
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.extensions.gone
import com.marzec.cheatday.extensions.visible
import com.marzec.cheatday.screen.chart.model.ChartsData
import com.marzec.cheatday.view.delegates.errorscreen.ErrorView
import com.marzec.mvi.State
import javax.inject.Inject

class ChartRenderer @Inject constructor() {

    private lateinit var chart: LineChart
    private lateinit var chartContent: LinearLayout
    private lateinit var optionsChips: ChipGroup
    private lateinit var weightsChartChip: Chip
    private lateinit var averageChartChip: Chip
    private lateinit var errorView: ErrorView
    private lateinit var progressBar: View

    var onTryAgainButtonClickListener: () -> Unit = { }

    var onWeightsChartChipClickListener: () -> Unit = { }

    var onAverageChartChipClickListener: () -> Unit = { }

    fun init(view: View) {
        this.chart = view.findViewById(R.id.chart)
        this.chartContent = view.findViewById(R.id.chart_content)
        this.optionsChips = view.findViewById(R.id.options_chips)
        this.weightsChartChip = view.findViewById(R.id.weights_chart_chip)
        this.averageChartChip = view.findViewById(R.id.average_chart_chip)
        this.errorView = view.findViewById(R.id.error_view)
        this.progressBar = view.findViewById(R.id.progress_bar)
        val color = chart.context.getColor(R.color.colorPrimaryDark)

        weightsChartChip.setOnClickListener {
            onWeightsChartChipClickListener()
        }

        averageChartChip.setOnClickListener {
            onAverageChartChipClickListener()
        }

        progressBar.background = null
        errorView.setOnButtonClickListener { onTryAgainButtonClickListener() }

        chart.xAxis.run {
            setDrawGridLines(false)
            spaceMin = CHART_X_SPACE_MIN
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
                errorView.errorMessage = state.message
            }
            is State.Loading -> {
                chart.gone()
                errorView.gone()
                progressBar.visible()
            }
        }
    }

    @Suppress("NewApi")
    private fun render(data: ChartsData) {
        val weights = if (data.showAverage) {
            data.averageWeights
        } else {
            data.weights
        }.reversed()

        val idToWeight = weights.mapIndexed { index, weight ->
            index to weight.date.toString(Constants.DATE_PICKER_PATTERN)
        }.toMap()

        val values =
            weights.mapIndexed { index, weight -> Entry(index.toFloat(), weight.value) }
        val lineDataSet = LineDataSet(values, "")

        val color = if (data.showAverage) {
            chart.context.getColor(R.color.colorAccent)
        } else {
            chart.context.getColor(R.color.chartDataSet)
        }
        lineDataSet.setColors(color)
        lineDataSet.valueTextColor = chart.context.getColor(R.color.colorPrimaryDark)
        lineDataSet.valueTextSize = TEXT_SIZE
        chart.data = LineData(lineDataSet)
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return idToWeight[value.toInt()].orEmpty()
            }
        }
        chart.invalidate()

        if (data.showAverage) {
            averageChartChip.isChecked = true
        } else {
            weightsChartChip.isChecked = true
        }
    }

    private companion object {
        const val CHART_X_SPACE_MIN = 5f
        const val TEXT_SIZE = 10f
    }
}
