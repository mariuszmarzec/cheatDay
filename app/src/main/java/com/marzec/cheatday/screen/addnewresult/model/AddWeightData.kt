package com.marzec.cheatday.screen.addnewresult.model

import com.marzec.cheatday.model.domain.WeightResult
import org.joda.time.DateTime

data class AddWeightData(
    val weightResult: WeightResult,
    val date: DateTime,
    val weight: String
) {
    companion object {
        val INITIAL = AddWeightData(
            weightResult = WeightResult(0, 0.0f, DateTime.now()),
            date = DateTime.now(),
            weight = "0.0"
        )
    }
}
