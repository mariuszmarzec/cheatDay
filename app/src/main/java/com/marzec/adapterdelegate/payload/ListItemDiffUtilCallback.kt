package com.marzec.adapterdelegate.payload

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.marzec.adapterdelegate.model.ListItem

class ListItemDiffUtilCallback<Item : ListItem> : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.equals(newItem)
    }

    override fun getChangePayload(oldItem: Item, newItem: Item): Any? {
        return oldItem.calculatePayload(newItem)
    }
}
