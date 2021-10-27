package com.marzec.cheatday.screens

import com.kaspersky.kaspresso.screens.KScreen
import com.marzec.cheatday.R
import com.marzec.cheatday.common.typeAndCloseKeyboard
import com.marzec.cheatday.screen.home.MainActivity
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton

object AddNewWeightsScreen : KScreen<AddNewWeightsScreen>() {

    override val layoutId: Int = R.layout.activity_main

    override val viewClass: Class<*> = MainActivity::class.java

    val weightInput = KEditText {
        withId(R.id.weight_edit_text)
    }

    val button = KButton {
        withId(R.id.button)
    }

    fun isDisplayed() {
        weightInput.isDisplayed()
        button.isDisplayed()
    }

    fun typeWeight(weight: String) = typeAndCloseKeyboard(weightInput, weight)
}
