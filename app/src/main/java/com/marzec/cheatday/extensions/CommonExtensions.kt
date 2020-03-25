package com.marzec.cheatday.extensions

import org.joda.time.DateTime

fun Long?.toDateTime() = this?.let { DateTime(it) }