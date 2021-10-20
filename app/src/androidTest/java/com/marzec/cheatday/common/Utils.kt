package com.marzec.cheatday.common

import android.content.ComponentName
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.marzec.cheatday.screen.home.MainActivity

fun startApplication() {
    ActivityScenario.launch<MainActivity>(
        Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                MainActivity::class.java
            )
        )
    )
}
