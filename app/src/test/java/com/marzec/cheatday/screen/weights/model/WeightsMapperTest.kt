package com.marzec.cheatday.screen.weights.model

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.R
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.stubs.stubWeightResult
import com.marzec.cheatday.view.model.LabeledRowItem
import io.mockk.every
import io.mockk.mockk
import java.util.Locale
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class WeightsMapperTest {

    val context = mockk<Context>()
    lateinit var mapper: WeightsMapper

    @BeforeEach
    fun setUp() {
        every { context.getString(any()) } answers {
            arg<Int>(0).toString()
        }
        every { context.getString(any(), any()) } answers {
            val formattedString = arg<Int>(0).toString()
            val argument = arg<String>(1)
            "$formattedString with $argument"
        }

        Locale.setDefault(Locale.UK)

        mapper = WeightsMapper(context)
    }

    @Test
    fun mapWeights() {
        val list = mapper.mapWeights(
            minWeight = stubWeightResult(value = 1f),
            maxPossibleValue = 2f,
            targetWeight = 0f,
            weights = listOf(
                stubWeightResult(id = 1, value = 1f),
                stubWeightResult(id = 2, value = 2f)
            )
        )
        assertThat(list).isEqualTo(
            listOf(
                stubMinWeightItem(),
                stubMaxPossibleWeightItem(),
                stubTargetWeightItem(),
                stubWeightsItem(stubWeightResult(id = 1, value = 1f)),
                stubWeightsItem(stubWeightResult(id = 2, value = 2f))
            )
        )
    }

    @Test
    fun mapWeightsWithoutMinWeight() {
        val list = mapper.mapWeights(
            minWeight = null,
            maxPossibleValue = 2f,
            targetWeight = 0f,
            weights = listOf(
                stubWeightResult(id = 1, value = 1f),
                stubWeightResult(id = 2, value = 2f)
            )
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

    private fun stubMinWeightItem(): LabeledRowItem {
        val label = context.getString(R.string.weights_min_label)
        val dateLabel = context.getString(R.string.weight_row_date_label)
        val date = "01 Jan 1970"
        return LabeledRowItem(
            id = WeightsMapper.MIN_ID,
            data = Unit,
            value = "1.0 ${context.getString(R.string.unit_kg_short)}",
            label = "$label $dateLabel $date"
        )
    }

    private fun stubMaxPossibleWeightItem() = LabeledRowItem(
        id = WeightsMapper.MAX_POSSIBLE_ID,
        data = Unit,
        value = "2.0 ${context.getString(R.string.unit_kg_short)}",
        label = context.getString(R.string.weights_max_possible_label)
    )

    private fun stubTargetWeightItem() = LabeledRowItem(
        id = WeightsMapper.TARGET_ID,
        data = Unit,
        value = "0.0 ${context.getString(R.string.unit_kg_short)}",
        label = context.getString(R.string.weights_target_label)
    )

    private fun stubWeightsItem(result: WeightResult) =
        LabeledRowItem(
            id = result.id.toString(),
            data = result,
            value = "${result.value} ${context.getString(R.string.unit_kg_short)}",
            label = "${context.getString(R.string.weight_row_date_label)} 01 Jan 1970"
        )
}