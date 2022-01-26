object Dependency {

    val crashlytics_version = "2.4.1"
    val firebase_version = "26.3.0"
    val kotlin_version = "1.6.10"
    val junit4_version = "4.13"
    val leak_canary_version = "2.7"
    val coroutines_version = "1.6.0"
    val appcompat_version = "1.1.0"
    val material_version = "1.2.0"
    val ktx_version = "2.3.0-alpha02"
    val core_ktx_version = "1.2.0"
    val constraint_layout_version = "1.1.3"
    val retrofit_version = "2.9.0"
    val gson_version = "2.8.5"
    val okhttp_version = "4.9.0"
    val anko_version = "0.10.8"
    val junit5_version = "5.7.1"
    val joda_version = "2.10.3"
    val truth_version = "1.1.3"
    val viewmodel_version = "1.1.1"
    val room_version = "2.3.0"
    val android_test_version = "1.1.3"
    val android_test_services_version = "1.4.1"
    val android_test_runner_version = "1.1.3"
    val runner_version = "1.3.0"
    val orchestrator_version = "1.4.1"
    val paris_version = "1.3.0"
    val arch_testing_version = "2.1.0"
    val epoxy_version = "3.11.0"
    val espresso_core = "3.3.0"
    val fragment_version = "1.3.2"
    val activity_version = "1.1.0"
    val screenshot_testing_version = "5.13.0"
    val kakao_version = "2.3.4"
    val nav_version = "2.3.0"
    val chart_version = "v3.1.0"
    val junit5_plugin_version = "1.7.1.1"
    val android_gradle_plugin = "7.2.0-alpha07"
    val datastore_version = "1.0.0-alpha05"
    val google_service_version = "4.3.4"
    val protobuf_version = "0.8.14"
    val hilt_version = "2.40.5"
    val mockk_version = "1.11.0"
    val kaspresso_version = "1.3.0"
    val detektVersion = "1.18.1"
    val androidTestMonitorVersion = "1.4.0"
    val hamcrestVersion = "1.3"

    object BuildPlugins {
        val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:${crashlytics_version}"
        val androidBuildPlugin = "com.android.tools.build:gradle:${android_gradle_plugin}"
        val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}"
        val jUnit5Plugin = "de.mannodermaus.gradle.plugins:android-junit5:${junit5_plugin_version}"
        val kotlinAllOpen = "org.jetbrains.kotlin:kotlin-allopen:${kotlin_version}"
        val screenshots = "com.karumi:shot:${screenshot_testing_version}"
        val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${nav_version}"
        val googleServices = "com.google.gms:google-services:${google_service_version}"
        val protobuf = "com.google.protobuf:protobuf-gradle-plugin:${protobuf_version}"
        val hilt = "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
        val detektPlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:$detektVersion"
    }

    object Android {
        val compileSdkVersion = 32
        val applicationId = "com.marzec.cheatday"
        val minSdkVersion = 28
        val targetSdkVersion = 32
        val versionCode = 1
        val versionName = "1.0"
    }
    object Libs
    object TestLibs
}
