package com.marzec.cheatday.screen.weights.model

import android.content.Context
import com.marzec.cheatday.R
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.extensions.plus
import com.marzec.cheatday.view.model.LabeledRowItem
import com.marzec.cheatday.view.model.ListItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeightsMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @OptIn(ExperimentalStdlibApi::class)
    fun mapWeights(
        minWeight: WeightResult?,
        weekAverage: Float?,
        maxPossibleValue: Float,
        targetWeight: Float,
        weights: List<WeightResult>
    ): List<ListItem> = buildList<ListItem> {
        minWeight?.let { add(minWeightItem(it)) }
        weekAverage?.let { add(stubWeekAverageWeightItem(it)) }
        add(maxPossibleWeightItem(maxPossibleValue))
        add(targetWeightItem(targetWeight))
        addAll(weightsItems(weights))
    }

    private fun minWeightItem(minWeight: WeightResult): LabeledRowItem {
        val label = context.getString(R.string.weights_min_label)
        val dateLabel = context.getString(R.string.weight_row_date_label)
        val date = minWeight.date.toString(
            DATE_PATTERN
        )
        return LabeledRowItem(
            id = MIN_ID,
            data = Unit,
            value = "${minWeight.value} ${context.getString(R.string.unit_kg_short)}",
            label = "$label $dateLabel $date"
        )
    }

    private fun stubWeekAverageWeightItem(weekAverage: Float?): LabeledRowItem = LabeledRowItem(
        id = WEEK_AVERAGE_ID,
        data = Unit,
        value = "$weekAverage ${context.getString(R.string.unit_kg_short)}",
        label = context.getString(R.string.weights_week_average_label)
    )

    private fun maxPossibleWeightItem(maxPossibleValue: Float) = LabeledRowItem(
        id = MAX_POSSIBLE_ID,
        data = Unit,
        value = "$maxPossibleValue ${context.getString(R.string.unit_kg_short)}",
        label = context.getString(R.string.weights_max_possible_label)
    )

    private fun targetWeightItem(targetWeight: Float) = LabeledRowItem(
        id = TARGET_ID,
        data = Unit,
        value = "$targetWeight ${context.getString(R.string.unit_kg_short)}",
        label = context.getString(R.string.weights_target_label)
    )

    private fun weightsItems(weights: List<WeightResult>) =
        weights.map { weightResult ->
            with(weightResult) {
                LabeledRowItem(
                    id = this.id.toString(),
                    data = this,
                    value = "$value ${context.getString(R.string.unit_kg_short)}",
                    label = "${context.getString(R.string.weight_row_date_label)} ${
                        date.toString(
                            DATE_PATTERN
                        )
                    }"
                )
            }
        }

    companion object {
        const val MIN_ID = "MIN_ID"
        const val WEEK_AVERAGE_ID = "WEEK_AVERAGE_ID"
        const val TARGET_ID = "TARGET_ID"
        const val MAX_POSSIBLE_ID = "MAX_POSSIBLE_ID"
        private const val DATE_PATTERN = "dd MMM yyyy"
    }
}
