package com.marzec.cheatday.screen.weights.model

import com.marzec.cheatday.view.model.ListItem

data class WeightsViewState(val list: List<ListItem>) {
    companion object {
        val INITIAL = WeightsViewState(emptyList())
    }
}