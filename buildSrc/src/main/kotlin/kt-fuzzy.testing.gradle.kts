/*
 * Copyright (c) 2023-2025 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.testing.gradle.kts is part of kotlin-fuzzy
 * Last modified on 26-09-2025 02:00 p.m.
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
 * KOTLIN-FUZZY IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:Suppress("KotlinRedundantDiagnosticSuppress", "UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
import kotlin.math.max

plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
    id("io.kotest")
    id("io.qameta.allure-report")
    id("io.qameta.allure-adapter")
}

kotlin {
    // kotest targets jvm 11
    targets.withType<KotlinJvmTarget>().configureEach {
        compilations.named { it.contains("test", ignoreCase = true) }.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget = JvmTarget.JVM_11
                }
            }
        }
    }

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(libs.bundles.kotest)

                implementation(project(":common-test"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.runner.junit5)
            }
        }
    }

    js {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefoxHeadless()

                    useConfigDirectory(project.rootDir.resolve("karma.config.d"))
                }
            }
        }
    }
}

allure {
    version = libs.versions.allure.reporter
    adapter {
        allureJavaVersion = libs.versions.allure.reporter

        autoconfigure = false
        autoconfigureListeners = false

        frameworks {
            junit5 {
                enabled = false
            }

            junit4 {
                enabled = false
            }
        }
    }
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()

        failFast = false
        maxParallelForks = max(Runtime.getRuntime().availableProcessors() - 1, 1)

        reports {
            html.required = false
            junitXml.required = false
        }

        environment("GRADLE_TASK_NAME", name)
        systemProperty("gradle.build.dir", layout.buildDirectory.get().asFile.absolutePath)
        systemProperty("kotest.framework.config.fqn", "ca.solostudios.fuzzykt.kotest.JvmKotestConfig")
    }

    withType<KotlinNativeTest>().configureEach {
        environment("GRADLE_TASK_NAME", name)
        environment("GRADLE_BUILD_DIR", layout.buildDirectory.get().asFile)
    }

    withType<KotlinJsTest>().configureEach {
        environment("GRADLE_TASK_NAME", name)
        environment("GRADLE_BUILD_DIR", layout.buildDirectory.get().asFile.absolutePath)
    }
}
