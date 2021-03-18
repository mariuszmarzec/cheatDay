package com.marzec.cheatday.api

object Api {

    const val BASE_URL = "http://fiteo-env.eba-mpctrvdb.us-east-2.elasticbeanstalk.com/test/fiteo/api/1/"

    const val BASE_CHEAT_URL = "http://fiteo-env.eba-mpctrvdb.us-east-2.elasticbeanstalk.com/test/cheat/api/1/"

    object Headers {
        const val env = "TEST"

        const val AUTHORIZATION_TEST = "Authorization-Test"
        const val AUTHORIZATION_PROD = "Authorization"
        val AUTHORIZATION
            get() = if (env == "TEST") AUTHORIZATION_TEST else AUTHORIZATION_PROD
    }
}