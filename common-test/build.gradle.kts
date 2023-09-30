@file:Suppress("KotlinRedundantDiagnosticSuppress", "UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode


plugins {
    `kt-fuzzy`.repositories
    `kt-fuzzy`.compilation
    `kt-fuzzy`.tasks
    `kt-fuzzy`.testing
    `kt-fuzzy`.versioning
}

group = "ca.solo-studios"
description = """
    Common test sources for kt-fuzzy and kt-string-similarity
""".trimIndent()

kotlin {
    explicitApi = ExplicitApiMode.Disabled

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.bundles.kotest)
            }
        }
    }
}
