# Package ca.solostudios.stringsimilarity

This package contains most of the string measure implementations.

## Algorithms

### Edit-based measures (Levenshtein, LCS, Damerau-Levenshtein, etc.)

### [Levenshtein][ca.solostudios.stringsimilarity.Levenshtein]

The [Levenshtein][ca.solostudios.stringsimilarity.Levenshtein] distance between two words is the minimum number of
single-character edits (insertions, deletions, or substitutions) required to change one word into the other.

It is a metric string distance.

This class implements the dynamic programming approach with two arrays,
which has a space requirement \\(O(n)\\), and computation cost \\(O(m \\times n)\\).

#### Example

```kotlin
val levenshtein = Levenshtein()

println(levenshtein.distance("My string", "My \\\$tring")) // prints 1.0
```

### [Damerau-Levenshtein][ca.solostudios.stringsimilarity.DamerauLevenshtein]

Similar to the [Levenshtein distance][ca.solostudios.stringsimilarity.Levenshtein],
the [Damerau-Levenshtein distance][ca.solostudios.stringsimilarity.DamerauLevenshtein] with transposition
(also sometimes calls unrestricted Damerau-Levenshtein distance) is the minimum number of operations needed to transform
one string into the other, where an operation is defined as an insertion, deletion, or substitution of a single character,
or a **transposition of two adjacent characters**.

It is a metric string distance.

This class implements the dynamic programming approach,
which has a space requirement \\(O(m \\times n)\\), and computation cost \\(O(m \\times n)\\).

This is not to be confused with the optimal string alignment distance, which is an extension where no substring can be
edited more than once.

#### Example

```kotlin
val damerau = DamerauLevenshtein()

println(damerau.distance("ABCDEF", "ABDCEF")) // prints 1.0

// 2 substitutions
println(damerau.distance("ABCDEF", "BACDFE")) // prints 2.0

// 1 deletion
println(damerau.distance("ABCDEF", "ABCDE")) // prints 1.0
println(damerau.distance("ABCDEF", "BCDEF")) // prints 1.0
println(damerau.distance("ABCDEF", "ABCGDEF")) // prints 1.0

// All different
println(damerau.distance("ABCDEF", "POIU")) // prints 6.0

// Transpose
println(damerau.distance("CA", "ABC")) // prints 2.0
```

### [Optimal String Alignment][ca.solostudios.stringsimilarity.OptimalStringAlignment]

The [Optimal String Alignment distance][ca.solostudios.stringsimilarity.OptimalStringAlignment] variant
of [Damerau-Levenshtein distance][ca.solostudios.stringsimilarity.DamerauLevenshtein]
(sometimes called the restricted edit distance) computes the number of edit operations needed
to make the strings equal under the condition that **no substring is edited more than once**,
whereas the true the [Damerau-Levenshtein distance][ca.solostudios.stringsimilarity.DamerauLevenshtein]
presents no such restriction.
The difference from the algorithm for the [Levenshtein distance][ca.solostudios.stringsimilarity.Levenshtein] is the
addition of one recurrence for the transposition operations.

It is *not* a metric string distance.

This class implements the dynamic programming approach,
which has a space requirement \\(O(m \\times n)\\), and computation cost \\(O(m \\times n)\\).

#### Example

```kotlin
val osa = OptimalStringAlignment()

println(osa.distance("ABCDEF", "ABDCEF")) // prints 1.0

// 2 substitutions
println(osa.distance("ABCDEF", "BACDFE")) // prints 2.0

// 1 deletion
println(osa.distance("ABCDEF", "ABCDE")) // prints 1.0
println(osa.distance("ABCDEF", "BCDEF")) // prints 1.0
println(osa.distance("ABCDEF", "ABCGDEF")) // prints 1.0

// All different
println(osa.distance("ABCDEF", "POIU")) // prints 6.0

println(osa.distance("CA", "ABC")) // prints 3.0
```

