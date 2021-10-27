package com.marzec.cheatday.screens

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.marzec.cheatday.R
import com.marzec.cheatday.screen.home.MainActivity
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object WeightsScreen : KScreen<WeightsScreen>() {

    override val layoutId: Int = R.layout.activity_main

    override val viewClass: Class<*> = MainActivity::class.java

    val floatingButton = KView {
        withId(R.id.floating_button)
    }

    val weights: KRecyclerView = KRecyclerView({
        withId(R.id.recycler_view)
    }, itemTypeBuilder = {
        itemType(::Item)
    })

    fun isDisplayed() {
        floatingButton.isDisplayed()
        weights.isDisplayed()
    }
}

class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
    val label: KTextView = KTextView(parent) { withId(R.id.label) }
    val value: KTextView = KTextView(parent) { withId(R.id.value) }
}
