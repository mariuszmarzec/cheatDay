package com.marzec.cheatday.common

import java.util.UUID
import javax.inject.Inject

class UuidProvider @Inject constructor() {

    fun create(): String = UUID.randomUUID().toString()
}