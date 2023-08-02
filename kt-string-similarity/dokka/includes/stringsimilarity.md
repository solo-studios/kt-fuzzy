# Package ca.solostudios.stringsimilarity

Package containing all the string similarity algorithms

## Algorithms

### Normalized, metric, similarity and distance

Although the topic might seem simple, a lot of different algorithms exist to measure text similarity or distance.
Therefore, the library defines some interfaces to categorize them.

#### (Normalized) similarity and distance

- [StringSimilarity][ca.solostudios.stringsimilarity.interfaces.StringSimilarity]: Implementing algorithms define a
  similarity between
  strings (0 means strings are completely different).
- [NormalizedStringSimilarity][ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity]: The interface
  extends [StringSimilarity][ca.solostudios.stringsimilarity.interfaces.StringSimilarity].
  Implementing algorithms compute a similarity that has been normalized based on the number of operations performed.
  This means that for non-weighted implementations, the result will always be between 0 and 1.
  [Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler] is an example of this.
- [StringDistance][ca.solostudios.stringsimilarity.interfaces.StringDistance]: Implementing algorithms define a distance
  between strings
  (0 means strings are identical), like [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein] for example.
  The maximum distance value depends on the algorithm.
- [NormalizedStringDistance][ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance]: This interface
  extends [StringDistance][ca.solostudios.stringsimilarity.interfaces.StringDistance].
  Implementing algorithms compute a distance that has been normalized based on the number of operations performed.
  This means that for non-weighted implementations, the result will always be between \\([0, 1]\\).
  [NormalizedLevenshtein][ca.solostudios.stringsimilarity.edit.NormalizedLevenshtein] is an example of this.

Generally, algorithms that
implement [NormalizedStringSimilarity][ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity]
also implement [NormalizedStringDistance][ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance], and
\\(\text{similarity} = 1 - \text{distance}\\).
<br/>
( !! Only if the result is *always* between 0 and 1. !! )

But there are a few exceptions, like N-Gram similarity and distance (Kondrak).

#### Metric distances

The [MetricStringDistance][ca.solostudios.stringsimilarity.interfaces.MetricStringDistance] interface: A few of the
distances are actually
metric distances, which means that verify the triangle inequality \\(d(x, y) <= d(x,z) + d(z,y)\\).
For example, [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein] is a metric distance, but
[NormalizedLevenshtein][ca.solostudios.stringsimilarity.edit.NormalizedLevenshtein] is not.

A lot of nearest-neighbor search algorithms and indexing structures rely on the triangle inequality.
You can check "Similarity Search, The Metric Space Approach" by Zezula et al. for a survey.
These cannot be used with non metric similarity measures.

### Shingles (n-gram) based similarity and distance

A few algorithms work by converting strings into sets of n-grams (sequences of n characters, also sometimes called
k-shingles).
The similarity or distance between the strings is then the similarity or distance between the sets.

Some of them, like [jaccard][ca.solostudios.stringsimilarity.Jaccard], consider strings as sets of shingles, and don't
consider the number
of occurences of each shingle.
Others, like [cosine similarity][ca.solostudios.stringsimilarity.Cosine], work using what is sometimes called the
profile of the strings,
which takes into account the number of occurences of each shingle.

For these algorithms, another use case is possible when dealing with large datasets:

1. Compute the set or profile representation of all the strings
2. Compute the similarity between sets or profiles

### [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein]

The [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein] distance between two words is the minimum number of
single-character edits
(insertions, deletions or substitutions) required to change one word into the other.

It is a metric string distance.
This implementation uses dynamic programming (Wagner–Fischer algorithm), with only 2 rows of data.
The space requirement is thus \\(O(m)\\) and the algorithm runs in \\(O(m \\times n)\\).

#### Example

```kotlin
val levenshtein = Levenshtein()

println(levenshtein.distance("My string", "My \$tring")) // prints 1.0
```

### [Normalized Levenshtein][ca.solostudios.stringsimilarity.edit.NormalizedLevenshtein]