### [Longest Common Subsequence][ca.solostudios.stringsimilarity.LCS]

The [Longest Common Subsequence][ca.solostudios.stringsimilarity.LCS] (LCS) problem consists in finding the longest
subsequence common to two (or more) sequences.
It differs from problems of finding common substrings: unlike substrings, subsequences are not required to
occupy consecutive positions within the original sequences.

It is used by the diff utility, by Git for reconciling multiple changes, etc.

The [LCS distance][ca.solostudios.stringsimilarity.LCS] is equivalent
to the [Levenshtein distance][ca.solostudios.stringsimilarity.Levenshtein] when only insertion and deletion is
allowed (no substitution), or when the cost of the substitution is the double of the cost of an insertion or deletion.

It is a metric string distance.

This class implements the dynamic programming approach with two arrays,
which has a space requirement \\(O(n)\\), and computation cost \\(O(m \\times n)\\)[@ft-a].

#### Example

```kotlin
val lcs = LCS()

println(lcs.distance("AGCAT", "GAC")) // prints 4.0

println(lcs.distance("AGCAT", "AGCT")) // prints 1.0
```

### Shingles (n-gram) based similarity and distance

A few algorithms work by converting strings into sets of n-grams
(sequences of n characters, also sometimes called k-shingles).
The similarity or distance between the strings is then the similarity or distance between the sets.

Some of them, like the [Jaccard index][ca.solostudios.stringsimilarity.Jaccard], consider strings as sets of shingles, and don't
consider the number of occurrences of each shingle.
Others, like [cosine similarity][ca.solostudios.stringsimilarity.Cosine], work using what is sometimes called the
profile of the strings, which takes into account the number of occurrences of each shingle.

For these algorithms, another use case is possible when dealing with large datasets:

1. Compute the set or profile representation of all the strings
2. Compute the similarity between sets or profiles

### [Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler]

[Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler] is a string distance that was developed in the area of
record linkage (duplicate detection), as defined by Winkler[@ref-1].
The Jaro–Winkler distance metric is designed and best suited for short strings such as person names,
and to detect transposition typos.

[Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler] computes the similarity between 2 strings, and the returned
value lies in the interval \\(&#91;0.0, 1.0&#93;\\).
It is (roughly) a variation of [Damerau-Levenshtein][ca.solostudios.stringsimilarity.normalized.DamerauLevenshtein], where the
transposition of 2 close characters is considered less important
than the transposition of 2 characters that are far from each other.
[Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler] penalizes additions or substitutions that cannot be
expressed as transpositions.

#### Example

```kotlin
val jaroWinkler = JaroWinkler()

// substitution of s and t
println(jaroWinkler.similarity("My string", "My tsring")) // prints 0.9740740656852722

// substitution of s and n
println(jaroWinkler.similarity("My string", "My ntrisg")) // prints 0.8962963223457336
```

### [N-Gram][ca.solostudios.stringsimilarity.NGram]

[N-Gram][ca.solostudios.stringsimilarity.NGram] similarity/distance is a distance used to measure the similarity of two strings,
that always lies in the range \\(&#91;0.0, 1.0&#93;\\).

The algorithm uses affixing with special character '`\\0`' to increase the weight of first characters.
The normalization is achieved by dividing the total similarity score the original length of the longest word.

#### Example

```kotlin
val twogram = NGram(2)
println(twogram.distance("ABCD", "ABTUIO")) // prints 0.583333

val s1 = "Adobe CreativeSuite 5 Master Collection from cheap 4zp"
val s2 = "Adobe CreativeSuite 5 Master Collection from cheap d1x"
val ngram = NGram(4)
println(ngram.distance(s1, s2)) // prints 0.97222
```

### [Shingle (n-gram) based algorithms][ca.solostudios.stringsimilarity.ShingleBased]

A few algorithms work by converting strings into sets of n-grams (sequences of n characters, also sometimes called
k-shingles).
The similarity or distance between the strings is then the similarity or distance between the sets.

> Note: although it may seem it is, the [N-Gram][ca.solostudios.stringsimilarity.NGram] similarity/distance is not shingle-based.

The cost for computing these similarities and distances is mainly dominated by k-shingling
(converting the strings into sequences of k characters).

There are typically two use cases for these algorithms:

1. Directly compute the distance between strings:
   ```kotlin
   val dig = QGram(2)

   // AB BC CD CE
   // 1  1  1  0
   // 1  1  0  1
   // Total: 2

   println(dig.distance("ABCD", "ABCE")) // prints 2
   ```

2. For large datasets, pre-compute the profile of all strings.
   The similarity can then be computed between profiles:
   ```kotlin
   /**
    * Example of computing cosine similarity with pre-computed profiles.
    */
   val s1 = "My first string"
   val s2 = "My other string..."

   // Let's work with sequences of 2 characters...
   val cosine = new Cosine(2)

   // Pre-compute the profile of strings
   val profile1 = cosine.profile(s1)
   val profile2 = cosine.profile(s2)

   // ...

   println(cosine.similarity(profile1, profile2)) // prints 0.516185
   ```

Pay attention, this only works if the same KShingling object is used to parse all input strings!

#### [Q-Gram][ca.solostudios.stringsimilarity.QGram]

[Q-gram][ca.solostudios.stringsimilarity.QGram] distance, as defined by Ukkonen in
"Approximate string-matching with q-grams and maximal matches"[@ref-3].

The distance between two strings is defined as the L1 norm of the difference of their profiles (the number of occurences
of each [n-gram][ca.solostudios.stringsimilarity.NGram]): \\(\\sum_{i=1}^n \\lVert \\vec{v1_i} - \\vec{v2_i} \\rVert\\).
[Q-gram][ca.solostudios.stringsimilarity.QGram] distance is a lower bound on
the [Levenshtein][ca.solostudios.stringsimilarity.normalized.Levenshtein] distance, but can be computed in \\(O(m + n)\\) time,
whereas the [Levenshtein][ca.solostudios.stringsimilarity.normalized.Levenshtein] distance requires \\(O(m \\times n)\\) time.

#### [Cosine similarity][ca.solostudios.stringsimilarity.Cosine]

The similarity between the two strings is the cosine of the angle between these two vector representation, and is
computed as \\(\\frac{\\vec{v_1} \\cdot \\vec{v_2}}{\\lVert\\vec{v_1}\\rVert \\times \\lVert\\vec{v_2}\\rVert}\\).

#### [Jaccard index][ca.solostudios.stringsimilarity.Jaccard]

Like [Q-Gram][ca.solostudios.stringsimilarity.QGram] distance, the input strings are first converted into sets of
n-grams (sequences of n characters, also called k-shingles), but this time the cardinality of
each [n-gram][ca.solostudios.stringsimilarity.NGram] is not taken into account.
Each input string is simply a set of n-grams.
The [Jaccard index][ca.solostudios.stringsimilarity.Jaccard] is then computed as
\\(\\frac{\\lVert V_1 \\cap V_2 \\rVert}{\\lVert V_1 \\cup V_2 \\rVert}\\).

[Jaccard index][ca.solostudios.stringsimilarity.Jaccard] is a metric distance.

#### [Sørensen-Dice coefficient][ca.solostudios.stringsimilarity.SorensenDice]

Similar to the [Jaccard index][ca.solostudios.stringsimilarity.Jaccard], but this time the similarity is computed as
\\(\\frac{2 \\times \\lVert V_1 \\cap V_2 \\rVert}{\\lVert V_1 \\rVert + \\lVert V_2 \\rVert}\\).

### [Ratcliff-Obershelp][ca.solostudios.stringsimilarity.RatcliffObershelp]

[Ratcliff/Obershelp Pattern Recognition][ca.solostudios.stringsimilarity.RatcliffObershelp],
also known as Gestalt Pattern Matching, is a string-matching algorithm for determining the similarity of two strings.
It was developed in 1983 by John W. Ratcliff and John A. Obershelp and published in the Dr. Dobb's Journal in July 1988[@ref-4].

[Ratcliff/Obershelp][ca.solostudios.stringsimilarity.RatcliffObershelp] computes the similarity between 2 strings, and
the returned value lies in the interval \\(&#91;0.0, 1.0&#93;\\).

#### Example

```kotlin
val ratcliffObershelp = RatcliffObershelp()

// substitution of s and t
println(ratcliffObershelp.similarity("My string", "My tsring")) // prints 0.8888888888888888

// substitution of s and n
println(ratcliffObershelp.similarity("My string", "My ntrisg")) // prints 0.7777777777777778
```

### Experimental

#### [Sift4][ca.solostudios.stringsimilarity.Sift4]

[Sift4][ca.solostudios.stringsimilarity.Sift4] is a general purpose string distance algorithm inspired
by [JaroWinkler][ca.solostudios.stringsimilarity.JaroWinkler]
and [Longest Common Subsequence][ca.solostudios.stringsimilarity.normalized.LCS].
It was developed to produce a distance measure that matches as close as possible to the human perception of string
distance.
Hence, it takes into account elements like character substitution, character distance, longest common subsequence etc.

It was developed using experimental testing, and without theoretical background.

#### Example

```kotlin
val s1 = "This is the first string"
val s2 = "And this is another string"
val sift4 = Sift4(maxOffset = 5)

val expectedResult = 11.0
val result = sift4.distance(s1, s2)

assertEquals(expectedResult, result, 0.0)
```

<h2 class="footnotes-header">Notes</h2>
<div class="footnotes">
<ol>
<li id="footnote-a">

K.S. Larsen proposed an algorithm that computes the length of LCS in time
\\(O(log(m) \\times log(n))\\).[@ref-4] But the algorithm has a memory requirement \\(O(m \\times n^2)\\) and was thus not
implemented here.
</li>
</ol>
</div>

<h2 class="references-header">References</h2>
<div class="references">
<ol>
<li id="reference-1">

Larsen, K. S. (1992-10). Length of maximal common subsequences. DAIMI Report
Series, 21(426).
<https://doi.org/10.7146/dpb.v21i426.6740><sup>[&#91;sci-hub&#93;](https://sci-hub.st/10.7146/dpb.v21i426.6740)</sup>
</li>
</ol>
</div>
<h2 class="references-header">References</h2>
<div class="references">
<ol>
<li id="reference-1">

Winkler, W. E. (1990). String comparator metrics and enhanced decision rules
in the fellegi-sunter model of record linkage. *Proceedings of the Survey
Research Methods Section*, 354-359. <https://eric.ed.gov/?id=ED325505>
</li>
<li id="reference-2">

Kondrak, G. (2005-11-02). N-gram similarity and distance. In String processing
and information retrieval, lecture notes in computer science (Pages 115-126).
Springer Berlin Heidelberg.
<https://doi.org/10.1007/11575832_13><sup>[&#91;sci-hub&#93;](https://sci-hub.st/10.1007/11575832_13)</sup>
</li>
<li id="reference-3">

Ukkonen, E. (1992-01). Approximate string matching with q-grams and maximal
matches. *Theoretical Computer Science*, *92*(1), 191–211.
<https://doi.org/10.1016/0304-3975(92)90143-4><sup>[&#91;sci-hub&#93;](https://sci-hub.st/10.1016/0304-3975(92)90143-4)</sup>
</li>
<li id="reference-4">

Ratcliff, J., & Metzener, D. E. (1988-07-01). Pattern matching: The gestalt
approach. *Dr. Dobb’s Journal*, *13*(7), 46. https://www.drdobbs.com/database/
pattern-matching-the-gestalt-approach/184407970?pgno=5
</li>
</ol>
</div>
