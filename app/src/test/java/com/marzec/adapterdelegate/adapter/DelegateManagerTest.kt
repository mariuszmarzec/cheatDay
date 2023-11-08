package com.marzec.adapterdelegate.adapter

import com.google.common.truth.Truth.assertThat
import com.marzec.adapterdelegate.adapter.TestItem.Companion.HEADER_VIEW_TYPE
import com.marzec.adapterdelegate.adapter.TestItem.Companion.ROW_VIEW_TYPE
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DelegateManagerTest {

    val header = TestItem.Header("header")
    val row = TestItem.Row("row")

    val delegateManager = DelegateManager<TestItem>()
        .add(HeaderViewAdapterDelegate())
        .add(RowViewAdapterDelegate())

    @Test
    fun `getting view type for header`() {
        val actual = delegateManager.getItemViewType(header)

        assertThat(actual).isEqualTo(HEADER_VIEW_TYPE)
    }

    @Test
    fun `getting view type for row`() {
        val actual = delegateManager.getItemViewType(row)

        assertThat(actual).isEqualTo(ROW_VIEW_TYPE)
    }

    @Test
    fun `throw exception when delegate for item is not added`() {
        assertThrows<IllegalArgumentException> {
            DelegateManager<TestItem>().getItemViewType(header)
        }
    }

    @Test
    fun `verify if right view holder is created for header`() {
        val actual = delegateManager.onCreateViewHolder(mockk(relaxed = true), HEADER_VIEW_TYPE)

        assertThat(actual).isInstanceOf(HeaderViewHolder::class.java)
    }

    @Test
    fun `verify if right view holder is created for row`() {
        val actual = delegateManager.onCreateViewHolder(mockk(relaxed = true), ROW_VIEW_TYPE)

        assertThat(actual).isInstanceOf(RowViewHolder::class.java)
    }
}