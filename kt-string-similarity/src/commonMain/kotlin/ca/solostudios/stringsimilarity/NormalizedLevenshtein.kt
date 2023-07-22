/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NormalizedLevenshtein.kt is part of kotlin-fuzzy
 * Last modified on 22-07-2023 04:39 p.m.
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
 * KT-FUZZY IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ca.solostudios.stringsimilarity

import ca.solostudios.stringsimilarity.interfaces.MetricStringDistance
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity

/**
 * A normalized metric based the Levenshtein distance, as defined by
 * L. Yujian and L. Bo, "A Normalized Levenshtein Distance Metric",
 * in IEEE Transactions on Pattern Analysis and Machine Intelligence,
 * vol. 29, no. 6, pp. 1091-1095, June 2007, doi: 10.1109/TPAMI.2007.1078.
 * <sup>[&#91;sci-hub&#93;](https://sci-hub.st/https://ieeexplore.ieee.org/document/4160958)</sup>
 *
 * The normalized Levenshtein distance between Strings \(X\) and \(Y\) is:
 * \(\frac{2 \times distance_{levenshtein}(X, Y)}{w_d \lvert X \rvert + w_i \lvert Y \rvert + distance_{levenshtein}(X, Y)}\).
 *
 * The similarity is computed as
 * \(1 - distance(X, Y)\).
 *
 * @param limit The maximum result to compute before stopping, before normalization.
 * @param insertionWeight The weight of an insertion. Represented as \(w_i\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param deletionWeight The weight of a deletion. Represented as \(w_d\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param substitutionWeight The weight of a substitution. Represented as \(w_s\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 *
 * @author Thibault Debatty, solonovamax
 *
 * @see Levenshtein
 * @see MetricStringDistance
 * @see NormalizedStringDistance
 * @see NormalizedStringSimilarity
 */
public class NormalizedLevenshtein(
    /**
     * The maximum result to compute before stopping. This
     * means that the calculation can terminate early if you
     * only care about strings with a certain similarity.
     * Set this to [Double.MAX_VALUE] if you want to run the
     * calculation to completion in every case.
     */
    public val limit: Double = Double.MAX_VALUE,
    /**
     * The weight of an insertion. Represented as \(w_i\).
     */
    public val insertionWeight: Double = 1.0,
    /**
     * The weight of a deletion. Represented as \(w_d\).
     */
    public val deletionWeight: Double = 1.0,
    /**
     * The weight of a substitution. Represented as \(w_s\).
     */
    public val substitutionWeight: Double = 1.0,
) : MetricStringDistance, NormalizedStringDistance, NormalizedStringSimilarity {
    init {
        // 1E10 is a reasonable upper limit
        require(insertionWeight > 0 && insertionWeight < 1E10)
        require(deletionWeight > 0 && deletionWeight < 1E10)
        require(deletionWeight > 0 && deletionWeight < 1E10)
    }

    private val levenshtein: Levenshtein = Levenshtein(limit, deletionWeight, insertionWeight, substitutionWeight)

    /**
     * Computes the normalized Levenshtein distance of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Levenshtein distance.
     * @see MetricStringDistance
     * @see NormalizedStringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty() || s2.isEmpty())
            return 1.0

        val levenshteinDistance = levenshtein.distance(s1, s2)
        return (2 * levenshteinDistance) / (deletionWeight * s1.length + insertionWeight * s2.length + levenshteinDistance)
    }

    /**
     * Computes the normalized Levenshtein similarity of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Levenshtein similarity.
     * @see NormalizedStringSimilarity
     */
    override fun similarity(s1: String, s2: String): Double {
        return 1.0 - distance(s1, s2)
    }
}
