package com.marzec.adapterdelegate.viewholder

import android.view.View
import com.marzec.adapterdelegate.model.ListItem

abstract class PayloadViewHolder<Item : ListItem, Payload>(itemView: View) :
    BaseViewHolder<Item>(itemView) {

    @Suppress("UNCHECKED_CAST")
    override fun onPayload(item: Item, payloads: List<Any>) {
        val payload: Payload = payloads.first() as Payload
        onPayload(item, payload)
    }

    abstract fun onPayload(item: Item, payload: Payload)
}