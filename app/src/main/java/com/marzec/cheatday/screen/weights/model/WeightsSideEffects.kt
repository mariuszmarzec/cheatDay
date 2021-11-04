package com.marzec.cheatday.screen.weights.model

sealed class WeightsSideEffects {
    object GoToAddResultScreen : WeightsSideEffects()
    object ShowTargetWeightDialog : WeightsSideEffects()
    object GoToChartAction : WeightsSideEffects()
    object ShowError : WeightsSideEffects()
    object ShowMaxPossibleWeightDialog : WeightsSideEffects()
    data class OpenWeightAction(val id: String) : WeightsSideEffects()
    data class ShowRemoveDialog(val id: String) : WeightsSideEffects()
}
