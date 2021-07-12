package com.marzec.cheatday.screen.addnewresult.model

import org.joda.time.DateTime

sealed class AddWeightSideEffect {
    data class ShowDatePicker(val date: DateTime) : AddWeightSideEffect()
    object ShowError : AddWeightSideEffect()
    object SaveSuccess : AddWeightSideEffect()
}
