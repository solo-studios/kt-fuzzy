/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.tasks.gradle.kts is part of kotlin-fuzzy
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

import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("multiplatform")

    id("org.jetbrains.dokka")
}

val ext = the<ExtraPropertiesExtension>()
val base = the<BasePluginExtension>()

tasks {
    withType<AbstractArchiveTask>().configureEach {
        archiveBaseName.set(project.name)
    }

    withType<Javadoc>().configureEach {
        options {
            encoding = "UTF-8"
        }
    }

    withType<Jar>().configureEach {
        from(rootProject.file("LICENSE"))
    }

    named<Task>("build") {
        dependsOn(withType<Jar>())
    }

    val dokkaHtml by getting(DokkaTask::class)

    val javadocJar by creating(Jar::class) {
        dependsOn(dokkaHtml)
        from(dokkaHtml.outputDirectory)
        archiveClassifier = "javadoc"
        group = JavaBasePlugin.DOCUMENTATION_GROUP
    }

    artifacts {
        archives(javadocJar)
    }
}
