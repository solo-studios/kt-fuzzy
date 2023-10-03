# Package ca.solostudios.stringsimilarity.edit

This package contains the edit-based string measure implementations.

## Algorithms

### [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein]

The [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein] distance between two words is the minimum number of
single-character edits (insertions, deletions, or substitutions) required to change one word into the other.

It is a metric string distance. This class implements the dynamic programming approach,
which has a space requirement \\(O(m \\times n)\\), and computation cost \\(O(m \\times n)\\).

#### Example

```kotlin
val levenshtein = Levenshtein()

println(levenshtein.distance("My string", "My \$tring")) // prints 1.0
```

### [Normalized Levenshtein][ca.solostudios.stringsimilarity.edit.NormalizedLevenshtein]

This is computed as the [levenshtein distance][ca.solostudios.stringsimilarity.edit.Levenshtein]
normalized to be in the range \\(&#91;0.0, 1.0&#93;\\).

It is a metric string distance. This class implements the dynamic programming approach,
which has a space requirement \\(O(m \\times n)\\), and computation cost \\(O(m \\times n)\\).

#### Example

```kotlin
val normLevenshtein = NormalizedLevenshtein()

println(normLevenshtein.distance("My string", "My \$tring")) // prints 0.10526315789473684
```

### [Damerau-Levenshtein][ca.solostudios.stringsimilarity.edit.DamerauLevenshtein]

Similar to the [Levenshtein distance][ca.solostudios.stringsimilarity.edit.Levenshtein],
the [Damerau-Levenshtein distance][ca.solostudios.stringsimilarity.edit.DamerauLevenshtein] with transposition
(also sometimes calls unrestricted Damerau-Levenshtein distance) is the minimum number of operations needed to transform
one string into the other, where an operation is defined as an insertion, deletion, or substitution of a single character,
or a **transposition of two adjacent characters**.

It is a metric string distance. This class implements the dynamic programming approach,
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

### [Normalized Damerau-Levenshtein][ca.solostudios.stringsimilarity.edit.NormalizedDamerauLevenshtein]

This is computed as the [Damerau-Levenshtein distance][ca.solostudios.stringsimilarity.edit.DamerauLevenshtein]
normalized to be in the range \\(&#91;0.0, 1.0&#93;\\).

It is a metric string distance. This class implements the dynamic programming approach,
which has a space requirement \\(O(m \\times n)\\), and computation cost \\(O(m \\times n)\\).

#### Example

```kotlin
val damerau = NormalizedDamerauLevenshtein()

println(damerau.distance("ABCDEF", "ABDCEF")) // prints 0.15384615384615385

// 2 substitutions
println(damerau.distance("ABCDEF", "BACDFE")) // prints 0.2857142857142857

// 1 deletion
println(damerau.distance("ABCDEF", "ABCDE")) // prints 0.16666666666666666
println(damerau.distance("ABCDEF", "BCDEF")) // prints 0.16666666666666666
println(damerau.distance("ABCDEF", "ABCGDEF")) // prints 0.14285714285714285

// All different
println(damerau.distance("ABCDEF", "POIU")) // prints 0.75

// Transpose
println(damerau.distance("CA", "ABC")) // prints 0.5714285714285714
```

### [Optimal String Alignment][ca.solostudios.stringsimilarity.edit.OptimalStringAlignment]

The [Optimal String Alignment distance][ca.solostudios.stringsimilarity.edit.OptimalStringAlignment] variant
of [Damerau-Levenshtein distance][ca.solostudios.stringsimilarity.edit.DamerauLevenshtein]
(sometimes called the restricted edit distance) computes the number of edit operations needed
to make the strings equal under the condition that **no substring is edited more than once**,
whereas the true the [Damerau-Levenshtein distance][ca.solostudios.stringsimilarity.edit.DamerauLevenshtein]
presents no such restriction.
The difference from the algorithm for the [Levenshtein distance][ca.solostudios.stringsimilarity.edit.Levenshtein] is the
addition of one recurrence for the transposition operations.

It is a metric string distance. This class implements the dynamic programming approach,
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

### [Normalized Optimal String Alignment][ca.solostudios.stringsimilarity.edit.NormalizedOptimalStringAlignment]

This is computed as the [Optimal String Alignment][ca.solostudios.stringsimilarity.edit.OptimalStringAlignment]
normalized to be in the range \\(&#91;0.0, 1.0&#93;\\).

It is a metric string distance. This class implements the dynamic programming approach,
which has a space requirement \\(O(m \\times n)\\), and computation cost \\(O(m \\times n)\\).

#### Example

```kotlin
val osa = NormalizedOptimalStringAlignment()

println(osa.distance("ABCDEF", "ABDCEF")) // prints 0.15384615384615385

// 2 substitutions
println(osa.distance("ABCDEF", "BACDFE")) // prints 0.2857142857142857

// 1 deletion
println(osa.distance("ABCDEF", "ABCDE")) // prints 0.16666666666666666
println(osa.distance("ABCDEF", "BCDEF")) // prints 0.16666666666666666
println(osa.distance("ABCDEF", "ABCGDEF")) // prints 0.14285714285714285

// All different
println(osa.distance("ABCDEF", "POIU")) // prints 0.75

// Transpose
println(osa.distance("CA", "ABC")) // prints 0.75
```

### [Longest Common Subsequence][ca.solostudios.stringsimilarity.edit.LCS]

The [Longest Common Subsequence][ca.solostudios.stringsimilarity.edit.LCS] (LCS) problem consists in finding the longest
subsequence common to two (or more) sequences.
It differs from problems of finding common substrings: unlike substrings, subsequences are not required to
occupy consecutive positions within the original sequences.

It is used by the diff utility, by Git for reconciling multiple changes, etc.

The [LCS distance][ca.solostudios.stringsimilarity.edit.LCS] is equivalent
to the [Levenshtein distance][ca.solostudios.stringsimilarity.edit.Levenshtein] when only insertion and deletion is
allowed (no substitution), or when the cost of the substitution is the double of the cost of an insertion or deletion.

It is a metric string distance. This class implements the dynamic programming approach,
which has a space requirement \\(O(m \\times n)\\), and computation cost \\(O(m \\times n)\\)[@ft-a].

#### Example

```kotlin
val lcs = LongestCommonSubsequence()

println(lcs.distance("AGCAT", "GAC")) // prints 4.0

println(lcs.distance("AGCAT", "AGCT")) // prints 1.0
```

### [Normalized Longest Common Subsequence][ca.solostudios.stringsimilarity.edit.NormalizedLCS]

This is computed as the [Longest Common Subsequence][ca.solostudios.stringsimilarity.edit.LCS]
normalized to be in the range \\(&#91;0.0, 1.0&#93;\\).

It is a metric string distance. This class implements the dynamic programming approach,
which has a space requirement \\(O(m \\times n)\\), and computation cost \\(O(m \\times n)\\)[@ft-a].

#### Example

```kotlin
val normalizedLCS = NormalizedLCS()

println(normalizedLCS.distance("ABCDEFG", "ABCDEFHJKL")) // prints 0.45454545454545453

println(normalizedLCS.distance("ABDEF", "ABDIF")) // prints 0.3333333333333333
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
