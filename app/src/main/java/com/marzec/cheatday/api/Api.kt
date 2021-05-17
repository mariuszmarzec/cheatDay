package com.marzec.cheatday.api

import org.joda.time.format.DateTimeFormat


object Api {

    const val env = "TEST"

    val HOST = if (env == "TEST") "http://fiteo-env-1.eba-cba76vkj.us-east-2.elasticbeanstalk.com/test"
    else "http://fiteo-env-1.eba-cba76vkj.us-east-2.elasticbeanstalk.com/"

    val BASE_URL = "$HOST/fiteo/api/1/"

    val BASE_CHEAT_URL = "$HOST/cheat/api/1/"

    const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    val DATE_FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT)

    object Headers {

        const val AUTHORIZATION_TEST = "Authorization-Test"
        const val AUTHORIZATION_PROD = "Authorization"
        val AUTHORIZATION
            get() = if (env == "TEST") AUTHORIZATION_TEST else AUTHORIZATION_PROD
    }
}
