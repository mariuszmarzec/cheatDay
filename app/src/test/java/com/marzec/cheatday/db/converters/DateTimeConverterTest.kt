package com.marzec.cheatday.db.converters

import org.joda.time.DateTime
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DateTimeConverterTest {

    val converter = DateTimeConverter()

    @Test
    fun fromTimestamp() {
        assertEquals(DateTime(0), converter.fromTimestamp(0))
        assertEquals(null, converter.fromTimestamp(null))
    }

    @Test
    fun dateToTimestamp() {
        assertEquals(0, converter.dateToTimestamp(DateTime(0)))
        assertEquals(null, converter.dateToTimestamp(null))
    }
}
