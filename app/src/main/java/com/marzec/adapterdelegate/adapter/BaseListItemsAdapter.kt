package com.marzec.adapterdelegate.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.marzec.adapterdelegate.model.ListItem
import com.marzec.adapterdelegate.payload.ListItemDiffUtilCallback
import com.marzec.adapterdelegate.viewholder.BaseViewHolder

abstract class BaseListItemsAdapter<Item : ListItem, Holder : BaseViewHolder<Item>> :
    RecyclerView.Adapter<Holder>() {

    private val diffUtils by lazy {
        AsyncListDiffer<Item>(this, ListItemDiffUtilCallback())
    }

    var items: List<Item>
        get() = diffUtils.currentList
        set(value) {
            diffUtils.submitList(value)
        }

    override fun getItemCount(): Int {
        return diffUtils.currentList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            onPayload(holder, position, payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    abstract fun onPayload(holder: Holder, position: Int, payloads: List<Any>)
}
