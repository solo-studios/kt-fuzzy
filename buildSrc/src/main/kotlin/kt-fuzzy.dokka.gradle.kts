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
import org.gradle.api.internal.component.SoftwareComponentInternal
import org.gradle.jvm.tasks.Jar
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

// project.configure<JavaPluginExtension> {
//     withSourcesJar()
//     withJavadocJar()
// }

val dokkaDirs = DokkaDirectories(project)

tasks {
    val dokkaHtml by named<DokkaTask>("dokkaHtml")

    val javadocJar by register<Jar>("javadocJar") {
        dependsOn(dokkaHtml)
        from(dokkaHtml.outputDirectory)
        archiveClassifier = "javadoc"
        group = JavaBasePlugin.DOCUMENTATION_GROUP
    }

    // Here's a configuration to declare the outgoing variant
    val javadocElements by configurations.register("javadocElements") {
        description = "Declares build script outgoing variant"
        isCanBeConsumed = true
        isCanBeResolved = false
        attributes {
            // See https://docs.gradle.org/current/userguide/variant_attributes.html
            attributes {
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named(DocsType.JAVADOC))
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
            }
        }
        outgoing.artifact(javadocJar) {
            classifier = "sources"
        }
    }

    // This is probably the most non-trivial incantation: adding the variant to "java" component
    // println("components: ${components.asMap}")
    // components.withType<SoftwareComponentInternal>() {
    //     println("name =$name")
    // }
    // (components["kotlin"] as AdhocComponentWithVariants).addVariantsFromConfiguration(javadocElements) {}

    // if (project.plugins.hasPlugin("publishing"))
    //     publishing.publications.withType<MavenPublication>() {
    //         artifact(javadocJar)
    //     }

    val processDokkaIncludes by register<ProcessResources>("processDokkaIncludes") {
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

    val compileDokkaSass by register<SassCompile>("compileDokkaSass") {
        group = BasePlugin.BUILD_GROUP
        source = files(dokkaDirs.styles).asFileTree
        destinationDir = dokkaDirs.stylesOutput
    }

    withType<AbstractDokkaTask>().configureEach {
        inputs.files(dokkaDirs.stylesOutput, dokkaDirs.assets, dokkaDirs.templates, dokkaDirs.scripts)

        dependsOn(compileDokkaSass)

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "© ${Year.now()} Copyright solo-studios"
            separateInheritedMembers = false

            // Evil bullshit
            val rootStyles = dokkaDirs.styles.flatMap { it.walk().filter { file -> file.extension == "css" } }
            val compiledStyles = dokkaDirs.stylesOutput.asFile.walk().toList()

            customStyleSheets = rootStyles + compiledStyles
            customAssets = dokkaDirs.assets.flatMap { it.walk() }
            templatesDir = dokkaDirs.templates
        }
        pluginConfiguration<DokkaScriptsPlugin, DokkaScriptsConfiguration> {
            scripts = dokkaDirs.scripts.flatMap { it.listFiles().orEmpty().toList() }
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

        dokkaSourceSets.configureEach {
            includes.from(dokkaDirs.includesOutput.asFileTree)

            reportUndocumented = true
            documentedVisibilities = setOf(Visibility.PUBLIC, Visibility.PROTECTED)
        }
    }

    withType<DokkaTaskPartial>().configureEach {
        dependsOn(processDokkaIncludes)

        dokkaSourceSets.configureEach {
            includes.from(dokkaDirs.includesOutput)

            reportUndocumented = true
        }
    }
}

@Language("RegExp")
val stringReplacements = listOf(
        "☐" to """<input type="checkbox" readonly>""",
        "☒" to """<input type="checkbox" readonly checked>""",
        "- \\[x\\]" to """- <input class="checklist-item" type="checkbox" readonly>""",
        "\\[@ft-(\\w+)\\]" to """<sup class="footnote"><a href="#footnote-$1">&#91;$1&#93;</a></sup>""",
        "\\[@ref-(\\d+)\\]" to """<sup class="reference"><a href="#reference-$1">&#91;$1&#93;</a></sup>""",
                               ).map { it.first.toRegex() to it.second }

fun includesLineTransformer(sourceLine: String): String {
    return stringReplacements.fold(sourceLine) { line, (old, new) ->
        line.replace(old, new)
    }
}
