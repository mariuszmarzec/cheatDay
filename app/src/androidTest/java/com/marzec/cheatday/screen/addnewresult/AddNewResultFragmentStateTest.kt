package com.marzec.cheatday.screen.addnewresult

import com.karumi.shot.ScreenshotTest
import com.marzec.cheatday.api.toContentData
import com.marzec.cheatday.common.compareStateScreenshot
import com.marzec.cheatday.screen.addnewresult.model.AddWeightData
import com.marzec.mvi.State
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.joda.time.DateTimeUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AddNewResultFragmentStateTest : ScreenshotTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

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

    @Before
    fun setUp() {
        DateTimeUtils.setCurrentMillisFixed(0)
    }

    @Test
    fun initialState() = compareStateScreenshot<AddNewWeightResultFragment>(state)

    @Test
    fun loadingState() = compareStateScreenshot<AddNewWeightResultFragment>(loadingState)

    @Test
    fun errorState() = compareStateScreenshot<AddNewWeightResultFragment>(errorState)
}
