package com.marzec.cheatday.screen.weights.model

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.R
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.stubs.stubWeightResult
import com.marzec.cheatday.view.delegates.labeledrowitem.LabeledRow
import io.mockk.every
import io.mockk.mockk
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class WeightsMapperTest {

    val context = mockk<Context>()
    val dispatcher = UnconfinedTestDispatcher()
    val scope = TestScope(dispatcher)

    lateinit var mapper: WeightsMapper

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        every { context.getString(any()) } answers {
            arg<Int>(0).toString()
        }
        every { context.getString(any(), any()) } answers {
            val formattedString = arg<Int>(0).toString()
            val argument = arg<String>(1)
            "$formattedString with $argument"
        }

        Locale.setDefault(Locale.UK)

        mapper = WeightsMapper(context, dispatcher)
    }

    @Test
    fun mapWeights() = scope.runTest {
        val list = mapper.mapWeights(
            minWeight = stubWeightResult(value = 1f),
            weekAverage = 3f,
            maxPossibleValue = 2f,
            targetWeight = 0f,
            weights = listOf(
                stubWeightResult(id = 1, value = 1f),
                stubWeightResult(id = 2, value = 2f)
            ),
            onClickListener = { },
            onLongClickListener = { }
        )
        assertThat(list).isEqualTo(
            listOf(
                stubMinWeightItem(),
                stubWeekAverageWeightItem(),
                stubMaxPossibleWeightItem(),
                stubTargetWeightItem(),
                stubWeightsItem(stubWeightResult(id = 1, value = 1f)),
                stubWeightsItem(stubWeightResult(id = 2, value = 2f))
            )
        )
    }

    @Test
    fun roundAverageWeight() = scope.runTest {
        val list = mapper.mapWeights(
            minWeight = null,
            weekAverage = 3.0121f,
            maxPossibleValue = 2f,
            targetWeight = 0f,
            weights = emptyList(),
            onClickListener = { },
            onLongClickListener = { }
        )
        assertThat(list).isEqualTo(
            listOf(
                stubWeekAverageWeightItem(),
                stubMaxPossibleWeightItem(),
                stubTargetWeightItem(),
            )
        )
    }

    @Test
    fun mapWeightsWithoutMinWeightAndAverage() = scope.runTest {
        val list = mapper.mapWeights(
            minWeight = null,
            weekAverage = null,
            maxPossibleValue = 2f,
            targetWeight = 0f,
            weights = listOf(
                stubWeightResult(id = 1, value = 1f),
                stubWeightResult(id = 2, value = 2f)
            ),
            onClickListener = { },
            onLongClickListener = { }
        )
        assertThat(list).isEqualTo(
            listOf(
                stubMaxPossibleWeightItem(),
                stubTargetWeightItem(),
                stubWeightsItem(stubWeightResult(id = 1, value = 1f)),
                stubWeightsItem(stubWeightResult(id = 2, value = 2f))
            )
        )
    }

    private fun stubMinWeightItem(): LabeledRow {
        val label = context.getString(R.string.weights_min_label)
        val dateLabel = context.getString(R.string.weight_row_date_label)
        val date = "01 Jan 1970"
        return LabeledRow(
            id = WeightsMapper.MIN_ID,
            value = "1.0 ${context.getString(R.string.unit_kg_short)}",
            label = "$label $dateLabel $date"
        )
    }

    private fun stubWeekAverageWeightItem(): LabeledRow = LabeledRow(
        id = WeightsMapper.WEEK_AVERAGE_ID,
        value = "3.0 ${context.getString(R.string.unit_kg_short)}",
        label = context.getString(R.string.weights_week_average_label)
    )


    private fun stubMaxPossibleWeightItem() = LabeledRow(
        id = WeightsMapper.MAX_POSSIBLE_ID,
        value = "2.0 ${context.getString(R.string.unit_kg_short)}",
        label = context.getString(R.string.weights_max_possible_label)
    )

    private fun stubTargetWeightItem() = LabeledRow(
        id = WeightsMapper.TARGET_ID,
        value = "0.0 ${context.getString(R.string.unit_kg_short)}",
        label = context.getString(R.string.weights_target_label)
    )

    private fun stubWeightsItem(result: WeightResult) =
        LabeledRow(
            id = result.id.toString(),
            value = "${result.value} ${context.getString(R.string.unit_kg_short)}",
            label = "${context.getString(R.string.weight_row_date_label)} 01 Jan 1970"
        )
}
