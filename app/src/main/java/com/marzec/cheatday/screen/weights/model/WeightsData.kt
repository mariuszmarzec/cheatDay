package com.marzec.cheatday.screen.weights.model

import com.marzec.cheatday.model.domain.WeightResult

data class WeightsData(
    val minWeight: WeightResult?,
    val maxPossibleWeight: Float,
    val targetWeight: Float,
    val weights: List<WeightResult>
)
