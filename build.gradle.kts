/*
 * Strata - A library for parsing and comparing version strings
 * Copyright (c) 2021-2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file build.gradle.kts is part of Strata
 * Last modified on 23-02-2022 12:48 p.m.
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
    signing
    publishing
    `maven-publish`
    id("org.ajoberstar.grgit") version "4.1.0"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "signing")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "org.ajoberstar.grgit")
    
    group = "ca.solo-studios"
    version = Version("1", "2", "1")
    
    repositories {
        mavenCentral()
    }
    
    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.8.2")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.8.2")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-params:5.8.2")
    }
    
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    tasks.withType<JavaCompile> {
        options.release.set(8)
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
    
    val projectDescription = """
        A library for parsing and comparing version strings
    """.trimIndent()
    val projectGroup = project.group as String
    val projectName = if (project.name == "strata-core")
        "Strata"
    else
        project.name.split("-").joinToString(separator = " ") { word -> word.capitalize() }
    val projectVersion = if (project.version is String) project.version as String else project.version.toString()
    val projectUrl = "https://github.com/solo-studios/Strata"
    
    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifact(jar)
                artifact(sourcesJar)
                artifact(javadocJar)
                
                version = projectVersion
                groupId = projectGroup
                artifactId = project.name
                
                pom {
                    name.set(projectName)
                    description.set(projectDescription)
                    url.set(projectUrl)
                    
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
                        url.set("$projectUrl/issues")
                    }
                    scm {
                        connection.set("scm:git:$projectUrl.git")
                        developerConnection.set("scm:git:${projectUrl.replace("https", "ssh")}.git")
                        url.set(projectUrl)
                    }
                }
            }
        }
        
        repositories {
            maven {
                name = "sonatypeStaging"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials(PasswordCredentials::class)
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
        publishing.publications.all {
            sign(this)
        }
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