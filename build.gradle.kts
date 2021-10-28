/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of kt-fuzzy
 * Last modified on 27-10-2021 08:04 p.m.
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

import org.jetbrains.dokka.gradle.DokkaTask

// import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
// import org.jmailen.gradle.kotlinter.tasks.InstallPreCommitHookTask

plugins {
    signing
    `maven-publish`
    kotlin("multiplatform") apply false
    id("org.jetbrains.dokka") version "1.5.0"
}

group = "ca.solo-studios"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "signing")
    apply(plugin = "maven-publish")
    apply(plugin = "kotlin-multiplatform")
    apply(plugin = "org.jetbrains.dokka")
    
    val dokkaHtml by tasks.getting(DokkaTask::class)
    
    val javadocJar by tasks.creating(Jar::class) {
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")
        from(dokkaHtml.outputDirectory)
    }
    
    
    tasks.withType<AbstractPublishToMaven> {
        dependsOn(tasks.withType<Sign>())
    }
    
    
    publishing {
        publications {
            withType<MavenPublication> {
                artifact(javadocJar)
                tasks.withType<AbstractPublishToMaven>()
                        .matching { it.publication == this }
                        .configureEach { enabled = true }
            }
        }
        publications.withType<MavenPublication> {
            pom {
                name.set("kt-fuzzy")
                description.set("A set of SLF4J extensions for Kotlin to make logging more idiomatic.")
                url.set("https://github.com/solo-studios/kt-fuzzy")
                
                inceptionYear.set("2021")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://mit-license.org/")
                    }
                }
                developers {
                    developer {
                        id.set("solonovamax")
                        name.set("solonovamax")
                        email.set("solonovamax@12oclockpoint.com")
                        url.set("https://github.com/solonovamax")
                    }
                }
                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/solo-studios/kt-fuzzy/issues")
                }
                scm {
                    connection.set("scm:git:https://github.com/solo-studios/kt-fuzzy.git")
                    developerConnection.set("scm:git:ssh://github.com/solo-studios/kt-fuzzy.git")
                    url.set("https://github.com/solo-studios/kt-fuzzy/")
                }
            }
            
            version = project.version as String
            groupId = project.group as String
            artifactId = project.name
        }
        
        repositories {
            maven {
                name = "sonatypeStaging"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
                credentials(org.gradle.api.credentials.PasswordCredentials::class)
            }
            maven {
                name = "sonatypeSnapshot"
                url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                credentials(PasswordCredentials::class)
            }
        }
        
        val publishToMavenLocal = tasks.getByName("publishToMavenLocal")
        tasks.getByName("publish").dependsOn(publishToMavenLocal)
    }
    
    
    signing {
        useGpgCmd()
        sign(publishing.publications)
    }
}

fun Project.kotlin(configure: Action<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>): Unit =
        (this as ExtensionAware).extensions.configure("kotlin", configure)
