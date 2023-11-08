package com.marzec.cheatday.screen.addnewresult

import android.view.View
import app.cash.paparazzi.Paparazzi
import com.marzec.cheatday.screen.addnewresult.model.AddWeightData
import com.marzec.mvi.State
import org.joda.time.DateTimeUtils
import com.marzec.cheatday.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AddNewResultRendererTest {

    @get:Rule
    val paparazzi = Paparazzi(theme = "AppTheme")

    val initialDate by lazy {
        AddWeightData.INITIAL
    }

    val state by lazy { State.Data(initialDate) }

    val loadingState by lazy {
        State.Loading(initialDate)
    }

    val errorState by lazy {
        State.Error(initialDate, "Error has occurred")
    }

    val renderer = AddNewWeightResultRenderer().apply {
        onDatePickerClick = { }
        onNewWeightChanged = { }
        onButtonClick = { }
    }

    @Before
    fun setUp() {
        DateTimeUtils.setCurrentMillisFixed(0)
    }

    @Test
    fun initialState() = compare(state)

    @Test
    fun loadingState() = compare(loadingState)

    @Test
    fun errorState() = compare(errorState)

    private fun compare(state: State<AddWeightData>) {
        val view = paparazzi.inflate<View>(R.layout.fragment_add_new_weight_result)
        renderer.init(weightId = null, view)
        renderer.render(state)
        paparazzi.snapshot(view)
    }
}
