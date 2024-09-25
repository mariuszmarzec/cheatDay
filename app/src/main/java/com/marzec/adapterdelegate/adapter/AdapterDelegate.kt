package com.marzec.adapterdelegate.adapter

import android.view.ViewGroup
import com.marzec.adapterdelegate.model.ListItem
import com.marzec.adapterdelegate.viewholder.BaseViewHolder

interface AdapterDelegate<Item : ListItem> {

    val viewType: Int

    fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<out Item>
}
