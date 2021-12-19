package com.marzec.cheatday.common

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class PolicySetupRule : TestRule {

    override fun apply(base: Statement, description: Description?): Statement =
        object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

                val apiVersion = Build.VERSION.SDK_INT
                when {
                    apiVersion == Build.VERSION_CODES.P -> {
                        uiDevice.executeShellCommand("settings put global hidden_api_policy_pre_p_apps 1")
                        uiDevice.executeShellCommand("settings put global hidden_api_policy_p_apps 1")
                    }
                    apiVersion >= Build.VERSION_CODES.Q -> {
                        uiDevice.executeShellCommand("settings put global hidden_api_policy 1")
                    }
                }

                base.evaluate()
            }
        }
}