// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.devtools.ksp") version "2.1.10-1.0.29" apply false
}

buildscript {

    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath(libs.plugin.androidBuild)
        classpath(libs.plugin.kotlinGradle)
        classpath(libs.plugin.googleServices)
        classpath(libs.plugin.crashlytics)
        classpath(libs.plugin.detekt)
        classpath(libs.plugin.jUnit5)
        classpath(libs.plugin.kotlinAllOpen)
        classpath(libs.plugin.screenshots)
        classpath(libs.plugin.navigationSafeArgs)
        classpath(libs.plugin.hilt)
    }
}

allprojects {
    repositories {
        google()
        mavenLocal()
        jcenter()
        maven { setUrl("https://jitpack.io") }
        mavenCentral()
    }
}

gradle.projectsEvaluated {
    tasks.withType<JavaCompile> {
        val compilerArgs = options.compilerArgs
        compilerArgs.add("-Xmaxerrs")
        compilerArgs.add("500")
    }
}

tasks.create<Delete>("clean") {
    delete = setOf(
        rootProject.buildDir
    )
}
