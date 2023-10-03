# Package ca.solostudios.stringsimilarity.interfaces

This package contains all the interfaces for string measures.

## Normalized, metric, similarity and distance

Although the topic might seem simple, a lot of different algorithms exist to measure text similarity or distance.
Therefore, the library defines some interfaces to categorize them.

### (Normalized) Similarity and Distance

- [StringSimilarity][ca.solostudios.stringsimilarity.interfaces.StringSimilarity]: Implementing algorithms define a
  similarity between
  strings (0 means strings are completely different).
- [NormalizedStringSimilarity][ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity]: The interface
  extends [StringSimilarity][ca.solostudios.stringsimilarity.interfaces.StringSimilarity].
  Implementing algorithms compute a similarity that has been normalized based on the number of operations performed.
  This means that for non-weighted implementations, the result will always be between 0 and 1.
  [Jaro-Winkler][ca.solostudios.stringsimilarity.JaroWinkler] is an example of this.
- [StringDistance][ca.solostudios.stringsimilarity.interfaces.StringDistance]: Implementing algorithms define a distance
  between strings (0 means strings are identical), like [Levenshtein][ca.solostudios.stringsimilarity.edit.Levenshtein] for example.
  The maximum distance value depends on the algorithm.
- [NormalizedStringDistance][ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance]: This interface
  extends [StringDistance][ca.solostudios.stringsimilarity.interfaces.StringDistance].
  Implementing algorithms compute a distance that has been normalized based on the number of operations performed.
  This means that for non-weighted implementations, the result will always be between \\(&#91;0, 1&#93;\\).
  [NormalizedLevenshtein][ca.solostudios.stringsimilarity.edit.NormalizedLevenshtein] is an example of this.

Generally, algorithms that
implement [NormalizedStringSimilarity][ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity]
also implement [NormalizedStringDistance][ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance].
This is because the similarity can be computed as \\(1 - \\text{distance}\\),
and the distance can be computed as \\(1 - \\text{similarity}\\).

> Note: This is only applicable if the result is *always* between 0 and 1.

### Metric Distances

The [MetricStringDistance][ca.solostudios.stringsimilarity.interfaces.MetricStringDistance]
interface indicates that the implementing class is a metric distance,
which means that it satisfies the required axioms to be considered metric.
Read [MetricStringDistance][ca.solostudios.stringsimilarity.interfaces.MetricStringDistance] for more information.

A lot of nearest-neighbor search algorithms and indexing structures rely on the triangle inequality.
You can check "Similarity Search, The Metric Space Approach" by Zezula et al. for a survey.
These cannot be used with non-metric similarity measures.

### Edit Measures

The edit measure interfaces indicate when a specific algorithm is edit-based.
See the `edit` package for all implementors.
