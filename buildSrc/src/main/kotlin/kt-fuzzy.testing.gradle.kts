/*
 * kotlin-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.testing.gradle.kts is part of kotlin-fuzzy
 * Last modified on 16-07-2023 04:23 p.m.
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

import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsSubTargetDsl
import kotlin.math.max

plugins {
    kotlin("multiplatform")
    id("io.kotest.multiplatform")
}

kotlin {
    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(libs.kotest.framework)
                implementation(libs.kotest.assertions)
                implementation(libs.kotest.property)
                // implementation(kotlin("test"))
                // implementation(kotlin("test-annotations-common"))
                // implementation(kotlin("test-common"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:5.6.2")
            }
        }
    }

    js(IR) {
        nodejs {
            configureTests()
        }
        browser {
            configureTests()
        }
    }
}

fun KotlinJsSubTargetDsl.configureTests() {
    testTask {
        useMocha()
    }
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()

        failFast = false
        maxParallelForks = max(Runtime.getRuntime().availableProcessors() - 1, 1)
    }
}
