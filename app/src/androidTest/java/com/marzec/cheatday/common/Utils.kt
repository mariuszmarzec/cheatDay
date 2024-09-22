package com.marzec.cheatday.common

import android.content.ComponentName
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.kaspersky.kaspresso.screens.KScreen
import com.marzec.cheatday.api.Api
import com.marzec.cheatday.model.domain.CurrentUserDomain
import com.marzec.cheatday.screen.home.MainActivity
import io.github.kakaocup.kakao.delegate.ViewInteractionDelegate
import io.github.kakaocup.kakao.edit.KEditText
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

fun loginResponse() = okStatusResponse()
    .setBody(loginResponseJson())

fun logoutResponse() = MockResponse().setResponseCode(200)

fun emptyWeightsListResponse() = okStatusResponse()
    .setBody(responseJson("empty_weights.json"))

fun oneWeightResponse() = okStatusResponse()
    .setBody(responseJson("one_weight.json"))

fun oneWeightsListResponse() = okStatusResponse()
    .setBody(responseJson("one_weights.json"))

fun updatedOneWeightsListResponse() = okStatusResponse()
    .setBody(responseJson("updated_one_weights.json"))

fun fewWeightsListResponse() = okStatusResponse()
    .setBody(responseJson("few_weights.json"))

fun updatedFewWeightsListResponse() = okStatusResponse()
    .setBody(responseJson("updated_few_weights.json"))

fun addWeightsResponse() = okStatusResponse()
    .setBody(responseJson("one_weight.json"))

fun updateWeightResponse() = okStatusResponse()
    .setBody(responseJson("updated_weight_0.json"))

private fun okStatusResponse() = MockResponse()
    .setHeader(Api.Headers.AUTHORIZATION, "token")

fun ViewInteractionDelegate.getId(): Int {
    var id = -1
    check { view, _ -> id = view.id }
    return id
}
