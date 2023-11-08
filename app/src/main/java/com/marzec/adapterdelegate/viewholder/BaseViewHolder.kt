package com.marzec.adapterdelegate.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.marzec.adapterdelegate.model.ListItem

abstract class BaseViewHolder<Item : ListItem>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(item: Item)

    abstract fun onPayload(item: Item, payloads: List<Any>)
}

