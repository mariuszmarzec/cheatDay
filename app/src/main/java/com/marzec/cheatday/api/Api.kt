package com.marzec.cheatday.api

import com.marzec.cheatday.BuildConfig
import org.joda.time.format.DateTimeFormat

object Api {

    const val LOCALHOST_API = "http://localhost"
    val HOST = BuildConfig.HOST

    val BASE_URL = "$HOST/fiteo/api/1/"

    val BASE_CHEAT_URL = "$HOST/cheat/api/1/"

    val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    val DATE_FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT)

    object Headers {

        val AUTHORIZATION = BuildConfig.AUTHORIZATION
    }
}
