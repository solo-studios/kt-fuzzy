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

import java.util.Locale
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.the

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
    val projectHost: String,
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
data class ProjectInfo(val group: String, val module: String, val version: String) {
    companion object {
        fun fromProject(project: Project): ProjectInfo {
            return ProjectInfo(
                group = project.group.toStringOrEmpty(),
                module = project.name,
                version = project.version.toStringOrEmpty(),
            )
        }
    }
}

class DokkaDirectories(val project: Project) {
    val root = project.rootProject.projectDir.resolve("dokka")

    val base = buildList {
        add(root)
        if (project.rootProject != project)
            add(project.projectDir.resolve("dokka"))
    }
    val baseOutput = project.layout.buildDirectory.dir("dokka").get()

    val includes = input("includes")
    val includesOutput = output("includes")

    val styles = input("styles")
    val stylesOutput = output("styles")

    val assets = input("assets")
    val templates = rootInput("templates")
    val scripts = input("scripts")

    fun include(name: String) = includes.map { it.resolve("_$name.md") }.first { it.exists() && it.isFile }
    fun readInclude(name: String) = include(name).readText()

    private fun input(name: String) = base.map { it.resolve(name) }
    private fun rootInput(name: String) = root.resolve(name)
    private fun output(name: String) = baseOutput.dir(name)
}
