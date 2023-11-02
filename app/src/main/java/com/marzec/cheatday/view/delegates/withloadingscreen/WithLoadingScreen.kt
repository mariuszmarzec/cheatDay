package com.marzec.cheatday.view.delegates.withloadingscreen

import com.marzec.adapterdelegate.model.ListItem
import com.marzec.cheatday.view.delegates.withloadingscreen.WithLoadingScreenDelegate.Companion.VIEW_TYPE

data class WithLoadingScreen(
    override val id: String,
    val items: List<ListItem>,
    val showOverflowLoading: Boolean
) : ListItem {

    override val viewType: Int = VIEW_TYPE

    data class Payload(
        val items: List<ListItem>?,
        val showOverflowLoading: Boolean?
    )
}