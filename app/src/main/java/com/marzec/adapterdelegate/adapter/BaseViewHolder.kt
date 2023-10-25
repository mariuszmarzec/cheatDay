package com.marzec.adapterdelegate.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.marzec.adapterdelegate.model.ListItem

abstract class BaseViewHolder<Item : ListItem>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(item: Item)

    abstract fun onPayload(item: Item, payloads: List<Any>)
}

abstract class PayloadViewHolder<Item : ListItem, Payload>(itemView: View) :
    BaseViewHolder<Item>(itemView) {

    @Suppress("UNCHECKED_CAST")
    override fun onPayload(item: Item, payloads: List<Any>) {
        val payload: Payload = payloads.first() as Payload
        onPayload(item, payload)
    }

    abstract fun onPayload(item: Item, payload: Payload)
}