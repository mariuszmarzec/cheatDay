package com.marzec.cheatday.screens

import com.kaspersky.kaspresso.screens.KScreen
import com.marzec.cheatday.R
import com.marzec.cheatday.screen.home.MainActivity
import io.github.kakaocup.kakao.common.views.KView

object HomeScreen : KScreen<HomeScreen>() {

    override val layoutId: Int = R.layout.activity_main

    override val viewClass: Class<*> = MainActivity::class.java

    val cheatDayCounter = KView {
        withId(R.id.cheat_counter)
    }

    val dietDayCounter = KView {
        withId(R.id.diet_counter)
    }

    val workoutDayCounter = KView {
        withId(R.id.workout_counter)
    }

    val logoutButton = KView {
        withId(R.id.logout)
    }

    fun isDisplayed() {
        cheatDayCounter.isDisplayed()
        dietDayCounter.isDisplayed()
        workoutDayCounter.isDisplayed()
    }
}