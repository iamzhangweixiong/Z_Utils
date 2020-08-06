package com.test.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("wilson") {
            it.doLast {
                println(" ==================== do wilson task ==================== ")
            }
        }
    }
}