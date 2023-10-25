package com.marzec.cheatday.view.delegates.labeledrowitem

import com.marzec.adapterdelegate.model.ListItem
import com.marzec.cheatday.view.delegates.labeledrowitem.LabeledRowAdapterDelegate.Companion.VIEW_TYPE

data class LabeledRow(
    override val id: String,
    val label: String,
    val value: String
) : ListItem {
    override val viewType: Int
        get() = VIEW_TYPE

    var onClickListener: (() -> Unit)? = null

    var onLongClickListener: (() -> Unit)? = null

    data class Payload(
        val label: String?,
        val value: String?
    )
}
