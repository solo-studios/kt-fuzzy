# Kotlin Fuzzy

[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/ca.solo-studios/kt-fuzzy.svg?style=for-the-badge&label=Maven%20Central)](https://search.maven.org/search?q=g:ca.solo-studios%20a:kt-fuzzy)
[![Pure Kotlin](https://img.shields.io/badge/100%25-kotlin-blue.svg?style=for-the-badge)](https://kotlinlang.org/)
[![Discord Server](https://img.shields.io/discord/871114669761372221?color=7389D8&label=Discord&logo=discord&logoColor=8fa3ff&style=for-the-badge)](https://discord.solo-studios.ca)

A zero-dependency multiplatform library for fuzzy string matching in Kotlin which is *roughly* equivalent to the Python
package [thefuzz](https://github.com/seatgeek/thefuzz/) (Previously "fuzzywuzzy") <ins>without</ins> the GPL license.

## Kotlin Fuzzy String Matching

Kotlin Fuzzy, or kt-fuzzy was created because the only other options for Java/Kotlin fuzzy string matching libraries
were under the GPL. This meant, they couldn't be used in non-GPL projects. So, I created this library to remedy that.

This library was *inspired* by libraries
like [javawuzzy](https://github.com/xdrop/fuzzywuzzy), [fuzzywuzzy-kotlin](https://github.com/willowtreeapps/fuzzywuzzy-kotlin),
and [thefuzz](https://github.com/seatgeek/thefuzz) but <ins>does not use any code from them</ins>, allowing it to exist
*without* the GPL license.

This library was designed with the goal of mimicking their functionality, without directly using any code from those
repositories.

There are two modules that can be used:

- The Primary module: Kotlin Fuzzy
- The Secondary module: [Kotlin String Similarity](kt-string-similarity/README.md)

### Kotlin String Similarity

The secondary module, Kotlin String Similarity implements various string similarity and distance measures. It contains
over a dozen such algorithsm, including Levenshtein distance (and sibblings), Jaro-Winkler, Longest Common Subsequence,
cosine similarity, and many others.

It primarily a *direct port* of [Java String Similarity](https://github.com/tdebatty/java-string-similarity) by
tdebatty.

If you want to depend *only* on this module, you do not need to add a dependency on Kotlin Fuzzy to do so.
Read the [Kotlin String Similarity README](kt-string-similarity/README.md) for more info.

## Features

- [x] No dependencies
- [x] Simple to use
- [x] Performance friendly

## Including

You can include Kotlin Fuzzy in your project by adding the following, depending on your platform:

### Maven

```xml
<dependency>
    <groupId>ca.solo-studios</groupId>
    <artifactId>kt-fuzzy</artifactId>
    <version>VERSION</version>
</dependency>
```

### Gradle Groovy

```groovy
implementation 'ca.solo-studios:kt-fuzzy:VERSION'
```

### Gradle Kotlin

```kotlin
implementation("ca.solo-studios:kt-fuzzy:VERSION")
```

## Kotlin String Similarity

Kotlin String Similarity is a port of tdebatty's
[java-string-similarity](https://github.com/tdebatty/java-string-similarity) to Kotlin Multiplatform.

Read [README for Kotlin String Similarity](kt-string-similarity/README.md) for more info