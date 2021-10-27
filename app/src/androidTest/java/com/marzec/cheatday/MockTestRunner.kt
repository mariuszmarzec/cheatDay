package com.marzec.cheatday

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import com.karumi.shot.ShotTestRunner
import dagger.hilt.android.testing.HiltTestApplication
import net.danlew.android.joda.JodaTimeAndroid

class MockTestRunner : ShotTestRunner() {
    override fun onCreate(args: Bundle) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        super.onCreate(args)
    }

    @Throws(
        InstantiationException::class,
        IllegalAccessException::class,
        ClassNotFoundException::class
    )
    override fun newApplication(
        cl: ClassLoader,
        className: String,
        context: Context
    ): Application = super.newApplication(cl,
        TestApplication_Application::class.java.name,
        context
    )
}