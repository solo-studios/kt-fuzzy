/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Utilities.kt is part of kotlin-fuzzy
 * Last modified on 07-07-2023 02:01 a.m.
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

import org.gradle.api.Project
import java.util.Locale

fun String.capitalize(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun Any?.toStringOrEmpty() = this as? String ?: this?.toString() ?: ""

val Project.nameFormatted: String
    get() = project.name.split("-").joinToString(separator = " ") { word ->
        if (word == "kt")
            "Kotlin"
        else
            word.capitalize()
    }

val Project.isSnapshot: Boolean
    get() = version.toString().endsWith("-SNAPSHOT")

object Repository {
    val projectUser = "solo-studios"
    val projectRepo = "kt-fuzzy"
    val projectBaseUri = "github.com/$projectUser/$projectRepo"
    val projectUrl = "https://$projectBaseUri"
}

/**
 * Project info class for the `processDokkaIncludes` task.
 */
data class ProjectInfo(val group: String, val module: String, val version: String)
