package com.marzec.adapterdelegate.payload

import com.marzec.adapterdelegate.model.ListItem

data class TestItem(
    override val id: String,
    val value: String,
    val nullableValue: String?
) : ListItem {
    override val viewType: Int = 1

    override fun calculatePayload(listItem: ListItem): Any? =
        (listItem as? TestItem)?.let { other ->
            Payload(
                payload(other.value, value),
                nullablePayload(other.nullableValue, nullableValue)
            )
        }

    data class Payload(
        val value: String?,
        val nullableValue: NullablePayload<String>?
    )
}

fun testItem(
    id: String = "",
    value: String = "",
    nullableValue: String? = null
) = TestItem(
    id = id,
    value = value,
    nullableValue = nullableValue
)
