package com.marzec.adapterdelegate.model

interface ListItem {

    val id: String

    val viewType: Int

    fun calculatePayload(listItem: ListItem): Any? = null

    fun areItemsTheSame(listItem: ListItem): Boolean =
        id == listItem.id && viewType == listItem.viewType
}