This distance is computed as [levenshtein distance][ca.solostudios.stringsimilarity.edit.Levenshtein] divided by the
length of the longest
string.
The resulting value is always in the interval \\([0.0, 1.0]\\) but it is not a metric anymore!

The similarity is computed as 1 - normalized distance.

```kotlin
val normLevenshtein = NormalizedLevenshtein()

println(normLevenshtein.distance("My string", "My \$tring")) // prints 0.1111111111111111
```

### [Weighted Levenshtein][ca.solostudios.stringsimilarity.WeightedLevenshtein]

An implementation of [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein] that allows to define different
weights for different
character substitutions.

This algorithm is usually used for optical character recognition (OCR) applications.
For OCR, the cost of substituting P and R is lower then the cost of substituting P and M for example because because
from and OCR point of
view P is similar to R.

It can also be used for keyboard typing auto-correction.
Here the cost of substituting T and R is lower for example because these are located next to each other on an AZERTY or
QWERTY keyboard.
Hence the probability that the user mistyped the characters is higher.

```kotlin
val weightedLevenshtein = WeightedLevenshtein() { old, new ->
    if (old == 't' && new == 'r') 0.5 else 1.0
}

println(weightedLevenshtein.distance("String1", "Srring2")) // prints 1.5
```

### [Damerau-Levenshtein][ca.solostudios.stringsimilarity.edit.DamerauLevenshtein]

Similar to [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein],
[Damerau-Levenshtein distance][ca.solostudios.stringsimilarity.edit.DamerauLevenshtein] with transposition
(also sometimes calls unrestricted Damerau-Levenshtein distance) is the minimum number of operations needed to transform
one string into the
other, where an operation is defined as an insertion, deletion, or substitution of a single character, or a *
*transposition of two adjacent
characters**.

It does respect triangle inequality, and is thus a metric distance.

This is not to be confused with the optimal string alignment distance, which is an extension where no substring can be
edited more than
once.

```kotlin
val damerau = Damerau()

println(damerau.distance("ABCDEF", "ABDCEF")) // prints 1.0

// 2 substitutions
println(d.distance("ABCDEF", "BACDFE")) // prints 2.0

// 1 deletion
println(d.distance("ABCDEF", "ABCDE")) // prints 1.0
println(d.distance("ABCDEF", "BCDEF")) // prints 1.0
println(d.distance("ABCDEF", "ABCGDEF")) // prints 1.0

// All different
println(d.distance("ABCDEF", "POIU")) // prints 6.0
```

### [Optimal String Alignment][ca.solostudios.stringsimilarity.edit.OptimalStringAlignment]

The [Optimal String Alignment][ca.solostudios.stringsimilarity.edit.OptimalStringAlignment] variant
of [Damerau-Levenshtein][ca.solostudios.stringsimilarity.edit.DamerauLevenshtein] (sometimes called the restricted edit
distance) computes the number of
edit operations needed to make the strings equal under the condition that **no substring is edited more than once**,
whereas the
true [Damerau-Levenshtein][ca.solostudios.stringsimilarity.edit.DamerauLevenshtein] presents no such restriction.
The difference from the algorithm for [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein] distance is the
addition of one recurrence
for the transposition
operations.

Note that for the [optimal string alignment][ca.solostudios.stringsimilarity.edit.OptimalStringAlignment] distance, the
triangle inequality does
not hold and so it is not a true metric.

```kotlin
val optimalStringAlignment = OptimalStringAlignment()

println(optimalStringAlignment.distance("CA", "ABC")) // prints 3.0
```

### [Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler]

[Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler] is a string edit distance that was developed in the area of
record linkage
(duplicate detection) (Winkler, 1990).
The Jaro–Winkler distance metric is designed and best suited for short strings such as person names, and to detect
transposition typos.

[Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler] computes the similarity between 2 strings, and the returned
value lies in the
interval \\([0.0, 1.0]\\).
It is (roughly) a variation of [Damerau-Levenshtein][ca.solostudios.stringsimilarity.edit.DamerauLevenshtein], where the
transposition of 2 close
characters is considered less important than the transposition of 2 characters that are far from each other.
[Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler] penalizes additions or substitutions that cannot be
expressed as transpositions.

