/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2024 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of Strata.
 * Last modified on 22-08-2024 07:13 p.m.
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
 * STRATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import ca.solostudios.nyx.util.soloStudios
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    java
    `java-library`
    `maven-publish`

    alias(libs.plugins.nyx)
    alias(libs.plugins.axion.release)

    alias(libs.plugins.jmh)
}

nyx {
    info {
        name = "Strata"
        group = "ca.solo-studios"
        version = scmVersion.version
        description = """
            A library for parsing and comparing version strings
        """.trimIndent()

        organizationName = "Solo Studios"
        organizationUrl = "https://solo-studios.ca/"

        developer {
            id.set("solonovamax")
            name.set("solonovamax")
            email.set("solonovamax@12oclockpoint.com")
            url.set("https://github.com/solonovamax")
        }
        developer {
            id.set("dfsek")
            name.set("dfsek")
            email.set("dfsek@protonmail.com")
            url.set("https://github.com/dfsek")
        }

        repository.fromGithub("solo-studios", "Strata")

        license.useMIT()
    }


    compile {
        jvmTarget = 8

        javadocJar = true
        sourcesJar = true

        allWarnings = true
        // warningsAsErrors = true
        distributeLicense = true
        buildDependsOnJar = true
        reproducibleBuilds = true

        java {
            // targeting java 8 has been deprecated in jdks >= 21
            // compilerArgs.add("-Xlint:-options")
        }
    }

    publishing {
        withSignedPublishing()

        repositories {
            maven {
                name = "SonatypeStaging"
                val repositoryId: String? by project
                url = when {
                    repositoryId != null -> uri("https://s01.oss.sonatype.org/service/local/staging/deployByRepositoryId/$repositoryId/")
                    else -> uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                }
                credentials(PasswordCredentials::class)
            }
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

repositories {
    soloStudios()
    mavenCentral()
}

dependencies {
    api(libs.jetbrains.annotations)

    testImplementation(libs.bundles.junit)
}

tasks {
    test {
        useJUnitPlatform()

        ignoreFailures = false
        failFast = false
        maxParallelForks = (Runtime.getRuntime().availableProcessors() - 1).coerceAtLeast(1)

        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}
