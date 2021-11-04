package com.marzec.cheatday.screens

import com.kaspersky.kaspresso.screens.KScreen
import com.marzec.cheatday.R
import com.marzec.cheatday.screen.home.MainActivity
import io.github.kakaocup.kakao.common.views.KView

object ChartScreen : KScreen<WeightsScreen>() {

    override val layoutId: Int = R.layout.activity_main

    override val viewClass: Class<*> = MainActivity::class.java

    val chart = KView {
        withId(R.id.chart)
    }

    fun isDisplayed() {
        chart.isDisplayed()
    }
}
