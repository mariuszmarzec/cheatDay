
val kotlinTree = fileTree("${project.buildDir}/tmp/kotlin-classes/stageDebug") {
    exclude("**/com/paris/**")
}

val kotlinSrc = "${project.projectDir}/src/main/java"

tasks.create("jacocoUnitTestReport", JacocoReport::class) {
    dependsOn("testStageDebugUnitTest")
    reports {
        xml.isEnabled = false
        csv.isEnabled = false
    }
}

tasks.create("jacocoMergeTestReport", JacocoMerge::class) {
    dependsOn("connectedStageDebugAndroidTest")
    dependsOn("jacocoUnitTestReport")

    executionData = fileTree(
        project.projectDir
    ) {
        include(
            "**/*UnitTest.exec",
            "**/*.ec"
        )
    }
}

tasks.create("jacocoTestReport", JacocoReport::class) {
    dependsOn("jacocoMergeTestReport")

    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.isEnabled = true
        html.destination = file("build/reports/jacoco/merged")
    }

    classDirectories.setFrom(files(kotlinTree))
    sourceDirectories.setFrom(files(kotlinSrc))
    executionData.setFrom(
        fileTree(
            baseDir = project.projectDir
        ) {
            include(
                "**/jacocoMergeTestReport.exec"
            )
            exclude(
                "**/*UnitTest.exec",
                "**/*.ec"
            )
        }
    )
}
