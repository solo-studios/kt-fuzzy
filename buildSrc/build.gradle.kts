/*
 * Copyright (c) 2025 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of kotlin-fuzzy
 * Last modified on 29-09-2025 07:57 p.m.
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

import ca.solostudios.nyx.util.soloStudios
import ca.solostudios.nyx.util.soloStudiosSnapshots

plugins {
    `kotlin-dsl`
    alias(libs.plugins.nyx)
}

repositories {
    mavenCentral()
    // for kotlin-dsl plugin
    gradlePluginPortal()

    soloStudios()
    soloStudiosSnapshots()

    // for android
    google()
}

nyx {
    compile {
        jvmToolchain = 17

        kotlin {
            apiVersion = "2.0"
            languageVersion = "2.0"
        }
    }
}

dependencies {
    implementation(libs.dokka.base)

    implementation(libs.dokka.plugin.script)
    implementation(libs.dokka.plugin.style.tweaks)


    implementation(libs.plugins.kotest.toDependency())

    implementation(libs.plugins.ksp.toDependency())

    implementation(libs.plugins.allure.report.toDependency())
    implementation(libs.plugins.allure.adapter.toDependency())

    implementation(libs.plugins.dokka.toDependency())
    implementation(libs.plugins.kotlin.multiplatform.toDependency())
    implementation(libs.plugins.kotlin.plugin.allopen.toDependency())

    implementation(libs.plugins.nyx.toDependency())

    implementation(libs.plugins.axion.release.toDependency())

    implementation(libs.plugins.kotlinx.benchmark.toDependency())

    implementation(libs.plugins.sass.base.toDependency())

    implementation(libs.plugins.android.library.toDependency())

    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

fun Provider<PluginDependency>.toDependency(): Provider<ExternalModuleDependency> = map { plugin ->
    val pluginId = plugin.pluginId
    val version = plugin.version

    return@map dependencyFactory.create(pluginId, "$pluginId.gradle.plugin", null).apply {
        version {
            branch = version.branch
            strictly(version.strictVersion)
            require(version.requiredVersion)
            prefer(version.preferredVersion)
            reject(*version.rejectedVersions.toTypedArray())
        }
    }
}
