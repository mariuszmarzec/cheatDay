// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath(Dependency.BuildPlugins.androidBuildPlugin)
        classpath(Dependency.BuildPlugins.kotlinGradlePlugin)
        classpath(Dependency.BuildPlugins.jUnit5Plugin)
        classpath(Dependency.BuildPlugins.kotlinAllOpen)
        classpath(Dependency.BuildPlugins.screenshots)
        classpath(Dependency.BuildPlugins.navigationSafeArgs)
        classpath(Dependency.BuildPlugins.googleServices)
        classpath(Dependency.BuildPlugins.crashlytics)
        classpath(Dependency.BuildPlugins.protobuf)
        classpath(Dependency.BuildPlugins.hilt)
        classpath(Dependency.BuildPlugins.detektPlugin)
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
