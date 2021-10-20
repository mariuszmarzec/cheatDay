package com.marzec.cheatday.screens

import com.kaspersky.kaspresso.screens.KScreen
import com.marzec.cheatday.R
import com.marzec.cheatday.screen.home.MainActivity
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton

object LoginScreen : KScreen<LoginScreen>() {

    override val layoutId: Int = R.layout.activity_main

    override val viewClass: Class<*> = MainActivity::class.java

    val loginEditText = KEditText {
        withId(R.id.login)
    }

    val passwordEditText = KEditText {
        withId(R.id.password)
    }

    val loginButton = KButton { withId(R.id.button) }

    fun typeLogin(login: String) {
        loginEditText.typeText(login)
    }

    fun typePassword(password: String) {
        passwordEditText.typeText(password)
    }

    fun isDisplayed() {
        loginEditText {
            isDisplayed()
        }
        passwordEditText {
            isDisplayed()
        }
    }
}