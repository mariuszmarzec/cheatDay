package com.marzec.cheatday

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.test.runner.AndroidJUnitRunner
import com.facebook.testing.screenshot.ScreenshotRunner
import dagger.hilt.android.testing.HiltTestApplication

class MockTestRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle) {
        ScreenshotRunner.onCreate(this, arguments);
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        super.onCreate(arguments)
    }

    override fun finish(resultCode: Int, results: Bundle?) {
        ScreenshotRunner.onDestroy()
        super.finish(resultCode, results)
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
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}