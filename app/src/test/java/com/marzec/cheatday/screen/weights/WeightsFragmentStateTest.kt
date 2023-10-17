package com.marzec.cheatday.screen.weights

import android.Manifest
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.screen.weights.model.WeightsData
import com.marzec.mvi.State
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test


///**
// * Screenshottests device - Pixel 3 API 29
// */
//class WeightsFragmentStateTest : ScreenshotTest {
//
//    @get:Rule
//    var runtimePermissionRule: GrantPermissionRule =
//        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
//    @get:Rule
//    var policySetupRule = PolicySetupRule()
//
//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)
//
//    val weightsData by lazy {
//        WeightsData(
//            minWeight = WeightResult(id = 9, value = 80f, date = DateTime.now()),
//            weekAverage = 83f,
//            maxPossibleWeight = 85f,
//            targetWeight = 78f,
//            weights = listOf(
//                WeightResult(id = 9, value = 80f, date = DateTime.now()),
//                WeightResult(id = 8, value = 82f, date = DateTime.now().minusDays(1)),
//                WeightResult(id = 7, value = 83.5f, date = DateTime.now().minusDays(2))
//            )
//        )
//    }
//    val initialState = State.Data(weightsData)
//    val loadingState = State.Loading<WeightsData>()
//    val pendingActionState = State.Loading(weightsData)
//    val errorState = State.Error(weightsData, "Error has occurred")
//
//    @Before
//    fun setUp() {
//        DateTimeUtils.setCurrentMillisFixed(0)
//    }
//
//    @Test
//    fun initialState() = compareStateScreenshot<WeightsFragment>(initialState)
//
//    @Test
//    fun loadingState() = compareStateScreenshot<WeightsFragment>(loadingState)
//
//    @Test
//    fun pendingActionState() = compareStateScreenshot<WeightsFragment>(pendingActionState)
//
//    @Test
//    fun errorState() = compareStateScreenshot<WeightsFragment>(errorState)
//}
