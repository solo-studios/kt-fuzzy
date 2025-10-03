/*
 * Copyright (c) 2023-2025 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file kt-fuzzy.publishing.gradle.kts is part of kotlin-fuzzy
 * Last modified on 03-10-2025 12:53 a.m.
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

import ca.solostudios.nyx.sonatype.PublishingType

plugins {
    signing
    `maven-publish`
    id("ca.solo-studios.nyx")
    id("ca.solo-studios.sonatype-publish")
}

sonatype {
    publishingType = PublishingType.AUTOMATIC
}

nyx {
    info {
        group = "ca.solo-studios"
        organizationName = "Solo Studios"
        organizationUrl = "https://solo-studios.ca/"

        developer {
            id = "solonovamax"
            name = "solonovamax"
            email = "solonovamax@12oclockpoint.com"
            url = "https://solonovamax.gay/"
        }

        repository.fromGithub("solo-studios", "kt-fuzzy")
        license.useMIT()
    }

    publishing {
        withSignedPublishing()

        repositories {
            maven {
                name = "SoloStudiosReleases"

                url = uri("https://maven.solo-studios.ca/releases/")

                credentials(PasswordCredentials::class)
                authentication { // publishing doesn't work without this for some reason
                    create<BasicAuthentication>("basic")
                }
            }
            maven {
                name = "SoloStudiosSnapshots"

                url = uri("https://maven.solo-studios.ca/snapshots/")

                credentials(PasswordCredentials::class)
                authentication { // publishing doesn't work without this for some reason
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }
}
