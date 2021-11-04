package com.marzec.cheatday.view.model

data class LabeledRowItem(
    override val id: String,
    override val data: Any,
    val label: String,
    val value: String
) : ListItem
