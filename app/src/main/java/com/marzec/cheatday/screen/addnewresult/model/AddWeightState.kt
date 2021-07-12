package com.marzec.cheatday.screen.addnewresult.model

import com.marzec.cheatday.extensions.emptyString
import com.marzec.cheatday.model.domain.WeightResult
import org.joda.time.DateTime

data class AddWeightState(
    val weightResult: WeightResult,
    val date: DateTime,
    val weight: String
) {
    companion object {
        val INITIAL = AddWeightState(
            weightResult = WeightResult(0, 0.0f, DateTime.now()),
            date = DateTime.now(),
            weight = emptyString()
        )
    }
}
