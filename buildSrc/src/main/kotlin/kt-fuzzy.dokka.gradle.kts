/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.dokka.gradle.kts is part of kotlin-fuzzy
 * Last modified on 16-09-2023 04:49 p.m.
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

import ca.solostudios.dokkascript.plugin.DokkaScriptsConfiguration
import ca.solostudios.dokkascript.plugin.DokkaScriptsPlugin
import ca.solostudios.dokkastyles.plugin.DokkaStyleTweaksConfiguration
import ca.solostudios.dokkastyles.plugin.DokkaStyleTweaksPlugin
import com.sass_lang.embedded_protocol.OutputStyle
import io.freefair.gradle.plugins.sass.SassCompile
import java.time.Year
import org.apache.tools.ant.filters.ReplaceTokens
import org.intellij.lang.annotations.Language
import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.AbstractDokkaTask
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("org.jetbrains.dokka")
    id("io.freefair.sass-base")
}

dependencies {
    dokkaPlugin(libs.dokka.plugin.script)
    dokkaPlugin(libs.dokka.plugin.style.tweaks)
}

sass {
    omitSourceMapUrl = true
    outputStyle = OutputStyle.COMPRESSED
    sourceMapContents = false
    sourceMapEmbed = false
    sourceMapEnabled = false
}

tasks {
    val rootDokkaDirectory = rootProject.projectDir.resolve("dokka")
    val dokkaDirectories = if (project.rootProject != project)
        listOf(rootDokkaDirectory, project.projectDir.resolve("dokka"))
    else
        listOf(rootDokkaDirectory)

    val processDokkaIncludes by register<ProcessResources>("processDokkaIncludes") {
        from(dokkaDirectories.map { it.resolve("includes") }) {
            exclude { it.name.startsWith("_") }
        }

        doFirst {
            val projectInfo = ProjectInfo(
                    group = project.group.toStringOrEmpty(),
                    module = project.name,
                    version = project.version.toStringOrEmpty(),
                                         )

            val including = rootDokkaDirectory.resolve("includes/_including.md").readText()

            filter<ReplaceTokens>("tokens" to mapOf("including" to including), "beginToken" to "[%%", "endToken" to "%%]")

            @Language("HTML")
            val stringReplacements = listOf(
                    "☐" to """<input type="checkbox" readonly>""",
                    "☒" to """<input type="checkbox" readonly checked>""",
                    "- [x]" to """- <input class="checklist-item" type="checkbox" readonly>"""
                                           )

            @Language("RegExp")
            val regexReplacements = listOf(
                    "\\[@ft-(\\w+)\\]".toRegex() to """<sup class="footnote"><a href="#footnote-$1">&#91;$1&#93;</a></sup>""",
                    "\\[@ref-(\\d+)\\]".toRegex() to """<sup class="reference"><a href="#reference-$1">&#91;$1&#93;</a></sup>""",
                                          )

            filter { sourceLine ->
                stringReplacements.fold(sourceLine) { line, (old, new) ->
                    line.replace(old, new)
                }.let { processedLine ->
                    regexReplacements.fold(processedLine) { line, (regex, replacement) ->
                        line.replace(regex, replacement)
                    }
                }
            }

            expand("project" to projectInfo)
        }

        destinationDir = layout.buildDirectory.dir("dokka/includes").get().asFile
        group = JavaBasePlugin.DOCUMENTATION_GROUP
    }

    val compileDokkaSass by register<SassCompile>("compileDokkaSass") {
        group = BasePlugin.BUILD_GROUP
        source = fileTree(rootDokkaDirectory.resolve("styles"))
        destinationDir = layout.buildDirectory.dir("dokka/styles")
    }

    withType<AbstractDokkaTask>().configureEach {
        inputs.files(rootDokkaDirectory)

        dependsOn(compileDokkaSass)

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "© ${Year.now()} Copyright solo-studios"
            separateInheritedMembers = false

            // Evil bullshit
            val rootStyles = rootDokkaDirectory.resolve("styles").listFiles { file -> file.extension == "css" }?.toList().orEmpty()
            val compiledStyles = rootDokkaDirectory.resolve("styles").listFiles { file ->
                file.extension == "scss" && !file.name.startsWith("_")
            }?.map {
                layout.buildDirectory.file("dokka/styles/${it.nameWithoutExtension}.css").get().asFile
            }.orEmpty()

            customStyleSheets = rootStyles + compiledStyles
            customAssets = rootDokkaDirectory.resolve("assets").listFiles()?.toList().orEmpty()
            templatesDir = rootDokkaDirectory.resolve("templates")
        }
        pluginConfiguration<DokkaScriptsPlugin, DokkaScriptsConfiguration> {
            scripts = rootDokkaDirectory.resolve("scripts").listFiles()?.toList().orEmpty()
            remoteScripts = listOf(
                    // MathJax
                    "https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.6/MathJax.js?config=TeX-AMS-MML_HTMLorMML&latest",
                                  )
        }
        pluginConfiguration<DokkaStyleTweaksPlugin, DokkaStyleTweaksConfiguration> {
            minimalScrollbar = true
            darkPurpleHighlight = true
            darkColorSchemeFix = true
            improvedBlockquoteBorder = true
            lighterBlockquoteText = true
            sectionTabFontWeight = "500"
            sectionTabTransition = true
            improvedSectionTabBorder = true
            disableCodeWrapping = true
            sidebarWidth = "340px"
        }

        moduleName = project.name
        moduleVersion = project.version.toStringOrEmpty()
    }

    withType<DokkaMultiModuleTask>().configureEach {
        if (project.rootProject == project)
            moduleName = project.name.formatAsName()
        else
            isEnabled = false
    }


    withType<DokkaTask>().configureEach {
        dependsOn(processDokkaIncludes)

        inputs.files(dokkaDirectories)

        dokkaSourceSets.configureEach {
            includes.from(processDokkaIncludes.outputs.files.asFileTree)

            reportUndocumented = true
            documentedVisibilities = setOf(Visibility.PUBLIC, Visibility.PROTECTED)
        }
    }

    withType<DokkaTaskPartial>().configureEach {
        dependsOn(processDokkaIncludes)

        inputs.files(dokkaDirectories)

        dokkaSourceSets.configureEach {
            includes.from(processDokkaIncludes.outputs.files.asFileTree)

            reportUndocumented = true
        }
    }
}
