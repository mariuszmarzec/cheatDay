package com.marzec.cheatday.view.delegates.labeledrowitem

import android.view.LayoutInflater
import android.view.ViewGroup
import com.marzec.adapterdelegate.adapter.AdapterDelegate
import com.marzec.adapterdelegate.viewholder.BaseViewHolder
import com.marzec.adapterdelegate.viewholder.PayloadViewHolder
import com.marzec.cheatday.R

class LabeledRowAdapterDelegate : AdapterDelegate<LabeledRow> {

    override val viewType: Int
        get() {
            return VIEW_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<out LabeledRow> {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_labeled_row, parent, false)
            .let {
                LabeledRowViewHolder(it as LabeledRowView)
            }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_labeled_row
    }
}

class LabeledRowViewHolder(
    private val view: LabeledRowView
) : PayloadViewHolder<LabeledRow, LabeledRow.Payload>(view) {

    override fun onBind(item: LabeledRow) {
        setLabel(item.label)
        setValue(item.value)
        item.onClickListener?.let {
            view.setOnClickListener { it() }
        }
        item.onLongClickListener?.let {
            view.setOnLongClickListener { it(); true }
        }
    }

    override fun onPayload(item: LabeledRow, payload: LabeledRow.Payload) {
        with(payload) {
            label?.let(::setLabel)
            value?.let(::setValue)
        }
    }

    private fun setLabel(label: String) {
        view.setLabel(label)
    }

    fun setValue(value: String) {
        view.setValue(value)
    }
}