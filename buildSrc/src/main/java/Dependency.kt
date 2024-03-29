object Dependency {

    val crashlytics_version = "2.4.1"
    val firebase_version = "26.3.0"
    val kotlin_version = "1.9.20"
    val junit4_version = "4.13.2"
    val leak_canary_version = "2.12"
    val coroutines_version = "1.7.3"
    val appcompat_version = "1.1.0"
    val material_version = "1.2.0"
    val ktx_version = "2.3.0-alpha02"
    val core_ktx_version = "1.2.0"
    val constraint_layout_version = "1.1.3"
    val retrofit_version = "2.9.0"
    val gson_version = "2.8.5"
    val okhttp_version = "4.9.0"
    val anko_version = "0.10.8"
    val junit5_version = "5.10.1"
    val joda_version = "2.10.3"
    val truth_version = "1.1.3"
    val viewmodel_version = "1.1.1"
    val room_version = "2.6.0-rc01"
    val junit_ext_version = "1.1.5"
    val android_test_services_version = "1.5.0-alpha01"
    val android_test_runner_version = "1.6.0-alpha03"
    val test_core = "1.5.0"
    val test_rules = "1.5.0"
    val runner_version = "1.6.0-alpha03"
    val orchestrator_version = "1.5.0-alpha01"
    val arch_testing_version = "2.2.0"
    val espresso_core = "3.5.1"
    val fragment_version = "1.6.1"
    val activity_version = "1.1.0"
    val screenshot_testing_version = "1.3.1"
    val nav_version = "2.7.4"
    val chart_version = "v3.1.0"
    val junit5_plugin_version = "1.10.0.0"
    val android_gradle_plugin = "8.1.3"
    val datastore_version = "1.0.0"
    val google_service_version = "4.3.4"
    val hilt_version = "2.48.1"
    val mockk_version = "1.13.8"
    val kaspresso_version = "1.5.3"
    val detektVersion = "1.18.1"
    val kspVersion = "1.9.20-1.0.14"
    val jacoco_version = "0.8.9"
    val hamcrestVersion = "2.2"

    object BuildPlugins {
        val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:${crashlytics_version}"
        val androidBuildPlugin = "com.android.tools.build:gradle:${android_gradle_plugin}"
        val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}"
        val jUnit5Plugin = "de.mannodermaus.gradle.plugins:android-junit5:${junit5_plugin_version}"
        val kotlinAllOpen = "org.jetbrains.kotlin:kotlin-allopen:${kotlin_version}"
        val screenshots = "app.cash.paparazzi:paparazzi-gradle-plugin:${screenshot_testing_version}"
        val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${nav_version}"
        val googleServices = "com.google.gms:google-services:${google_service_version}"
        val hilt = "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
        val detektPlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:$detektVersion"
    }

    object Android {
        val compileSdkVersion = 34
        val applicationId = "com.marzec.cheatday"
        val minSdkVersion = 30
        val targetSdkVersion = 34
        val versionCode = 1
        val versionName = "1.0"
    }
    object Libs
    object TestLibs
}
