package com.marzec.cheatday.extensions

inline fun <reified T : Enum<T>> enumValueOfOrDefault(name: String, def: T): T = try {
    enumValueOf(name)
} catch (e: IllegalArgumentException) {
    def
}