/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2021-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of kotlin-fuzzy
 * Last modified on 22-07-2023 05:12 p.m.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * KT-FUZZY IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:Suppress("KotlinRedundantDiagnosticSuppress", "UNUSED_VARIABLE")

import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.internal.JvmPluginsHelper


plugins {
    `kt-fuzzy`.repositories
    `kt-fuzzy`.compilation
    `kt-fuzzy`.tasks
    `kt-fuzzy`.publishing
    `kt-fuzzy`.dokka
    `kt-fuzzy`.testing
    `kt-fuzzy`.versioning
}

group = "ca.solo-studios"
description = """
    A dependency-less Kotlin Multiplatform library for fuzzy string matching
""".trimIndent()

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.stdlib)

                api(projects.ktStringSimilarity)
            }
        }
    }
}

// afterEvaluate {
//     println("source sets=${sourceSets.asMap}")
//
//     project.configure<JavaPluginExtension> {
//         // withSourcesJar()
//         val main = sourceSets.getByName("commonMain")
//         JvmPluginsHelper.createDocumentationVariantWithArtifact(
//                 main.sourcesElementsConfigurationName,
//                 null,
//                 DocsType.SOURCES,
//                 listOf(),
//                 main.sourcesJarTaskName,
//                 main.allSource,
//                 project as ProjectInternal
//                                                                )
//
//         // withJavadocJar()
//     }
// }
