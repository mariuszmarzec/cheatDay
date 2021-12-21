package com.marzec.cheatday.screen.addnewresult

import androidx.test.rule.GrantPermissionRule
import android.Manifest
import androidx.core.os.bundleOf
import com.karumi.shot.ScreenshotTest
import com.marzec.cheatday.common.PolicySetupRule
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
    var runtimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var policySetupRule = PolicySetupRule()

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
    fun initialState() = compare(state)

    @Test
    fun loadingState() = compare(loadingState)

    @Test
    fun errorState() = compare(errorState)

    private fun compare(state: State<AddWeightData>) {
        compareStateScreenshot<AddNewWeightResultFragment>(
            state,
            fragmentArgs = bundleOf("weightId" to null)
        )
    }
}
