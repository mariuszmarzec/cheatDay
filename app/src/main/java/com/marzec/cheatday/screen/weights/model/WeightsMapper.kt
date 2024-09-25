package com.marzec.cheatday.screen.weights.model

import android.content.Context
import com.marzec.adapterdelegate.model.ListItem
import com.marzec.cheatday.R
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.view.delegates.labeledrowitem.LabeledRow
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WeightsMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatcher: CoroutineDispatcher
) {
    var onClickListener: (String) -> Unit = { }
    var onLongClickListener: (String) -> Unit = { }

    suspend fun mapWeights(
        minWeight: WeightResult?,
        weekAverage: Float?,
        maxPossibleValue: Float,
        targetWeight: Float,
        weights: List<WeightResult>
    ): List<ListItem> = withContext(dispatcher) {
        buildList {
            minWeight?.let { add(minWeightItem(it)) }
            weekAverage?.let { add(stubWeekAverageWeightItem(it)) }
            add(maxPossibleWeightItem(maxPossibleValue))
            add(targetWeightItem(targetWeight))
            addAll(weightsItems(weights))
        }
    }

    private fun minWeightItem(minWeight: WeightResult): LabeledRow {
        val label = context.getString(R.string.weights_min_label)
        val dateLabel = context.getString(R.string.weight_row_date_label)
        val date = minWeight.date.toString(
            DATE_PATTERN
        )
        return LabeledRow(
            id = MIN_ID,
            value = "${minWeight.value} ${context.getString(R.string.unit_kg_short)}",
            label = "$label $dateLabel $date"
        )
    }

    private fun stubWeekAverageWeightItem(weekAverage: Float?): LabeledRow {
        val formattedAverage = "%.1f".format(Locale.ENGLISH, weekAverage)
        return LabeledRow(
            id = WEEK_AVERAGE_ID,
            value = "$formattedAverage ${context.getString(R.string.unit_kg_short)}",
            label = context.getString(R.string.weights_week_average_label)
        )
    }

    private fun maxPossibleWeightItem(maxPossibleValue: Float) =
        LabeledRow(
            id = MAX_POSSIBLE_ID,
            value = "$maxPossibleValue ${context.getString(R.string.unit_kg_short)}",
            label = context.getString(R.string.weights_max_possible_label)
        ).apply {
            this.onClickListener = { this@WeightsMapper.onClickListener(id) }
        }

    private fun targetWeightItem(targetWeight: Float) =
        LabeledRow(
            id = TARGET_ID,
            value = "$targetWeight ${context.getString(R.string.unit_kg_short)}",
            label = context.getString(R.string.weights_target_label)
        ).apply {
            this.onClickListener = { this@WeightsMapper.onClickListener(id) }
        }

    private fun weightsItems(weights: List<WeightResult>) =
        weights.map { weightResult ->
            with(weightResult) {
                LabeledRow(
                    id = this.id.toString(),
                    value = "$value ${context.getString(R.string.unit_kg_short)}",
                    label = "${context.getString(R.string.weight_row_date_label)} ${
                        date.toString(
                            DATE_PATTERN
                        )
                    }"
                ).apply {
                    this.onClickListener = { this@WeightsMapper.onClickListener(id) }
                    this.onLongClickListener = { this@WeightsMapper.onLongClickListener(id) }
                }
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
