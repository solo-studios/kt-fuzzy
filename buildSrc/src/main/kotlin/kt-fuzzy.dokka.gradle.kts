/*
 * Copyright (c) 2023-2025 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.dokka.gradle.kts is part of kotlin-fuzzy
 * Last modified on 22-09-2025 02:39 a.m.
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

@file:Suppress("serial")

import com.sass_lang.embedded_protocol.OutputStyle
import gradle.kotlin.dsl.accessors._731353d3330d41c97ba54bd629a1162c.nyx
import io.freefair.gradle.plugins.sass.SassCompile
import org.apache.tools.ant.filters.ReplaceTokens
import org.intellij.lang.annotations.Language
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.dokka.gradle.engine.plugins.DokkaPluginParametersBaseSpec
import org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask
import java.time.Year

plugins {
    id("org.jetbrains.dokka")
    id("io.freefair.sass-base")
    id("ca.solo-studios.nyx")
    id("pl.allegro.tech.build.axion-release")
}

nyx {
    compile {
        withJavadocJar()
    }
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

val dokkaDirs = DokkaDirectories(project)

val processDokkaIncludes by tasks.registering(ProcessResources::class) {
    from(dokkaDirs.includes) {
        exclude { it.name.startsWith("_") }
    }

    doFirst {
        val dependency = dokkaDirs.readInclude("dependency")
        filter<ReplaceTokens>("tokens" to mapOf("dependencies" to dependency), "beginToken" to "{{", "endToken" to "}}")
        filter(::includesLineTransformer)
        expand("project" to ProjectInfo.fromProject(project))
    }

    into(dokkaDirs.includesOutput)
    group = JavaBasePlugin.DOCUMENTATION_GROUP
}


val compileDokkaSass by tasks.registering(SassCompile::class) {
    group = BasePlugin.BUILD_GROUP
    source = files(dokkaDirs.styles).asFileTree
    destinationDir = dokkaDirs.stylesOutput
}

dokka {
    moduleName = nyx.info.name
    moduleVersion = nyx.info.version
    dokkaSourceSets.configureEach {
        includes.from(dokkaDirs.includesOutput.asFileTree.files)
        reportUndocumented = true
        documentedVisibilities = setOf(VisibilityModifier.Public, VisibilityModifier.Protected)

        // sourceLink {
        //     localDirectory = projectDir.resolve("src")
        //     remoteUrl = nyx.info.repository.projectUrl.map { uri("$it/blob/${scmVersion.scmPosition.revision}/src") }
        //     remoteLineSuffix = "#L"
        // }
    }

    pluginsConfiguration {
        html {
            homepageLink = nyx.info.repository.projectUrl
            footerMessage = "© ${Year.now()} Copyright solo-studios"
            separateInheritedMembers = false

            val rootStyles = dokkaDirs.styles.flatMap { it.walk().filter { file -> file.extension == "css" } }

            customStyleSheets.from(fileTree(compileDokkaSass.flatMap { it.destinationDir }), rootStyles)
            customAssets.from(dokkaDirs.assets.flatMap { it.walk() })
            templatesDir = dokkaDirs.templates
        }

        registerBinding(DokkaScriptsPluginParameters::class, DokkaScriptsPluginParameters::class)

        register<DokkaScriptsPluginParameters>("dokkaScripts") {
            scripts.from(dokkaDirs.scripts.flatMap { it.listFiles().orEmpty().toList() })
            remoteScripts = listOf(
                // MathJax
                "https://cdnjs.cloudflare.com/ajax/libs/mathjax/4.0.0/tex-chtml.js",
            )
        }

        registerBinding(DokkaStyleTweaksPluginParameters::class, DokkaStyleTweaksPluginParameters::class)

        register<DokkaStyleTweaksPluginParameters>("dokkaStyles") {
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
    }
}

abstract class DokkaScriptsPluginParameters @Inject constructor(name: String) : DokkaPluginParametersBaseSpec(
    name,
    "ca.solostudios.dokkascript.plugin.DokkaScriptsPlugin"
) {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:NormalizeLineEndings
    abstract val scripts: ConfigurableFileCollection

    @get:Input
    abstract val remoteScripts: ListProperty<String>


    override fun jsonEncode(): String {
        val scriptEntries = scripts.files.joinToString(separator = ",", prefix = "[", postfix = "]") {
            val file = it.canonicalFile.invariantSeparatorsPath
            "\"$file\""
        }
        val remoteScriptsEntries = remoteScripts.get().joinToString(separator = ",", prefix = "[", postfix = "]") {
            "\"$it\""
        }
        // language=JSON
        return """
            {
                "scripts": $scriptEntries,
                "remoteScripts": $remoteScriptsEntries
            }
        """.trimIndent()
    }
}

abstract class DokkaStyleTweaksPluginParameters @Inject constructor(name: String) : DokkaPluginParametersBaseSpec(
    name,
    "ca.solostudios.dokkastyles.plugin.DokkaStyleTweaksPlugin"
) {
    @get:Input
    abstract val darkColorSchemeFix: Property<Boolean>

    @get:Input
    abstract val darkPurpleHighlight: Property<Boolean>

    @get:Input
    abstract val disableCodeWrapping: Property<Boolean>

    @get:Input
    abstract val improvedBlockquoteBorder: Property<Boolean>

    @get:Input
    abstract val improvedSectionTabBorder: Property<Boolean>

    @get:Input
    abstract val lighterBlockquoteText: Property<Boolean>

    @get:Input
    abstract val minimalScrollbar: Property<Boolean>

    @get:Input
    abstract val sectionTabFontWeight: Property<String>

    @get:Input
    abstract val sectionTabTransition: Property<Boolean>

    @get:Input
    abstract val sidebarWidth: Property<String>

    override fun jsonEncode(): String {
        val darkColorSchemeFix = darkColorSchemeFix.getOrElse(false)
        val darkPurpleHighlight = darkPurpleHighlight.getOrElse(false)
        val disableCodeWrapping = disableCodeWrapping.getOrElse(false)
        val improvedBlockquoteBorder = improvedBlockquoteBorder.getOrElse(false)
        val improvedSectionTabBorder = improvedSectionTabBorder.getOrElse(false)
        val lighterBlockquoteText = lighterBlockquoteText.getOrElse(false)
        val minimalScrollbar = minimalScrollbar.getOrElse(false)
        val sectionTabFontWeight = sectionTabFontWeight.orNull.let { "\"$it\"" }
        val sectionTabTransition = sectionTabTransition.getOrElse(false)
        val sidebarWidth = sidebarWidth.orNull?.let { "\"$it\"" }
        // language=JSON
        return """
            {
                "darkColorSchemeFix": $darkColorSchemeFix,
                "darkPurpleHighlight": $darkPurpleHighlight,
                "disableCodeWrapping": $disableCodeWrapping,
                "improvedBlockquoteBorder": $improvedBlockquoteBorder,
                "improvedSectionTabBorder": $improvedSectionTabBorder,
                "lighterBlockquoteText": $lighterBlockquoteText,
                "minimalScrollbar": $minimalScrollbar,
                "sectionTabFontWeight": $sectionTabFontWeight,
                "sectionTabTransition": $sectionTabTransition,
                "sidebarWidth": $sidebarWidth
            }
        """.trimIndent()
    }
}

tasks {
    withType<DokkaGenerateTask>().configureEach {
        inputs.files(dokkaDirs.stylesOutput, dokkaDirs.assets, dokkaDirs.templates, dokkaDirs.scripts)

        dependsOn(compileDokkaSass, processDokkaIncludes)
    }
}

@Language("RegExp")
val stringReplacements = listOf(
    "☐" to """<input type="checkbox" readonly>""",
    "☒" to """<input type="checkbox" readonly checked>""",
    "- \\[x]" to """- <input class="checklist-item" type="checkbox" readonly>""",
    "\\[@ft-(\\w+)]" to """<sup class="footnote"><a href="#footnote-$1">&#91;$1&#93;</a></sup>""",
    "\\[@ref-(\\d+)]" to """<sup class="reference"><a href="#reference-$1">&#91;$1&#93;</a></sup>""",
).map { it.first.toRegex() to it.second }

fun includesLineTransformer(sourceLine: String): String {
    return stringReplacements.fold(sourceLine) { line, (old, new) ->
        line.replace(old, new)
    }
}
