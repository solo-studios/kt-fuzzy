/*
 * Copyright (c) 2025 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PlatformExtensions.jvm.kt is part of kotlin-fuzzy
 * Last modified on 25-09-2025 08:46 p.m.
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

package ca.solostudios.fuzzykt.kotest

import ca.solostudios.fuzzykt.getEnv
import io.kotest.core.extensions.Extension
import io.kotest.extensions.htmlreporter.HtmlReporter
import io.kotest.extensions.junitxml.JunitXmlReporter

private val taskName: String
    get() = getEnv("GRADLE_TASK_NAME")!!


private val buildDir: String
    get() = getEnv("GRADLE_BUILD_DIR")!!

actual fun platformExtensions(): List<Extension> {
    return listOf(
        JunitXmlReporter(
            includeContainers = false,
            useTestPathAsName = true,
            outputDir = "test-results/$taskName"
        ),
        HtmlReporter(
            outputDir = "reports/tests/$taskName"
        )
    )
}
