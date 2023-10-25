package com.marzec.adapterdelegate.payload;

import androidx.recyclerview.widget.DiffUtil
import com.marzec.adapterdelegate.model.ListItem

class ListItemDiffUtilCallback<Item : ListItem> : DiffUtil.ItemCallback<Item>(){

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.equals(newItem)
    }

    override fun getChangePayload(oldItem: Item, newItem: Item): Any? {
        return oldItem.calculatePayload(newItem)
    }
}
