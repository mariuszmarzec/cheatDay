package com.marzec.cheatday.screens

import com.kaspersky.kaspresso.screens.KScreen
import com.marzec.cheatday.R
import com.marzec.cheatday.common.typeAndCloseKeyboard
import com.marzec.cheatday.screen.home.MainActivity
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView

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

    val errorMessage = KTextView { withId(R.id.error) }

    fun typeLogin(login: String) = typeAndCloseKeyboard(loginEditText, login)

    fun typePassword(password: String) = typeAndCloseKeyboard(passwordEditText, password)

    fun isDisplayed() {
        loginEditText {
            isDisplayed()
        }
        passwordEditText {
            isDisplayed()
        }
    }
}
