package com.marzec.adapterdelegate.adapter

import android.view.ViewGroup
import com.marzec.adapterdelegate.model.ListItem

class DelegateManager<Item : ListItem> {

    private val delegates = mutableListOf<AdapterDelegate<out Item>>()

    fun add(adapterDelegate: AdapterDelegate<out Item>): DelegateManager<Item> {
        return apply { delegates.add(adapterDelegate) }
    }

    fun getItemViewType(item: Item): Int =
        delegates.firstOrNull { it.viewType == item.viewType }
            ?.viewType
            ?: throw IllegalArgumentException("Delegate for $item doesn't exist")

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out Item> =
        delegates.first { it.viewType == viewType }.onCreateViewHolder(parent)
}