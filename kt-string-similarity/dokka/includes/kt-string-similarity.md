# Module kt-string-similarity

Kotlin String Similarity is a Kotlin Multiplatform library for measuring and comparing the similarity of strings.

Kotlin String Similarity implements various string similarity and distance measures.
It contains over a dozen algorithms, including, but not limited to,
[Levenshtein][ca.solostudios.stringsimilarity.Levenshtein] distance (and siblings),
[Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler],
[Longest Common Subsequence][ca.solostudios.stringsimilarity.LCS],
[Cosine similarity][ca.solostudios.stringsimilarity.Cosine], and many others.
Check the summary table below for the complete list.

This is project was initially a port of tdebatty's
[java-string-similarity](https://github.com/tdebatty/java-string-similarity) to Kotlin Multiplatform,
however is now expanding upon it.

## Including

{{dependencies}}

## Overview

The main characteristics of each implemented algorithm are presented below.
The "cost" columns gives an estimation of the computational/memory costs to compute the similarity between two strings of length
\\(m\\) and \\(n\\) respectively.

| Name                                       | Distance | Similarity | Normalized | Metric | Memory cost          | Execution cost              |
|--------------------------------------------|:--------:|:----------:|:----------:|:------:|----------------------|-----------------------------|
| Levenshtein                                |    ☒     |     ☒      |     ☐      |   ☒    | \\(O(n)\\)           | \\(O(m \\times n)\\)        |
| Longest Common Subsequence                 |    ☒     |     ☒      |     ☐      |   ☒    | \\(O(n)\\)           | \\(O(m \\times n)\\)[@ft-a] |
| Damerau-Levenshtein[@ft-b]                 |    ☒     |     ☒      |     ☐      |   ☒    | \\(O(m \\times n)\\) | \\(O(m \\times n)\\)        |
| Optimal String Alignment[@ft-b]            |    ☒     |     ☒      |     ☐      |   ☐    | \\(O(m \\times n)\\) | \\(O(m \\times n)\\)        |
| Normalized Levenshtein                     |    ☒     |     ☒      |     ☒      |   ☒    | \\(O(n)\\)           | \\(O(m \\times n)\\)        |
| Normalized Longest Common Subsequence      |    ☒     |     ☒      |     ☒      |   ☒    | \\(O(n)\\)           | \\(O(m \\times n)\\)[@ft-a] |
| Normalized Damerau-Levenshtein[@ft-b]      |    ☒     |     ☒      |     ☒      |   ☒    | \\(O(m \\times n)\\) | \\(O(m \\times n)\\)        |
| Normalized Optimal String Alignment[@ft-b] |    ☒     |     ☒      |     ☒      |   ☐    | \\(O(m \\times n)\\) | \\(O(m \\times n)\\)        |
| Cosine similarity                          |    ☒     |     ☒      |     ☒      |   ☐    | \\(O(m + n)\\)       | \\(O(m + n)\\)              |
| Jaccard index                              |    ☒     |     ☒      |     ☒      |   ☒    | \\(O(m + n)\\)       | \\(O(m + n)\\)              |
| Jaro-Winkler                               |    ☒     |     ☒      |     ☒      |   ☐    | \\(O(m + n)\\)       | \\(O(m \\times n)\\)        |
| N-Gram                                     |    ☒     |     ☐      |     ☒      |   ☐    |                      | \\(O(m \\times n)\\)        |
| Q-Gram                                     |    ☒     |     ☐      |     ☐      |   ☐    |                      | \\(O(m + n)\\)              |
| Ratcliff-Obershelp                         |    ☒     |     ☒      |     ☒      |   ☐    | \\(O(m + n)\\)       | \\(O(n^3)\\)                |
| Sorensen-Dice coefficient                  |    ☒     |     ☒      |     ☒      |   ☐    |                      | \\(O(m + n)\\)              |
| Sift 4                                     |    ☒     |     ☐      |     ☐      |   ☐    | \\(O(m + n)\\)       | \\(O(m + n)\\)              |

<h2 class="footnotes-header">Notes</h2>
<div class="footnotes">
<ol>
<li id="footnote-a">

K.S. Larsen proposed an algorithm that computes the length of LCS in time
\\(O(log(m) \\times log(n))\\).[@ref-4] But the algorithm has a memory requirement \\(O(m \\times n^2)\\) and was thus not
implemented here.
</li>
<li id="footnote-b">

There are two variants of Damerau-Levenshtein string distance: Damerau-Levenshtein with adjacent transpositions
(also sometimes called unrestricted Damerau–Levenshtein distance) and Optimal String Alignment (also sometimes called
restricted edit distance). For Optimal String Alignment, no substring can be edited more than once.
</li>
</ol>
</div>

<h2 class="references-header">References</h2>
<div class="references">
<ol>
<li id="reference-1">

Wagner, R. A., & Fischer, M. J. (1974-01). The string-to-string correction problem.
Journal of the ACM, 21(1), 168–173.
<https://doi.org/10.1145/321796.321811><sup>[&#91;sci-hub&#93;](https://sci-hub.st/10.1145/321796.321811)</sup>
</li>
<li id="reference-2">

Arlazarov, V. L., Dinitz, Y. A., Kronrod, M. A., & Faradzhev, I. (1970).
An algorithm for the reduction of finite non-oriented graphs to canonical form.
*Soviet Mathematics Doklady*, *194*(3), 487-488.
</li>
<li id="reference-3">

Masek, W. J., & Paterson, M. S. (1980-02). A faster algorithm computing string
edit distances. *Journal of Computer and System Sciences*, *20*(1), 18-31.
<https://doi.org/10.1016/0022-0000(80)90002-1><sup>[&#91;sci-hub&#93;](https://sci-hub.st/10.1016/0022-0000(80)90002-1)</sup>
</li>
<li id="reference-4">

Larsen, K. S. (1992-10). Length of maximal common subsequences. DAIMI Report
Series, 21(426).
<https://doi.org/10.7146/dpb.v21i426.6740><sup>[&#91;sci-hub&#93;](https://sci-hub.st/10.7146/dpb.v21i426.6740)</sup>
</li>
</ol>
</div>

