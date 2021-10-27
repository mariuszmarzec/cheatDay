package com.marzec.cheatday.common

import android.app.Instrumentation
import android.content.ComponentName
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.internal.inject.InstrumentationContext
import androidx.test.platform.app.InstrumentationRegistry
import com.kaspersky.kaspresso.screens.KScreen
import com.marzec.cheatday.api.Api
import com.marzec.cheatday.model.domain.CurrentUserDomain
import com.marzec.cheatday.screen.home.MainActivity
import io.github.kakaocup.kakao.edit.KEditText
import java.io.File
import okhttp3.mockwebserver.MockResponse

fun startApplication() {
    ActivityScenario.launch<MainActivity>(
        Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                MainActivity::class.java
            )
        )
    )
}

fun <T : KScreen<T>> KScreen<T>.typeAndCloseKeyboard(
    editText: KEditText,
    text: String
) {
    editText.clearText()
    editText.typeText(text)
    closeSoftKeyboard()
}

val currentUser = CurrentUserDomain(
    id = 1,
    auth = "token",
    email = "email"
)

fun responseJson(fileName: String): String {
    val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream("response/$fileName")
    return inputStream.bufferedReader().use { it.readText() }
}

fun loginResponseJson() = responseJson("user.json")

fun loginResponse() = MockResponse()
    .setHeader(Api.Headers.AUTHORIZATION, "token")
    .setBody(loginResponseJson())

fun logoutResponse() = MockResponse().setResponseCode(200)

fun emptyWeightsListResponse() = MockResponse()
    .setHeader(Api.Headers.AUTHORIZATION, "token")
    .setBody(responseJson("empty_weights.json"))

fun oneWeightsListResponse() = MockResponse()
    .setHeader(Api.Headers.AUTHORIZATION, "token")
    .setBody(responseJson("one_weights.json"))

fun addWeightsResponse() = MockResponse()
    .setHeader(Api.Headers.AUTHORIZATION, "token")
