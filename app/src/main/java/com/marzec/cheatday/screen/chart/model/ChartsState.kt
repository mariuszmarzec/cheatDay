package com.marzec.cheatday.screen.chart.model

import com.marzec.cheatday.model.domain.WeightResult

data class ChartsState(
    val weights: List<WeightResult>
) {

    companion object {
        val INITIAL = ChartsState(emptyList())
    }
}
