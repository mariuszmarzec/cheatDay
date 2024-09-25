package com.marzec.adapterdelegate.payload

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested

import org.junit.jupiter.api.Test

class PayloadsKtTest {

    @Nested
    inner class Payload {

        @Test
        fun objectsAreEquals() {
            val oldValue = "val"
            val newValue = "val"

            val actual = payload(newValue, oldValue)

            assertThat(actual).isNull()
        }

        @Test
        fun objectsAreNotEqual() {
            val oldValue = "val"
            val newValue = "value"

            val actual = payload(newValue, oldValue)

            assertThat(actual).isEqualTo(newValue)
        }
    }

    @Nested
    inner class NullablePayload {

        @Test
        fun objectsAreEquals() {
            val oldValue = "val"
            val newValue = "val"

            val actual = nullablePayload(newValue, oldValue)

            assertThat(actual).isNull()
        }

        @Test
        fun objectsAreNotEqual() {
            val oldValue = "val"
            val newValue = "value"

            val actual = nullablePayload(newValue, oldValue)

            assertThat(actual).isEqualTo(NullablePayload(newValue))
        }

        @Test
        fun changedToNull() {
            val oldValue = "val"
            val newValue = null

            val actual = nullablePayload(newValue, oldValue)

            assertThat(actual).isEqualTo(NullablePayload(null))
        }
    }
}
