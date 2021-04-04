package com.marzec.cheatday.screen.weights.model

import android.content.Context
import com.marzec.cheatday.R
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.extensions.plus
import com.marzec.cheatday.view.model.LabeledRowItem
import com.marzec.cheatday.view.model.ListItem
import javax.inject.Inject

interface WeightsMapper {
    fun mapWeights(
        minWeight: WeightResult?,
        maxPossibleValue: Float,
        targetWeight: Float,
        weights: List<WeightResult>
    ): List<ListItem>

    companion object {
        const val MIN_ID = "MIN_ID"
        const val TARGET_ID = "TARGET_ID"
        const val MAX_POSSIBLE_ID = "MAX_POSSIBLE_ID"
    }
}

class WeightsMapperImpl @Inject constructor(
    private val context: Context
) : WeightsMapper {
    override fun mapWeights(
        minWeight: WeightResult?,
        maxPossibleValue: Float,
        targetWeight: Float,
        weights: List<WeightResult>
    ): List<ListItem> {
        val minWeightItem = minWeight?.let {
            LabeledRowItem(
                id = WeightsMapper.MIN_ID,
                data = this,
                value = "${it.value} ${context.getString(R.string.unit_kg_short)}",
                label = "${context.getString(R.string.weights_min_label)} ${context.getString(R.string.weight_row_date_label)} ${it.date.toString(
                    "dd MMM yyyy"
                )}"
            )
        }
        val maxPossibleWeightItem = LabeledRowItem(
            id = WeightsMapper.MAX_POSSIBLE_ID,
            data = this,
            value = "$maxPossibleValue ${context.getString(R.string.unit_kg_short)}",
            label = context.getString(R.string.weights_max_possible_label)
        )
        val targetWeightItem = LabeledRowItem(
            id = WeightsMapper.TARGET_ID,
            data = this,
            value = "$targetWeight ${context.getString(R.string.unit_kg_short)}",
            label = context.getString(R.string.weights_target_label)
        )
        val weightsList = weights.map { weightResult ->
            with(weightResult) {
                LabeledRowItem(
                    id = this.id.toString(),
                    data = this,
                    value = "$value ${context.getString(R.string.unit_kg_short)}",
                    label = "${context.getString(R.string.weight_row_date_label)} ${date.toString("dd MMM yyyy")}"
                )
            }
        }
        return if (minWeightItem != null) {
            minWeightItem + maxPossibleWeightItem + targetWeightItem + weightsList
        } else {
            maxPossibleWeightItem + targetWeightItem + weightsList
        }
    }
}