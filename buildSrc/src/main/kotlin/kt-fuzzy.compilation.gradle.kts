/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.compilation.gradle.kts is part of kotlin-fuzzy
 * Last modified on 07-07-2023 02:00 a.m.
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

import org.gradle.api.internal.attributes.ImmutableAttributes
import kotlin.math.max
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPom
import org.gradle.api.publish.maven.internal.publication.MavenPomInternal
import org.gradle.api.publish.maven.internal.tasks.MavenPomFileGenerator
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.publish.maven.internal.dependencies.VersionRangeMapper
import org.gradle.internal.serialization.Transient

plugins {
    kotlin("multiplatform")
}

kotlin {
    explicitApi()

    targets.all {
        compilations.all {
            kotlinOptions.freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
            kotlinOptions.apiVersion = "1.8"
            kotlinOptions.languageVersion = "1.8"
        }
    }

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()

            failFast = false
            maxParallelForks = max(Runtime.getRuntime().availableProcessors() - 1, 1)
        }
    }

    // Why is kotlin/js broken? I don't fucking know!
    // For some reason it shows errors when importing a library I definitely added as a dependency!
    // js(IR) {
    //     // useEsModules()
    //     // useCommonJs()
    //     // generateTypeScriptDefinitions()
    //
    //     // nodejs()
    //     // browser {
    //     //     testTask {
    //     //         useKarma {
    //     //             useFirefoxHeadless()
    //     //             useChromiumHeadless()
    //     //         }
    //     //     }
    //     // }
    // }


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
    watchosDeviceArm64()
    watchosSimulatorArm64()

    tvos()
    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()
}
