package com.marzec.adapterdelegate.adapter

import android.view.ViewGroup
import com.google.common.truth.Truth.assertThat
import com.marzec.adapterdelegate.adapter.TestItem.Companion.ROW_VIEW_TYPE
import com.marzec.adapterdelegate.model.ListItem
import com.marzec.adapterdelegate.viewholder.BaseViewHolder
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test

class DelegateAdapterTest {

    val headerItem = TestItem.Header("header")
    val rowItem = TestItem.Header("row")
    val rowItem2 = TestItem.Header("row2")

    val itemList = listOf(headerItem, rowItem, rowItem2)

    val delegateManager = mockk<DelegateManager<ListItem>>()

    val adapter = spyk(DelegateAdapter(delegateManager)) {
        every { items } returns itemList
    }

    @Test
    fun `getting item view type based on delegate manager`() {
        every { delegateManager.getItemViewType(rowItem) } returns ROW_VIEW_TYPE

        assertThat(adapter.getItemViewType(position = 1)).isEqualTo(ROW_VIEW_TYPE)
    }

    @Test
    fun `create view holder based on delegate manager`() {
        val viewGroup = mockk<ViewGroup>()
        val viewHolder = RowViewHolder()
        every { delegateManager.onCreateViewHolder(viewGroup, ROW_VIEW_TYPE) } returns viewHolder

        val actual = adapter.onCreateViewHolder(viewGroup, ROW_VIEW_TYPE)

        assertThat(actual).isEqualTo(viewHolder)
    }

    @Test
    fun `on bind view holder use delegate manager`() {
        val viewHolder = mockk<BaseViewHolder<ListItem>>(relaxed = true)

        adapter.onBindViewHolder(viewHolder, position = 1)

        verify { viewHolder.onBind(rowItem) }
    }

    @Test
    fun `on payload use delegate manager`() {
        val viewHolder = mockk<BaseViewHolder<ListItem>>(relaxed = true)

        adapter.onPayload(holder = viewHolder, position = 1, payloads = listOf(Unit))

        verify { viewHolder.onPayload(rowItem, payloads = listOf(Unit)) }
    }
}
