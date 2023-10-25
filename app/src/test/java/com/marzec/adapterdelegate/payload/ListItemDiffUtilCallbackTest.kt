package com.marzec.adapterdelegate.payload

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ListItemDiffUtilCallbackTest {

    private val callback = ListItemDiffUtilCallback<TestItem>()

    @Test
    fun areItemsTheSame() {
        val old = testItem(id = "id", value = "1")
        val new = testItem(id = "id")

        assertThat(callback.areItemsTheSame(old, new)).isTrue()
    }
    @Test
    fun areItemsTheSame_differentId() {
        val old = testItem(id = "id", value = "1")
        val new = testItem(id = "id_new")

        assertThat(callback.areItemsTheSame(old, new)).isFalse()
    }

    @Test
    fun areContentsTheSame() {
        val old = testItem(id = "id")
        val new = testItem(id = "id")

        assertThat(callback.areContentsTheSame(old, new)).isTrue()
    }

    @Test
    fun areContentsTheSame_notSame() {
        val old = testItem(id = "id", value = "1")
        val new = testItem(id = "id")

        assertThat(callback.areContentsTheSame(old, new)).isFalse()
    }

    @Test
    fun getChangePayload() {
        val old = testItem(id = "id")
        val new = testItem(id = "id", value = "1")

        assertThat(callback.getChangePayload(old, new)).isEqualTo(
            TestItem.Payload(value = "1", nullableValue = null)
        )
    }
}
