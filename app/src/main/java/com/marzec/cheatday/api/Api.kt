package com.marzec.cheatday.api

import com.marzec.cheatday.BuildConfig
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

object Api {

    const val LOCALHOST_API = "http://localhost"
    const val HOST = BuildConfig.HOST

    const val BASE_URL = "$HOST/fiteo/api/1/"

    const val BASE_CHEAT_URL = "$HOST/cheat/api/1/"

    const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    val DATE_FORMATTER: DateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT)

    object Headers {

        const val AUTHORIZATION = BuildConfig.AUTHORIZATION
    }
}
