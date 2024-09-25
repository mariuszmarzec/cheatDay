package com.marzec.cheatday.view.delegates.progress

import com.marzec.adapterdelegate.model.ListItem
import com.marzec.cheatday.view.delegates.progress.ProgressScreenDelegate.Companion.VIEW_TYPE

data class ProgressScreen(
    override val id: String
) : ListItem {

    override val viewType: Int = VIEW_TYPE
}
