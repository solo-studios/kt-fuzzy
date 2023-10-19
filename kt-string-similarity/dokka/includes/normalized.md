# Package ca.solostudios.stringsimilarity.normalized

This package contains normalized variants of several algorithms.

## Algorithms

### [Normalized Levenshtein][ca.solostudios.stringsimilarity.normalized.NormalizedLevenshtein]

This is computed as the [levenshtein distance][ca.solostudios.stringsimilarity.Levenshtein]
normalized to be in the range \\(&#91;0.0, 1.0&#93;\\).

It is a metric string distance.

This class implements the dynamic programming approach with two arrays,
which has a space requirement \\(O(n)\\), and computation cost \\(O(m \\times n)\\).

#### Example

```kotlin
val levenshtein = NormalizedLevenshtein()

println(levenshtein.distance("My string", "My \\\$tring")) // prints 0.10526315789473684
```

### [Normalized Damerau-Levenshtein][ca.solostudios.stringsimilarity.normalized.NormalizedDamerauLevenshtein]

This is computed as the [Damerau-Levenshtein distance][ca.solostudios.stringsimilarity.DamerauLevenshtein]
normalized to be in the range \\(&#91;0.0, 1.0&#93;\\).

It is a metric string distance.

This class implements the dynamic programming approach,
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

### [Normalized Optimal String Alignment][ca.solostudios.stringsimilarity.normalized.NormalizedOptimalStringAlignment]

This is computed as the [Optimal String Alignment][ca.solostudios.stringsimilarity.OptimalStringAlignment]
normalized to be in the range \\(&#91;0.0, 1.0&#93;\\).

It is *not* a metric string distance.

This class implements the dynamic programming approach,
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

### [Normalized Longest Common Subsequence][ca.solostudios.stringsimilarity.normalized.NormalizedLCS]

This is computed as the [Longest Common Subsequence][ca.solostudios.stringsimilarity.LCS]
normalized to be in the range \\(&#91;0.0, 1.0&#93;\\).

It is a metric string distance.

This class implements the dynamic programming approach with two arrays,
which has a space requirement \\(O(n)\\), and computation cost \\(O(m \\times n)\\)[@ft-a].

#### Example

```kotlin
val lcs = NormalizedLCS()

println(lcs.distance("ABCDEFG", "ABCDEFHJKL")) // prints 0.45454545454545453

println(lcs.distance("ABDEF", "ABDIF")) // prints 0.3333333333333333
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

