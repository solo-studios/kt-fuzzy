# Module kt-string-similarity

Kotlin String Similarity is a Kotlin Multiplatform library for measuring and comparing the similarity of strings.

Kotlin String Similarity implements various string similarity and distance measures.
It contains over a dozen algorithms, including, but not limited to,
[Levenshtein][ca.solostudios.stringsimilarity.Levenshtein] distance (and siblings),
[Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler],
[Longest Common Subsequence][ca.solostudios.stringsimilarity.LongestCommonSubsequence],
[Cosine similarity][ca.solostudios.stringsimilarity.Cosine], and many others.
Check the summary table below for the complete list.

This is project contains a port of tdebatty's
[java-string-similarity](https://github.com/tdebatty/java-string-similarity) to Kotlin Multiplatform.

## Including

You can include ${project.module} in your project by adding the following:

### Maven

```xml
<dependency>
  <groupId>${project.group}</groupId>
  <artifactId>${project.module}</artifactId>
  <version>${project.version}</version>
</dependency>
```

### Gradle Groovy DSL

```groovy
implementation '${project.group}:${project.module}:${project.version}'
```

### Gradle Kotlin DSL

```kotlin
implementation("${project.group}:${project.module}:${project.version}")
```

### Gradle Version Catalog

```toml
${project.module} = { group = "${project.group}", name = "${project.module}", version = "${project.version}" }
```

## Overview

The main characteristics of each implemented algorithm are presented below.
The "cost" column gives an estimation of the computational cost to compute the similarity between two strings of length
\\(m\\) and \\(n\\) respectively.

| Name                                 | Similarity support | Normalized | Metric | Type    | Cost                                | Typical usage                    |
|--------------------------------------|--------------------|------------|--------|---------|-------------------------------------|----------------------------------|
| Levenshtein                          | ☐                  | ☐          | ☒      |         | \\(O(m \\times n)\\) <sup>1</sup>   |                                  |
| Normalized Levenshtein               | ☒                  | ☒          | ☐      |         | \\(O(m \\times n)\\) <sup>1</sup>   |                                  |
| Weighted Levenshtein                 | ☐                  | ☐          | ☐      |         | \\(O(m \\times n)\\) <sup>1</sup>   | OCR                              |
| Damerau-Levenshtein<sup>3</sup>      | ☐                  | ☐          | ☒      |         | \\(O(m \\times n)\\) <sup>1</sup>   |                                  |
| Optimal String Alignment<sup>3</sup> | ☐                  | ☐          | ☐      |         | \\(O(m \\times n)\\) <sup>1</sup>   |                                  |
| Jaro-Winkler                         | ☒                  | ☒          | ☐      |         | \\(O(m \\times n)\\)                | typo correction                  |
| Longest Common Subsequence           | ☐                  | ☐          | ☐      |         | \\(O(m \\times n)\\) <sup>1,2</sup> | diff utility, GIT reconciliation |
| Metric Longest Common Subsequence    | ☐                  | ☒          | ☒      |         | \\(O(m \\times n)\\) <sup>1,2</sup> |                                  |
| N-Gram                               | ☐                  | ☒          | ☐      |         | \\(O(m \\times n)\\)                |                                  |
| Q-Gram                               | ☐                  | ☐          | ☐      | Profile | \\(O(m+n)\\)                        |                                  |
| Cosine similarity                    | ☒                  | ☒          | ☐      | Profile | \\(O(m+n)\\)                        |                                  |
| Jaccard index                        | ☒                  | ☒          | ☒      | Set     | \\(O(m+n)\\)                        |                                  |
| Sorensen-Dice coefficient            | ☒                  | ☒          | ☐      | Set     | \\(O(m+n)\\)                        |                                  |
| Ratcliff-Obershelp                   | ☒                  | ☒          | ☐      |         | ?                                   |                                  |

1. In this library, Levenshtein edit distance, LCS distance and their sibblings are computed using the dynamic programming method, which
   has a cost \\(O(m \\times n)\\).
   For Levenshtein distance, the algorithm is sometimes called Wagner-Fischer algorithm ("The string-to-string correction problem", 1974).
   The original algorithm uses a matrix of size m x n to store the Levenshtein distance between string prefixes.

   If the alphabet is finite, it is possible to use the method of four russians (Arlazarov et al. "On economic construction of the
   transitive
   closure of a directed graph", 1970) to speedup computation.
   This was published by Masek in 1980 ("A Faster Algorithm Computing String Edit Distances").
   This method splits the matrix in blocks of size \\(t \\times t\\).
   Each possible block is precomputed to produce a lookup table.
   This lookup table can then be used to compute the string similarity (or distance) in \\(O(\\frac{nm}{t})\\).
   Usually, \\(t\\) is chosen as \\(log(m)\\) if \\(m > n\\).
   The resulting computation cost is thus \\(O(\\frac{mn}{log(m)})\\).
   This method has not been implemented (yet).

2. In "Length of Maximal Common Subsequences", K.S. Larsen proposed an algorithm that computes the length of LCS in time
   \\(O(log(m) \\times log(n))\\). But the algorithm has a memory requirement \\(O(m \\times n^2)\\) and was thus not implemented here.

3. There are two variants of Damerau-Levenshtein string distance: Damerau-Levenshtein with adjacent transpositions (also sometimes called
   unrestricted Damerau–Levenshtein distance) and Optimal String Alignment (also sometimes called restricted edit distance).
   For Optimal String Alignment, no substring can be edited more than once.
