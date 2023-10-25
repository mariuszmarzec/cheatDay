package com.marzec.adapterdelegate.adapter

import com.marzec.adapterdelegate.model.ListItem

sealed class TestItem(open val name: String) : ListItem {

    data class Header(override val name: String) : TestItem(name) {

        override val id: String = name

        override val viewType: Int = HEADER_VIEW_TYPE
    }

    data class Row(override val name: String) : TestItem(name) {

        override val id: String = name

        override val viewType: Int = ROW_VIEW_TYPE
    }

    companion object {
        val HEADER_VIEW_TYPE = 1
        val ROW_VIEW_TYPE = HEADER_VIEW_TYPE + 1
    }
}
