/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.publishing.gradle.kts is part of kotlin-fuzzy
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

plugins {
    signing
    `maven-publish`
}

afterEvaluate {
    publishing {
        publications.withType<MavenPublication>().configureEach {
            val projectName = project.nameFormatted
            val projectGroup = project.group.toStringOrEmpty()
            val projectVersion = project.version.toStringOrEmpty()
            val projectDescription = project.description.toStringOrEmpty()
            val projectUrl = Repository.projectUrl
            val projectBaseUri = Repository.projectBaseUri

            val licenseName = "MIT"
            val licenseUrl = "https://mit-license.org/"

            groupId = projectGroup
            // This breaks shit (don't do it)
            // artifactId = project.name
            version = projectVersion

            pom {
                name = projectName
                description = projectDescription
                url = projectUrl

                inceptionYear = "2021"

                licenses {
                    license {
                        name = licenseName
                        url = licenseUrl
                    }
                }
                developers {
                    developer {
                        id = "solonovamax"
                        name = "solonovamax"
                        email = "solonovamax@12oclockpoint.com"
                        url = "https://solonovamax.gay/"
                    }
                }
                issueManagement {
                    system = "GitHub"
                    url = "$projectUrl/issues"
                }
                scm {
                    connection = "scm:git:$projectUrl.git"
                    developerConnection = "scm:git:ssh://$projectBaseUri.git"
                    url = projectUrl
                }
            }
        }

        repositories {
            maven {
                name = "Sonatype"

                val releasesUrl =
                        uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") // releases repo
                val snapshotUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") // snapshot repo
                url = if (isSnapshot) snapshotUrl else releasesUrl

                credentials(PasswordCredentials::class)
            }
            maven {
                name = "SoloStudios"

                val releasesUrl = uri("https://maven.solo-studios.ca/releases/")
                val snapshotUrl = uri("https://maven.solo-studios.ca/snapshots/")
                url = if (isSnapshot) snapshotUrl else releasesUrl

                credentials(PasswordCredentials::class)
                authentication { // publishing doesn't work without this for some reason
                    create<BasicAuthentication>("basic")
                }
            }

            maven {
                name = "test"
                url = rootProject.buildDir.resolve("localmaven").toURI()
            }
        }
    }

    // tasks.withType<AbstractPublishToMaven>().configureEach {
    //     dependsOn(tasks.withType<Sign>())
    // }
    //
    // signing {
    //     useGpgCmd()
    //     sign(publishing.publications)
    //     // sign(publishing.publications["maven"])
    // }
}
