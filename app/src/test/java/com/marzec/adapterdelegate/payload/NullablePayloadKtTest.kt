package com.marzec.adapterdelegate.payload

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested

import org.junit.jupiter.api.Test

class NullablePayloadKtTest {

    @Nested
    inner class ApplyIfNonNull {

        @Test
        fun `When nullable payload is null, do not run action`() {
            val nullablePayload: NullablePayload<Any>? = null
            val action = mockk<(Any?) -> Unit>(relaxed = true)

            nullablePayload.applyIfNonNull(action)

            verify(exactly = 0) { action.invoke(any()) }
        }

        @Test
        fun `When nullable payload is not null, run action`() {
            val value = "value"
            val nullablePayload: NullablePayload<Any> = NullablePayload(value)
            val action = mockk<(Any?) -> Unit>(relaxed = true)

            nullablePayload.applyIfNonNull(action)

            verify { action.invoke(value) }
        }
    }
}
