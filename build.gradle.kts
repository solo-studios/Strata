/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of Strata
 * Last modified on 24-09-2021 02:49 p.m.
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

import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    java
    idea
    signing
    `java-library`
    `maven-publish`
    id("org.ajoberstar.grgit") version "4.1.0"
}

group = "ca.solostudios"
version = Version("0", "1", "0", true)

repositories {
    mavenCentral()
}

dependencies {
    api("com.google.guava:guava:30.1.1-jre")
    api("org.apache.commons:commons-text:1.9")
    api("org.apache.commons:commons-collections4:4.4")
    api("org.jetbrains:annotations:22.0.0")
    
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params:5.8.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    
    ignoreFailures = false
    failFast = false
    maxParallelForks = (Runtime.getRuntime().availableProcessors() - 1).coerceAtLeast(1)
    
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
    }
}


/**
 * Version class that does version stuff.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Version(
    val major: String,
    val minor: String,
    val revision: String,
    val preRelease: Boolean = false
             ) {
    
    override fun toString(): String {
        return if (!preRelease)
            "$major.$minor.$revision"
        else //Only use git hash if it's a prerelease.
            "$major.$minor.$revision-BETA+${getGitHash()}"
    }
}

fun getGitHash(): String = grgit.head().abbreviatedId

println("getGitHash(): ${getGitHash()}")

val jar by tasks.named<Jar>("jar")

val javadoc by tasks.getting(Javadoc::class)

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(javadoc)
    archiveClassifier.set("javadoc")
    from(javadoc.destinationDir)
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.build {
    dependsOn(tasks.withType<Jar>())
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(sourcesJar)
            artifact(javadocJar)
            artifact(jar)
        
            version = version.toString()
            groupId = group.toString()
            artifactId = "strata"
        
            pom {
                name.set("Strata")
                description.set("A library for parsing and comparing version strings")
                url.set("https://github.com/PolyhedralDev/Strata")
            
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
                    developer {
                        id.set("dfsek")
                        name.set("dfsek")
                        email.set("dfsek@protonmail.com")
                        url.set("https://github.com/dfsek")
                    }
                }
                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/PolyhedralDev/Strata/issues")
                }
                scm {
                    connection.set("scm:git:https://github.com/PolyhedralDev/Strata.git")
                    developerConnection.set("scm:git:ssh://github.com/PolyhedralDev/Strata.git")
                    url.set("https://github.com/PolyhedralDev/Strata")
                }
            }
        }
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
}

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])
}
