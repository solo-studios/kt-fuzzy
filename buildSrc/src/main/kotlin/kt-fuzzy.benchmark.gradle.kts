/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.benchmark.gradle.kts is part of kotlin-fuzzy
 * Last modified on 09-08-2023 07:24 p.m.
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

import kotlinx.benchmark.gradle.JvmBenchmarkTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.allopen")

    id("org.jetbrains.kotlinx.benchmark")
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State") // Make jmh happy
}

kotlin {
    jvm {
        compilations.create("benchmarks")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val commonBenchmarks by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.kotlinx.benchmark.runtime)
            }
        }
        val jvmMain by getting {}
        val jvmBenchmarks by getting {
            dependsOn(commonBenchmarks)
            dependsOn(jvmMain)
        }
    }
}


benchmark {
    configurations {
        named("main") {
            reportFormat = "json"
            warmups = 5
            iterations = 5
            iterationTime = 5
            iterationTimeUnit = "s"
        }
    }
    targets {
        register("jvmBenchmarks") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.37"
        }
    }
}
