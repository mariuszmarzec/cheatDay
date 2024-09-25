package com.marzec.cheatday.screens

import android.view.View
import android.widget.EditText
import androidx.test.espresso.matcher.ViewMatchers
import com.kaspersky.kaspresso.screens.KScreen
import com.marzec.cheatday.R
import com.marzec.cheatday.common.typeAndCloseKeyboard
import com.marzec.cheatday.screen.home.MainActivity
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.dialog.KAlertDialog
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf

object WeightsScreen : KScreen<WeightsScreen>() {

    override val layoutId: Int = R.layout.activity_main

    override val viewClass: Class<*> = MainActivity::class.java

    val floatingButton = KView {
        withId(R.id.floating_button)
    }

    val chartButton = KView {
        withId(R.id.chart)
    }

    val weights: KRecyclerView = KRecyclerView({
        withId(R.id.recycler_view)
    }, itemTypeBuilder = {
        itemType(::Item)
    })

    val inputDialog = KAlertDialog()

    fun isDisplayed() {
        floatingButton.isDisplayed()
        weights.isDisplayed()
    }

    fun typeMaxPossibleWeight(weight: String) {
        val kEditText = KEditText(allOf(ViewMatchers.isRoot())) {
            isAssignableFrom(EditText::class.java)
        }
        typeAndCloseKeyboard(kEditText, weight)
    }
}

class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
    val label: KTextView = KTextView(parent) { withId(R.id.label) }
    val value: KTextView = KTextView(parent) { withId(R.id.value) }
}
