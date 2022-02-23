# Strata

[![Build Status](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.solo-studios.ca%2Fjob%2Fsolo-studios%2Fjob%2FPolyBot%2F&style=for-the-badge)](https://ci.solo-studios.ca/job/solo-studios/job/PolyBot/)
[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/ca.solo-studios/strata.svg?style=for-the-badge&label=Maven%20Central)](https://search.maven.org/search?q=g:ca.solo-studios%20a:strata)
[![100% Java](https://img.shields.io/badge/100%25-java-blue.svg?style=for-the-badge)](https://openjdk.java.net/)
[![Discord Server](https://img.shields.io/discord/871114669761372221?color=7389D8&label=Discord&logo=discord&logoColor=8fa3ff&style=for-the-badge)](https://discord.solo-studios.ca)

A simple, dependency-less, library for parsing and comparing version according to the [SemVer spec](https://semver.org/)

## Features

- Can parse any valid SemVer version.
- Has no external dependencies other than [JetBrains Annotations](https://github.com/JetBrains/java-annotations) to help document the public
  API.
- Very fast. Can parse 1 million versions in under 5 seconds. (2.1 seconds on my cpu, a Ryzen 5 3600.) At that level, speed is
  insignificant, unless you have a really weird usecase where you need to parse hundreds of thousands of versions a second.
- Simple to use API.
- Includes a set of Kotlin extensions to make development in kotlin easier and more idiomatic.

## Including

Builds can be found on Maven Central, and can be included with any build tool that supports maven.

```xml
<dependency>
  <groupId>ca.solo-studios</groupId>
  <artifactId>strata</artifactId>
  <version>1.2.2</version>
</dependency>
```

The kotlin extensions can be included as follows:

```xml
<dependency>
  <groupId>ca.solo-studios</groupId>
  <artifactId>strata-kotlin</artifactId>
  <version>1.2.2</version>
</dependency>
```

### Gradle Groovy DSL

```groovy
implementation 'ca.solo-studios:strata:1.2.2'
```

The kotlin extensions can be included as follows:

```groovy
implementation 'ca.solo-studios:strata-kotlin:1.2.2'
```

### Gradle Kotlin DSL

```kotlin
implementation("ca.solo-studios:strata:1.2.2")
```

The kotlin extensions can be included as follows:

```kotlin
implementation("ca.solo-studios:strata-kotlin:1.2.2")
```

## Examples

Note: version parsing may throw a `ParseException`, which you are expeted to handle. This is thrown if ever the provided version is
incorrect.

### Getting a version

```java
try {
    Version version = Versions.parseVersion("1.2.3");
} catch (ParseException e) {
    // handle version parse exception
}
```

or

```kotlin
val version = "1.2.3".toVersion()
```

### Getting a version range

```java
try {
    VersionRange range = Versions.parseVersionRange("[1.2.3,4.5.6)");
} catch (ParseException e) {
    // handle invalid version range parse exception
}
```

or

```kotlin
val range = "[1.2.3,4.5.6)".toVersionRange()
```

### Checking if a version is within a version range

```java
Version version = [...]

VersionRange range = [...]

if (range.isSatisfiedBy(version)) {
    // version is within range
} else {
    // version is outside range
}
```

or

```kotlin
val version = [...]

val range = [...]

if (version in range)
    // version is within range
else
    // version is outside range
```