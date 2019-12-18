package com.marzec.cheatday.extensions

import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CommonExtensionsKtTest {

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun toDateTime() {
        val nullLong: Long? = null
        assertEquals(null, nullLong.toDateTime())
        assertEquals(DateTime(10), 10L.toDateTime())
    }
}