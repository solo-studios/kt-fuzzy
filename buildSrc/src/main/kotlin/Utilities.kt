/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Utilities.kt is part of kotlin-fuzzy
 * Last modified on 31-07-2023 05:48 p.m.
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

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinJsOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetHierarchyDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary
import java.util.Locale

fun String.capitalize(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun Any?.toStringOrEmpty() = this as? String ?: this?.toString() ?: ""

fun String.formatAsName(): String {
    return this.split("-").map {
        it.takeIf { it != "kt" } ?: "Kotlin"
    }.joinToString(separator = " ") { it.capitalize() }
}

val Project.isSnapshot: Boolean
    get() = version.toString().endsWith("-SNAPSHOT")

// https://github.com/gradle/gradle/issues/15383
val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

data class Repository(
    val projectUser: String,
    val projectRepo: String,
    val projectHost: String = "github.com",
) {
    val projectBaseUri: String
        get() = "$projectHost/$projectUser/$projectRepo"
    val projectUrl: String
        get() = "https://$projectBaseUri"
}

var Project.repository: Repository
    get() = rootProject.extra["repo"] as Repository
    set(value) {
        rootProject.extra["repo"] = value
    }

/**
 * Project info class for the `processDokkaIncludes` task.
 */
data class ProjectInfo(val group: String, val module: String, val version: String)

/*
 Magic shit
 */
fun KotlinJsTargetDsl.configureCommonJs() {
    compilations.configureEach {
        kotlinOptions.configureCommonJsOptions()

        binaries.withType<JsIrBinary>().configureEach {
            linkTask.configure {
                kotlinOptions.configureCommonJsOptions()
            }
        }
    }
}

/*-------------------------------------------------------------------+
 |                                                                   |
 |      Implementations of useCommonJs(), useEsModules(), and        |
 | generateTypeScriptDefinitions() that use lazy task configuration. |
 |                                                                   |
 +-------------------------------------------------------------------*/

fun KotlinJsTargetDsl.configureEsModules() {
    compilations.configureEach {
        kotlinOptions.configureEsModulesOptions()

        binaries.withType<JsIrBinary>().configureEach {
            linkTask.configure {
                kotlinOptions.configureEsModulesOptions()
            }
        }
    }
}

fun KotlinJsTargetDsl.configureGenerateTypeScriptDefinitions() {
    compilations.configureEach {
        binaries.withType<JsIrBinary>().configureEach {
            generateTs = true
            linkTask.configure {
                compilerOptions.freeCompilerArgs.add(CompilerFlags.GENERATE_D_TS)
            }
        }
    }
}

private fun KotlinJsOptions.configureCommonJsOptions() {
    moduleKind = "commonjs"
    sourceMap = true
    sourceMapEmbedSources = "never"
}

private fun KotlinJsOptions.configureEsModulesOptions() {
    moduleKind = "es"
    sourceMap = true
    sourceMapEmbedSources = "never"
}

@ExperimentalKotlinGradlePluginApi
fun KotlinMultiplatformExtension.targetHierarchy(configure: KotlinTargetHierarchyDsl.() -> Unit) {
    targetHierarchy.configure()
}

/**
 * [org.jetbrains.kotlin.gradle.targets.js.ir.ENTRY_IR_MODULE]
 */
internal object CompilerFlags {
    internal const val ENTRY_IR_MODULE = "-Xinclude"

    internal const val DISABLE_PRE_IR = "-Xir-only"
    internal const val ENABLE_DCE = "-Xir-dce"

    internal const val GENERATE_D_TS = "-Xgenerate-dts"

    internal const val PRODUCE_JS = "-Xir-produce-js"
    internal const val PRODUCE_UNZIPPED_KLIB = "-Xir-produce-klib-dir"
    internal const val PRODUCE_ZIPPED_KLIB = "-Xir-produce-klib-file"

    internal const val MINIMIZED_MEMBER_NAMES = "-Xir-minimized-member-names"

    internal const val KLIB_MODULE_NAME = "-Xir-module-name"

    internal const val PER_MODULE = "-Xir-per-module"
    internal const val PER_MODULE_OUTPUT_NAME = "-Xir-per-module-output-name"

    internal const val WASM_BACKEND = "-Xwasm"
}
