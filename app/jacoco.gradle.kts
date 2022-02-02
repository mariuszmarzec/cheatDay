
val kotlinTree = fileTree("${project.buildDir}/tmp/kotlin-classes/stageDebug") {
    exclude(
        "**/com/paris/**",
        "**/com/airbnb/paris/extensions/**"
    )
}

val kotlinSrc = "${project.projectDir}/src/main/java"

tasks.create("jacocoMergeTestReportStageDebug", JacocoMerge::class) {
    dependsOn("connectedStageDebugAndroidTest")
    dependsOn("testStageDebugUnitTest")

    executionData = fileTree(
        project.projectDir
    ) {
        include(
            "**/*UnitTest.exec",
            "**/*.ec"
        )
    }
}

tasks.create("jacocoTestReportStageDebug", JacocoReport::class) {
    dependsOn("jacocoMergeTestReportStageDebug")

    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.isEnabled = true
        html.destination = file("build/reports/jacoco/merged")
    }

    classDirectories.setFrom(files(kotlinTree))

    additionalSourceDirs.setFrom(files(kotlinSrc))
    sourceDirectories.setFrom(files(kotlinSrc))
    executionData.setFrom(
        fileTree(
            baseDir = project.projectDir
        ) {
            include(
                "**/jacocoMergeTestReportStageDebug.exec"
            )
            exclude(
                "**/*UnitTest.exec",
                "**/*.ec"
            )
        }
    )
}
