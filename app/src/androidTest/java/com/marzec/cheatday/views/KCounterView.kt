package com.marzec.cheatday.views

import android.view.View
import androidx.test.espresso.DataInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.marzec.cheatday.R
import com.marzec.cheatday.common.getId
import io.github.kakaocup.kakao.common.actions.BaseActions
import io.github.kakaocup.kakao.common.assertions.BaseAssertions
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.common.views.KView
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf

class KCounterView : KBaseView<KCounterView>, CounterViewAssertions, CounterViewActions {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(parent: Matcher<View>, function: ViewBuilder.() -> Unit) : super(parent, function)
    constructor(parent: DataInteraction, function: ViewBuilder.() -> Unit) : super(parent, function)
}

interface CounterViewAssertions : BaseAssertions {

    fun isValueEqual(value: String) {
        view.check(
            ViewAssertions.matches(
                ViewMatchers.withChild(
                    allOf(
                        withId(R.id.value_text_view),
                        withText(value)
                    )
                )
            )
        )
    }
}

interface CounterViewActions : BaseActions {

    fun performIncrease() {
        KView(withId(view.getId())) {
            withId(R.id.increase_button)
        }.invoke {
            isDisplayed()
            click()
        }
    }

    fun performDecrease() {
        KView(withId(view.getId())) {
            withId(R.id.decrease_button)
        }.invoke {
            isDisplayed()
            click()
        }
    }
}
