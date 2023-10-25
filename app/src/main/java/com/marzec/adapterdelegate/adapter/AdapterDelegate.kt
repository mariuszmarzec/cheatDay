package com.marzec.adapterdelegate.adapter

import android.view.ViewGroup
import com.marzec.adapterdelegate.model.ListItem

interface AdapterDelegate<Item : ListItem> {

    val viewType: Int

    fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<out Item>
}