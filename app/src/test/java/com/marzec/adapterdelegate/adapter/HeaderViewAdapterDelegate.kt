package com.marzec.adapterdelegate.adapter

import android.view.ViewGroup
import com.marzec.adapterdelegate.adapter.TestItem.Companion.HEADER_VIEW_TYPE
import com.marzec.adapterdelegate.viewholder.BaseViewHolder
import io.mockk.mockk

class HeaderViewAdapterDelegate : AdapterDelegate<TestItem.Row> {
    override val viewType: Int
        get() = HEADER_VIEW_TYPE

    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<out TestItem.Row> =
        HeaderViewHolder()
}

class HeaderViewHolder : BaseViewHolder<TestItem.Row>(mockk()) {
    override fun onBind(item: TestItem.Row) = Unit

    override fun onPayload(item: TestItem.Row, payloads: List<Any>) = Unit
}
