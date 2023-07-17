/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.compilation.gradle.kts is part of kotlin-fuzzy
 * Last modified on 17-07-2023 05:52 p.m.
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

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

/*
 * kotlin-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.compilation.gradle.kts is part of kotlin-fuzzy
 * Last modified on 16-07-2023 04:24 p.m.
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

plugins {
    kotlin("multiplatform")
}

kotlin {
    explicitApi()

    sourceSets {
        configureEach {
            languageSettings {
                optIn("-opt-in=kotlin.RequiresOptIn")
            }
        }
    }

    targets.configureEach {
        compilations.configureEach {
            kotlinOptions {
                apiVersion = "1.8"
                languageVersion = "1.8"
            }
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    targetHierarchy.default {

    }


    jvm {
        compilations.configureEach {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    // Why is kotlin/js broken? I don't fucking know!
    // For some reason it shows errors when importing a library I definitely added as a dependency!
    js(IR) {
        useCommonJs()
        generateTypeScriptDefinitions()

        nodejs()
        browser()
    }


    mingwX64()

    linuxX64()
    linuxArm64()

    macosX64()
    macosArm64()

    // TODO build android shit (idk why it doesn't work, too lazy to figure it out, the jvm source sets should be fine)
    // android()
    // androidNativeX64()
    // androidNativeX86()
    // androidNativeArm32()
    // androidNativeArm64()

    ios()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    watchos()
    watchosX64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()

    tvos()
    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()
}
