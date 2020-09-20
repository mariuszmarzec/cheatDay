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
}

android {
    compileSdkVersion(Config.Android.compileSdkVersion)
    defaultConfig {
        applicationId = Config.Android.applicationId
        minSdkVersion(Config.Android.minSdkVersion)
        targetSdkVersion(Config.Android.targetSdkVersion)
        versionCode = Config.Android.versionCode
        versionName = Config.Android.versionName
        testInstrumentationRunner = "com.marzec.cheatday.MockTestRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf(
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Config.kotlin_version}")
    implementation("androidx.appcompat:appcompat:${Config.appcompat_version}")
    implementation("androidx.core:core-ktx:${Config.core_ktx_version}")
    implementation("androidx.constraintlayout:constraintlayout:${Config.constraint_layout_version}")
    implementation("com.squareup.retrofit2:retrofit:${Config.retrofit_version}")
    implementation("io.reactivex.rxjava2:rxjava:${Config.rxjava_version}")
    implementation("com.squareup.retrofit2:adapter-rxjava2:${Config.retrofit_version}")
    implementation("com.google.code.gson:gson:${Config.gson_version}")
    implementation("com.squareup.okhttp3:okhttp:${Config.okhttp_version}")
    implementation("com.squareup.retrofit2:converter-gson:${Config.retrofit_version}")
    implementation("io.reactivex.rxjava2:rxandroid:${Config.rxandroid_version}")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("org.jetbrains.anko:anko-commons:${Config.anko_version}")
    implementation("net.danlew:android.joda:${Config.joda_version}")
    implementation("android.arch.lifecycle:viewmodel:${Config.viewmodel_version}")
    implementation("android.arch.lifecycle:reactivestreams:${Config.reactivestreams_version}")
    implementation("androidx.lifecycle:lifecycle-livedata:${Config.livedata_version}")
    implementation("androidx.room:room-runtime:${Config.room_version}")
    implementation("androidx.room:room-ktx:${Config.room_version}")
    implementation("androidx.room:room-rxjava2:${Config.room_version}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Config.ktx_version}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Config.ktx_version}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Config.ktx_version}")
    implementation("com.google.android.material:material:${Config.material_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${Config.coroutines_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Config.coroutines_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Config.coroutines_version}")
    implementation("com.airbnb.android:paris:${Config.paris_version}")
    implementation("com.airbnb.android:epoxy:${Config.epoxy_version}")
    implementation("com.google.dagger:dagger:${Config.dagger_version}")
    implementation("com.google.dagger:dagger-android:${Config.dagger_version}")
    implementation("com.google.dagger:dagger-android-support:${Config.dagger_version}")
    implementation("androidx.preference:preference-ktx:${Config.preferences_ktx_version}")
    implementation("com.f2prateek.rx.preferences2:rx-preferences:${Config.rx_preferences_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Config.coroutines_version}")
    implementation("androidx.fragment:fragment:${Config.fragment_version}")
    implementation("androidx.activity:activity:${Config.activity_version}")
    implementation("com.squareup.inject:assisted-inject-annotations-dagger2:0.5.2")
    kapt("com.squareup.inject:assisted-inject-processor-dagger2:0.5.2")
    implementation("androidx.navigation:navigation-fragment-ktx:${Config.nav_version}")
    implementation("androidx.navigation:navigation-ui-ktx:${Config.nav_version}")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:${Config.nav_version}")
    debugImplementation("androidx.fragment:fragment-testing:${Config.fragment_version}")
    androidTestImplementation("androidx.navigation:navigation-testing:${Config.nav_version}")
    kapt("com.airbnb.android:epoxy-processor:${Config.epoxy_version}")
    kapt( "com.google.dagger:dagger-compiler:${Config.dagger_version}")
    kapt("com.google.dagger:dagger-android-processor:${Config.dagger_version}")
    kapt("androidx.room:room-compiler:${Config.room_version}")
    kapt("com.airbnb.android:paris-processor:${Config.paris_version}")
    testImplementation("junit:junit:4.12")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Config.junit5_version}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Config.junit5_version}")
    testImplementation("org.mockito:mockito-core:${Config.mockito_version}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Config.mockito_kotlin_version}")
    testImplementation("joda-time:joda-time:${Config.joda_version}")
    kaptAndroidTest("com.airbnb.android:epoxy-processor:${Config.epoxy_version}")
    kaptAndroidTest( "com.google.dagger:dagger-compiler:${Config.dagger_version}")
    kaptAndroidTest("com.google.dagger:dagger-android-processor:${Config.dagger_version}")
    kaptAndroidTest("androidx.room:room-compiler:${Config.room_version}")
    kaptAndroidTest("com.airbnb.android:paris-processor:${Config.paris_version}")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Config.kotlin_version}")
    androidTestImplementation("androidx.appcompat:appcompat:${Config.appcompat_version}")
    androidTestImplementation("androidx.core:core-ktx:${Config.core_ktx_version}")
    androidTestImplementation("androidx.constraintlayout:constraintlayout:${Config.constraint_layout_version}")
    androidTestImplementation("com.squareup.retrofit2:retrofit:${Config.retrofit_version}")
    androidTestImplementation("io.reactivex.rxjava2:rxjava:${Config.rxjava_version}")
    androidTestImplementation("com.squareup.retrofit2:adapter-rxjava2:${Config.retrofit_version}")
    androidTestImplementation("com.google.code.gson:gson:${Config.gson_version}")
    androidTestImplementation("com.squareup.okhttp3:okhttp:${Config.okhttp_version}")
    androidTestImplementation("com.squareup.retrofit2:converter-gson:${Config.retrofit_version}")
    androidTestImplementation("io.reactivex.rxjava2:rxandroid:${Config.rxandroid_version}")
    androidTestImplementation("androidx.recyclerview:recyclerview:1.1.{Config.0}")
    androidTestImplementation("org.jetbrains.anko:anko-commons:${Config.anko_version}")
    androidTestImplementation("androidx.lifecycle:lifecycle-livedata:${Config.livedata_version}")
    androidTestImplementation("androidx.room:room-runtime:${Config.room_version}")
    androidTestImplementation("androidx.room:room-ktx:${Config.room_version}")
    androidTestImplementation("androidx.room:room-rxjava2:${Config.room_version}")
    androidTestImplementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Config.ktx_version}")
    androidTestImplementation("androidx.lifecycle:lifecycle-runtime-ktx:${Config.ktx_version}")
    androidTestImplementation("androidx.lifecycle:lifecycle-livedata-ktx:${Config.ktx_version}")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${Config.coroutines_version}")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Config.coroutines_version}")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Config.coroutines_version}")
    androidTestImplementation("com.airbnb.android:paris:${Config.paris_version}")
    androidTestImplementation("com.airbnb.android:epoxy:${Config.epoxy_version}")
    androidTestImplementation("com.google.dagger:dagger:${Config.dagger_version}")
    androidTestImplementation("com.google.dagger:dagger-android:${Config.dagger_version}")
    androidTestImplementation("com.google.dagger:dagger-android-support:${Config.dagger_version}")
    androidTestImplementation("androidx.preference:preference-ktx:${Config.preferences_ktx_version}")
    androidTestImplementation("com.f2prateek.rx.preferences2:rx-preferences:${Config.rx_preferences_version}")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Config.coroutines_version}")
    androidTestImplementation("io.reactivex.rxjava2:rxandroid:${Config.rxandroid_version}")
    androidTestImplementation("org.mockito:mockito-android:${Config.mockito_version}")
    androidTestImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Config.mockito_kotlin_version}")
    androidTestImplementation("androidx.test.ext:junit:${Config.android_test_runner_version}")
    androidTestImplementation("androidx.test:rules:${Config.android_test_version}")
    androidTestImplementation("androidx.arch.core:core-testing:${Config.arch_testing_version}")
    androidTestImplementation("junit:junit:4.12")
    androidTestImplementation("com.squareup.rx.idler:rx2-idler:${Config.idler_version}")
    androidTestImplementation("androidx.room:room-testing:${Config.room_version}")
    androidTestImplementation("androidx.test:runner:${Config.android_test_version}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Config.espresso_core}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Config.espresso_core}")
    androidTestImplementation("androidx.fragment:fragment-testing:${Config.fragment_version}")
    androidTestImplementation("com.agoda.kakao:kakao:${Config.kakao_version}")
}
