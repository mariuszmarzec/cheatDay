import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android-extensions")
    id("kotlin-allopen")
    id("de.mannodermaus.android-junit5")
    id("com.facebook.testing.screenshot")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Dependency.Android.compileSdkVersion)
    defaultConfig {
        applicationId = Dependency.Android.applicationId
        minSdkVersion(Dependency.Android.minSdkVersion)
        targetSdkVersion(Dependency.Android.targetSdkVersion)
        versionCode = Dependency.Android.versionCode
        versionName = Dependency.Android.versionName
        testInstrumentationRunner = "com.marzec.cheatday.MockTestRunner"
        testInstrumentationRunnerArgument("listener", "leakcanary.FailTestOnLeakRunListener")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = arguments + mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }

        compileOptions {
            setSourceCompatibility(JavaVersion.VERSION_1_8)
            setTargetCompatibility(JavaVersion.VERSION_1_8)
        }

        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }

    signingConfigs {
        create("release") {
            val properties = Properties()
            val propertiesFile = File("local.properties")
            if (propertiesFile.exists()) {
                properties.load(FileInputStream(propertiesFile))
                storeFile = file(properties.getProperty("storeFile"))
                keyAlias = properties.getProperty("keyAlias")
                storePassword = properties.getProperty("storePassword")
                keyPassword = properties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures.dataBinding = true

    testOptions.animationsDisabled = true
    testOptions.unitTests.isIncludeAndroidResources = true

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

kapt {
    correctErrorTypes = true

    arguments {
        arg("dagger.experimentalDaggerErrorMessages", "enabled")
    }
}

allOpen {
    annotation("com.marzec.cheatday.OpenClass")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Dependency.leak_canary_version}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Dependency.kotlin_version}")
    implementation("androidx.appcompat:appcompat:${Dependency.appcompat_version}")
    implementation("androidx.core:core-ktx:${Dependency.core_ktx_version}")
    implementation("androidx.constraintlayout:constraintlayout:${Dependency.constraint_layout_version}")
    implementation("com.squareup.retrofit2:retrofit:${Dependency.retrofit_version}")
    implementation("com.google.code.gson:gson:${Dependency.gson_version}")
    implementation("com.squareup.okhttp3:okhttp:${Dependency.okhttp_version}")
    implementation("com.squareup.retrofit2:converter-gson:${Dependency.retrofit_version}")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("org.jetbrains.anko:anko-commons:${Dependency.anko_version}")
    implementation("net.danlew:android.joda:${Dependency.joda_version}")
    implementation("android.arch.lifecycle:viewmodel:${Dependency.viewmodel_version}")
    implementation("android.arch.lifecycle:reactivestreams:${Dependency.reactivestreams_version}")
    implementation("androidx.lifecycle:lifecycle-livedata:${Dependency.livedata_version}")
    implementation("androidx.room:room-runtime:${Dependency.room_version}")
    implementation("androidx.room:room-ktx:${Dependency.room_version}")
    implementation("androidx.room:room-rxjava2:${Dependency.room_version}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Dependency.ktx_version}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Dependency.ktx_version}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Dependency.ktx_version}")
    implementation("com.google.android.material:material:${Dependency.material_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${Dependency.coroutines_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependency.coroutines_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Dependency.coroutines_version}")
    implementation("com.airbnb.android:paris:${Dependency.paris_version}")
    implementation("com.airbnb.android:epoxy:${Dependency.epoxy_version}")
    implementation("androidx.preference:preference-ktx:${Dependency.preferences_ktx_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependency.coroutines_version}")
    implementation("androidx.fragment:fragment:${Dependency.fragment_version}")
    implementation("androidx.activity:activity:${Dependency.activity_version}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Dependency.nav_version}")
    implementation("androidx.navigation:navigation-ui-ktx:${Dependency.nav_version}")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:${Dependency.nav_version}")
    implementation("com.github.PhilJay:MPAndroidChart:${Dependency.chart_version}")
    implementation("androidx.datastore:datastore-preferences:${Dependency.datastore_version}")
    debugImplementation("androidx.fragment:fragment-testing:${Dependency.fragment_version}")
    androidTestImplementation("androidx.navigation:navigation-testing:${Dependency.nav_version}")
    implementation("com.google.dagger:hilt-android:${Dependency.hilt_plugin}")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:${Dependency.hilt_compiler_version}")
    kapt("androidx.hilt:hilt-compiler:${Dependency.hilt_compiler_version}")
    kapt("com.google.dagger:hilt-android-compiler:${Dependency.hilt_plugin}")
    kapt("com.airbnb.android:epoxy-processor:${Dependency.epoxy_version}")
    kapt("androidx.room:room-compiler:${Dependency.room_version}")
    kapt("com.airbnb.android:paris-processor:${Dependency.paris_version}")
    testImplementation("junit:junit:${Dependency.junit4_version}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Dependency.junit5_version}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Dependency.junit5_version}")
    testImplementation("org.mockito:mockito-core:${Dependency.mockito_version}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Dependency.mockito_kotlin_version}")
    testImplementation("joda-time:joda-time:${Dependency.joda_version}")
    kaptAndroidTest("com.airbnb.android:epoxy-processor:${Dependency.epoxy_version}")
    kaptAndroidTest("androidx.room:room-compiler:${Dependency.room_version}")
    kaptAndroidTest("com.airbnb.android:paris-processor:${Dependency.paris_version}")
    androidTestImplementation("com.squareup.leakcanary:leakcanary-android-instrumentation:${Dependency.leak_canary_version}")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependency.coroutines_version}")
    androidTestImplementation("org.mockito:mockito-android:${Dependency.mockito_version}")
    androidTestImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Dependency.mockito_kotlin_version}")
    androidTestImplementation("androidx.test.ext:junit:${Dependency.android_test_runner_version}")
    androidTestImplementation("androidx.test:rules:${Dependency.android_test_version}")
    androidTestImplementation("androidx.arch.core:core-testing:${Dependency.arch_testing_version}")
    androidTestImplementation("junit:junit:${Dependency.junit4_version}")
    androidTestImplementation("com.squareup.rx.idler:rx2-idler:${Dependency.idler_version}")
    androidTestImplementation("androidx.room:room-testing:${Dependency.room_version}")
    androidTestImplementation("androidx.test:runner:${Dependency.android_test_version}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Dependency.espresso_core}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Dependency.espresso_core}")
    androidTestImplementation("androidx.fragment:fragment-testing:${Dependency.fragment_version}")
    androidTestImplementation("com.agoda.kakao:kakao:${Dependency.kakao_version}")
}