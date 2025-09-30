/*
 * Copyright (c) 2023-2025 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.compilation.gradle.kts is part of kotlin-fuzzy
 * Last modified on 29-09-2025 09:08 p.m.
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

@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    // id("com.android.library")
    id("ca.solo-studios.nyx")
}

nyx {
    compile {
        distributeLicense = true
        withSourcesJar()

        kotlin {
            apiVersion = "2.0"
            languageVersion = "2.0"
        }
    }
}

kotlin {
    explicitApi()

    applyDefaultHierarchyTemplate()

    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }

    // Why is kotlin/js broken? I don't fucking know!
    // For some reason it refuses to run my tests!
    js(IR) {
        // TODO 2025-09-22 (solonovamax): remove both of these
        useCommonJs()
        useEsModules()

        generateTypeScriptDefinitions()

        nodejs()
        browser()
    }

    wasmJs {
        browser()
        nodejs()
        // TODO 2025-09-25 (solonovamax): The d8 target is currently broken due to an issue with kotlin's Uuid class
        // d8()
    }

    // TODO 2025-09-22 (solonovamax): enable wasm wasi target
    // wasmWasi {
    //     nodejs()
    // }

    mingwX64()

    linuxX64()
    linuxArm64()

    macosX64()
    macosArm64()

    // androidTarget() // TODO 2025-09-29 (solonovamax): Android is broken
    androidNativeX64()
    androidNativeX86()
    androidNativeArm32()
    androidNativeArm64()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    watchosX64()
    watchosArm32()
    watchosArm64()
    watchosDeviceArm64()
    watchosSimulatorArm64()

    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()
}

// TODO 2025-09-29 (solonovamax): something about AGP entirely breaks the allure gradle plugin.
//  I have no clue what it is, and absolutely zero desire to debug this right now.
//  here's a starting point if anyone else wants to try to figure this out.
// android {
//     namespace = "ca.solostudios"
//     compileSdk = 36
//     defaultConfig {
//         minSdk = 21
//         aarMetadata {
//             minCompileSdk = 21
//         }
//     }
//
//     // TODO 2025-09-29 (solonovamax): multiple product flavours breaks the allure plugin
//     // flavorDimensions("api")
//     //
//     // productFlavors {
//     //     register("minApi24") {
//     //         dimension = "api"
//     //         minSdk = 24
//     //     }
//     //
//     //     register("minApi21") {
//     //         dimension = "api"
//     //         minSdk = 21
//     //     }
//     // }
//
//     publishing {
//         multipleVariants {
//             allVariants()
//             withSourcesJar()
//             withJavadocJar()
//         }
//     }
// }
