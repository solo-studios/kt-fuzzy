/*
 * Copyright (c) 2025 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Jenkinsfile is part of kotlin-fuzzy
 * Last modified on 03-10-2025 12:52 a.m.
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

pipeline {
    agent any

    tools {
        jdk "Temurin Java 21"
    }

    triggers {
        githubPush()
    }

    environment {
        DISCORD_WEBHOOK_URL = credentials('kt-fuzzy-discord-webhook-url')
    }

    stages {
        stage('Setup Gradle') {
            steps {
                sh 'chmod +x gradlew'
            }
        }

        stage('Build') {
            steps {
                withGradle {
                    sh './gradlew build dokkaGenerate -x allTests -x check'
                }
            }

            post {
                success {
                    archiveArtifacts artifacts: '**/build/libs/*.jar', excludes: '**/tmp/**,**/buildSrc/**,**/common-test/**', fingerprint: true, onlyIfSuccessful: true

                    javadoc javadocDir: 'build/dokka/html/', keepAll: true
                }
            }
        }

        stage('Tests') {
            steps {
                withGradle {
                    sh './gradlew allTests'
                }
            }

            post {
                always {
                    junit testResults: '**/build/test-results/*/TEST-*.xml'
                    allure includeProperties: false, jdk: '', results: [[path: '**/build/allure-results']]
                }
            }
        }

        stage('Deploy to snapshots repositories') {
            when {
                allOf {
                    not { buildingTag() }
                    not { expression { env.TAG_NAME != null && env.TAG_NAME.matches('v\\d+\\.\\d+\\.\\d+') } }
                }
            }

            steps {
                withCredentials([
                        string(credentialsId: 'maven-signing-key', variable: 'ORG_GRADLE_PROJECT_signingKey'),
                        string(credentialsId: 'maven-signing-key-password', variable: 'ORG_GRADLE_PROJECT_signingPassword'),
                        usernamePassword(
                                credentialsId: 'solo-studios-maven',
                                passwordVariable: 'ORG_GRADLE_PROJECT_SoloStudiosSnapshotsPassword',
                                usernameVariable: 'ORG_GRADLE_PROJECT_SoloStudiosSnapshotsUsername'
                        )
                ]) {
                    withGradle {
                        sh './gradlew publishAllPublicationsToSoloStudiosSnapshotsRepository'
                    }
                }
            }
        }

        stage('Deploy to releases repositories') {
            when {
                allOf {
                    buildingTag()
                    expression { env.TAG_NAME != null && env.TAG_NAME.matches('v\\d+\\.\\d+\\.\\d+') }
                }
            }

            steps {
                withCredentials([
                        string(credentialsId: 'maven-signing-key', variable: 'ORG_GRADLE_PROJECT_signingKey'),
                        string(credentialsId: 'maven-signing-key-password', variable: 'ORG_GRADLE_PROJECT_signingPassword'),
                        usernamePassword(
                                credentialsId: 'solo-studios-maven',
                                passwordVariable: 'ORG_GRADLE_PROJECT_SoloStudiosReleasesPassword',
                                usernameVariable: 'ORG_GRADLE_PROJECT_SoloStudiosReleasesUsername'
                        ),
                        usernamePassword(
                                credentialsId: 'sonatype-maven-credentials',
                                passwordVariable: 'ORG_GRADLE_PROJECT_SonatypeCentralPassword',
                                usernameVariable: 'ORG_GRADLE_PROJECT_SonatypeCentralUsername'
                        )
                ]) {
                    withGradle {
                        sh './gradlew publishAllPublicationsToSoloStudiosReleasesRepository'
                        sh './gradlew publishAllPublicationsToSonatypeRepository'
                    }
                }
            }
        }
    }

    post {
        always {
            discoverReferenceBuild()

            recordIssues(
                    aggregatingResults: true,
                    enabledForFailure: true,
                    minimumSeverity: 'ERROR',
                    sourceCodeEncoding: 'UTF-8',
                    checksAnnotationScope: 'ALL',
                    sourceCodeRetention: 'LAST_BUILD',
                    tools: [kotlin()]
            )

            discordSend(
                    title: env.JOB_NAME + ' ' + env.BUILD_DISPLAY_NAME,
                    showChangeset: true,
                    enableArtifactsList: false,
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult,
                    customAvatarUrl: 'https://github.com/solo-studios.png',
                    customUsername: 'Solo Studios Jenkins',
                    webhookURL: env.DISCORD_WEBHOOK_URL,
            )

            cleanWs()
        }
    }
}
