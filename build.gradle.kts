/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of Strata
 * Last modified on 24-09-2021 02:42 p.m.
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
        create<MavenPublication>("mavenJava") {
            artifact(tasks["sourcesJar"])
            artifact(tasks["jar"])
        }
    }
    
    repositories {
        val mavenUrl = "https://repo.codemc.io/repository/maven-releases/"
        
        maven(mavenUrl) {
            val mavenUsername: String? by project
            val mavenPassword: String? by project
            if (mavenUsername != null && mavenPassword != null) {
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }
}
