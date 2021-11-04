package com.marzec.cheatday.db.converters

import androidx.room.TypeConverter
import com.marzec.cheatday.extensions.toDateTime
import org.joda.time.DateTime

open class DateTimeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): DateTime? = value?.toDateTime()

    @TypeConverter
    fun dateToTimestamp(date: DateTime?): Long? = date?.millis

}
