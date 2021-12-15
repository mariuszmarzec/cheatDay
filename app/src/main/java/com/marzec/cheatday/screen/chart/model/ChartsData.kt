package com.marzec.cheatday.screen.chart.model

import com.marzec.cheatday.model.domain.WeightResult

data class ChartsData(
    val weights: List<WeightResult>,
    val averageWeights: List<WeightResult>,
    val showAverage: Boolean = false
) {

    companion object {
        val INITIAL = ChartsData(
            weights = emptyList(),
            averageWeights = emptyList()
        )
    }
}
