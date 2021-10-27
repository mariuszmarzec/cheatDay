package com.marzec.cheatday.screens

import com.kaspersky.kaspresso.screens.KScreen
import com.marzec.cheatday.R
import com.marzec.cheatday.screen.home.MainActivity
import com.marzec.cheatday.views.KCounterView
import io.github.kakaocup.kakao.common.views.KView

object HomeScreen : KScreen<HomeScreen>() {

    override val layoutId: Int = R.layout.activity_main

    override val viewClass: Class<*> = MainActivity::class.java

    val cheatDayCounter = KCounterView {
        withId(R.id.cheat_counter)
    }

    val dietDayCounter = KCounterView {
        withId(R.id.diet_counter)
    }

    val workoutDayCounter = KCounterView {
        withId(R.id.workout_counter)
    }

    val logoutButton = KView {
        withId(R.id.logout)
    }

    val weightTab = KView {
        withId(R.id.weights)
    }

    fun isDisplayed() {
        cheatDayCounter.isDisplayed()
        dietDayCounter.isDisplayed()
        workoutDayCounter.isDisplayed()
    }
}
