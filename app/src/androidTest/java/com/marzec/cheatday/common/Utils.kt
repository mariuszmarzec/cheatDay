package com.marzec.cheatday.common

import android.content.ComponentName
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.kaspersky.kaspresso.screens.KScreen
import com.marzec.cheatday.screen.home.MainActivity
import io.github.kakaocup.kakao.edit.KEditText
import java.io.File

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

fun <T: KScreen<T>> KScreen<T>.typeAndCloseKeyboard(
    editText: KEditText,
    text: String
) {
    editText.typeText(text)
    closeSoftKeyboard()
}

fun responseJson(fileName: String) = File(fileName).readText()

fun loginResponseJson() = responseJson("response/user.json")