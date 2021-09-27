package com.marzec.cheatday.screen.chart.model

import com.marzec.cheatday.model.domain.WeightResult

data class ChartsData(
    val weights: List<WeightResult>
) {

    companion object {
        val INITIAL = ChartsData(emptyList())
    }
}