The distance is computed as \\(1 - [Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler]\\) similarity.

```kotlin
val jaroWinkler = JaroWinkler()

// substitution of s and t
println(jaroWinkler.similarity("My string", "My tsring")) // prints 0.9740740656852722

// substitution of s and n
println(jaroWinkler.similarity("My string", "My ntrisg")) // prints 0.8962963223457336
```

### [Longest Common Subsequence][ca.solostudios.stringsimilarity.edit.LCS]

The [longest common subsequence][ca.solostudios.stringsimilarity.edit.LCS] (LCS) problem consists in finding the longest
subsequence common to two (or more) sequences.
It differs from problems of finding common substrings: unlike substrings, subsequences are not required to
occupy consecutive positions within the original sequences.

It is used by the diff utility, by Git for reconciling multiple changes, etc.

The [LCS][ca.solostudios.stringsimilarity.edit.LCS] distance between strings X (of length n) and Y (of length m) is
\\(n + m - 2 |LCS(X, Y)|\\),
\\(\\text{min} = 0\\),
\\(\\text{max} = n + m\\)

[LCS][ca.solostudios.stringsimilarity.edit.LCS] distance is equivalent
to [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein] distance when only insertion and deletion is
allowed (no substitution), or
when the cost of the substitution is the double of the cost of an insertion or deletion.

This class implements the dynamic programming approach, which has a space requirement \\(O(m \\times n)\\), and
computation cost
\\(O(m \\times n)\\).

In "Length of Maximal Common Subsequences", K.S. Larsen proposed an algorithm that computes the length
of [LCS][ca.solostudios.stringsimilarity.edit.LCS] in time \\(O(log(m) \\times log(n))\\).
But the algorithm has a memory requirement \\(O(m \\times n^2)\\) and was thus not implemented here.

```kotlin
val longestCommonSubsequence = LongestCommonSubsequence()

println(longestCommonSubsequence.distance("AGCAT", "GAC")) // prints 4.0

println(longestCommonSubsequence.distance("AGCAT", "AGCT")) // prints 1.0
```

### [Normalized Longest Common Subsequence][ca.solostudios.stringsimilarity.edit.NormalizedLCS]

This distance is computed as [Longest Common Subsequence][ca.solostudios.stringsimilarity.edit.LCS] divided by the
length of the longest
string.
The resulting value is always in the interval \\([0.0, 1.0]\\) but it is not a metric anymore!

The similarity is computed as 1 - normalized distance.

The distance is computed as \\(1 - |LCS(s1, s2)| / max(|s1|, |s2|)\\)

```kotlin
val normalizedLCS = NormalizedLCS()

// LCS: "ABCDEF" => length = 6
// longest = "ABCDEFHJKL" => length = 10
// => 1 - 6/10 = 0.4
println(normalizedLCS.distance("ABCDEFG", "ABCDEFHJKL")) // prints 0.4

// LCS: "ABDF" => length = 4
// longest = "ABDEF" => length = 5
// => 1 - 4 / 5 = 0.2
println(normalizedLCS.distance("ABDEF", "ABDIF")) // prints 0.2
```

### [N-Gram][ca.solostudios.stringsimilarity.NGram]

[Normalized N-Gram][ca.solostudios.stringsimilarity.NGram] distance as defined by Kondrak,
"N-Gram Similarity and Distance", String Processing and Information Retrieval,
Lecture Notes in Computer Science Volume 3772, 2005, pp 115-126.

The algorithm uses affixing with special character '`\\n`' to increase the weight of first characters.
The normalization is achieved by dividing the total similarity score the original length of the longest word.

In the paper, Kondrak also defines a similarity measure, which is not implemented (yet).

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

The cost for computing these similarities and distances is mainly domnitated by k-shingling
(converting the strings into sequences of k characters).
Therefore there are typically two use cases for these algorithms:

Directly compute the distance between strings:

