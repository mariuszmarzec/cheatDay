package com.marzec.cheatday.api

import com.marzec.cheatday.BuildConfig
import org.joda.time.format.DateTimeFormat


object Api {

    const val HOST = BuildConfig.HOST

    const val BASE_URL = "$HOST/fiteo/api/1/"

    const val BASE_CHEAT_URL = "$HOST/cheat/api/1/"

    const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    val DATE_FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT)

    object Headers {

        const val AUTHORIZATION = BuildConfig.AUTHORIZATION
    }
}
