package com.marzec.adapterdelegate.adapter

import android.view.ViewGroup
import com.marzec.adapterdelegate.model.ListItem

class DelegateAdapter<Item : ListItem>(
    private val delegateManager: DelegateManager<Item>
) : BaseListItemsAdapter<Item, BaseViewHolder<Item>>() {

    override fun getItemViewType(position: Int): Int =
        delegateManager.getItemViewType(items[position])

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Item> =
        delegateManager.onCreateViewHolder(parent, viewType) as BaseViewHolder<Item>

    override fun onBindViewHolder(holder: BaseViewHolder<Item>, position: Int) {
        holder.onBind(items[position])
    }

    override fun onPayload(holder: BaseViewHolder<Item>, position: Int, payloads: List<Any>) {
        holder.onPayload(items[position], payloads)
    }
}