```kotlin
val dig = QGram(2)

// AB BC CD CE
// 1  1  1  0
// 1  1  0  1
// Total: 2

println(dig.distance("ABCD", "ABCE")) // prints 2
```

Or, for large datasets, pre-compute the profile of all strings.
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
"Approximate string-matching with q-grams and maximal matches"

The distance between two strings is defined as the L1 norm of the difference of their profiles (the number of occurences
of
each [n-gram][ca.solostudios.stringsimilarity.NGram]): \\(SUM( |V1_i - V2_i| )\\).
[Q-gram][ca.solostudios.stringsimilarity.QGram] distance is a lower bound
on [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein]
distance, but can be computed in \\(O(m + n)\\), where [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein]
requires
\\(O(m \\times n)\\)

#### [Cosine similarity][ca.solostudios.stringsimilarity.Cosine]

The similarity between the two strings is the cosine of the angle between these two vectors representation, and is
computed as \\(V1 \\cdot V2 / (|V1| * |V2|)\\)

Distance is computed as \\(1 - \text{cosine similarity}\\).

#### [Jaccard index][ca.solostudios.stringsimilarity.Jaccard]

Like [Q-Gram][ca.solostudios.stringsimilarity.QGram] distance, the input strings are first converted into sets of
n-grams (sequences of n
characters, also called k-shingles), but this time the cardinality of
each [n-gram][ca.solostudios.stringsimilarity.NGram] is not taken into
account.
Each input string is simply a set of n-grams.
The [Jaccard index][ca.solostudios.stringsimilarity.Jaccard] is then computed as \\(|V1 \\cap V2| / |V1 \\cup V2|\\).

Distance is computed as 1 - similarity.
[Jaccard index][ca.solostudios.stringsimilarity.Jaccard] is a metric distance.

#### [Sorensen-Dice coefficient][ca.solostudios.stringsimilarity.SorensenDice]

Similar to [Jaccard index][ca.solostudios.stringsimilarity.Jaccard], but this time the similarity is computed as \\(2 *
|V1 \\cap V2| / (
|V1| + |V2|)\\).

Distance is computed as 1 - similarity.

### [Ratcliff-Obershelp][ca.solostudios.stringsimilarity.RatcliffObershelp]

[Ratcliff/Obershelp Pattern Recognition][ca.solostudios.stringsimilarity.RatcliffObershelp], also known as Gestalt
Pattern Matching, is a
string-matching algorithm for determining the similarity of two strings.
It was developed in 1983 by John W. Ratcliff and John A. Obershelp and published in the Dr. Dobb's Journal in July 1988

[Ratcliff/Obershelp][ca.solostudios.stringsimilarity.RatcliffObershelp] computes the similarity between 2 strings, and
the returned value
lies in the interval \\([0.0, 1.0]\\).

The distance is computed as 1 - Ratcliff/Obershelp similarity.

```kotlin
val ratcliffObershelp = RatcliffObershelp()

// substitution of s and t
println(ratcliffObershelp.similarity("My string", "My tsring")) // prints 0.8888888888888888

// substitution of s and n
println(ratcliffObershelp.similarity("My string", "My ntrisg")) // prints 0.7777777777777778
```

### Experimental

#### [SIFT4][ca.solostudios.stringsimilarity.Sift4]

[SIFT4][ca.solostudios.stringsimilarity.Sift4] is a general purpose string distance algorithm inspired
by [JaroWinkler][ca.solostudios.stringsimilarity.JaroWinkler]
and [Longest Common Subsequence][ca.solostudios.stringsimilarity.edit.LCS].
It was developed to produce a distance measure that matches as close as possible to the human perception of string
distance.
Hence it takes into account elements like character substitution, character distance, longest common subsequence etc.
It was developed using experimental testing, and without theoretical background.

```kotlin
val s1 = "This is the first string"
val s2 = "And this is another string"
val sift4 = Sift4(maxOffset = 5)

val expectedResult = 11.0
val result = sift4.distance(s1, s2)

assertEquals(expectedResult, result, 0.0)
```
