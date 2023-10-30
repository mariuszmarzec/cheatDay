package com.marzec.cheatday.view.delegates.errorscreen

import com.marzec.adapterdelegate.model.ListItem
import com.marzec.cheatday.view.delegates.errorscreen.ErrorScreenDelegate.Companion.VIEW_TYPE

data class ErrorScreen(
    override val id: String,
    val message: String,
    val buttonLabel: String
) : ListItem {
    override val viewType: Int = VIEW_TYPE

    var onButtonClickListener: (() -> Unit)? = null

    data class Payload(
        val message: String?,
        val buttonLabel: String?
    )
}