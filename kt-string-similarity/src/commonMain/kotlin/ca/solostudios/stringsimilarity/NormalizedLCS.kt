/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NormalizedLCS.kt is part of kotlin-fuzzy
 * Last modified on 21-07-2023 05:56 p.m.
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
import ca.solostudios.stringsimilarity.interfaces.StringDistance
import ca.solostudios.stringsimilarity.interfaces.StringSimilarity

/**
 * A normalized metric based on Longest Common Subsequence, as defined by
 * L. Yujian and L. Bo, "A Normalized Levenshtein Distance Metric",
 * in IEEE Transactions on Pattern Analysis and Machine Intelligence,
 * vol. 29, no. 6, pp. 1091-1095, June 2007, doi: 10.1109/TPAMI.2007.1078.
 * <sup>[&#91;sci-hub&#93;](https://sci-hub.st/https://ieeexplore.ieee.org/document/4160958)</sup>
 *
 * The normalized LCS distance between Strings \(X\) and \(Y\) is:
 * \(\frac{2 \times distance_{LCS}(X, Y)}{\lvert X \rvert + \lvert Y \rvert + distance_{LCS}(X, Y)}\),
 * where \(GLD(X, Y)\) is the non-normalized LCS distance.
 *
 * The similarity is \(1.0 - distance(X, Y)\).
 *
 * **Note: Because this class uses [LCS] internally,
 * which implements the dynamic programming approach,
 * it has a space requirement \(O(m \times n)\)**
 *
 * @author Thibault Debatty, solonovamax
 *
 * @see LCS
 * @see MetricStringDistance
 * @see StringDistance
 * @see StringSimilarity
 */
public class NormalizedLCS : MetricStringDistance, NormalizedStringDistance, NormalizedStringSimilarity {
    private val lcs = LCS()

    /**
     * Computes the Longest Common Subsequence distance of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Longest Common Subsequence distance.
     * @see MetricStringDistance
     * @see StringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty() || s2.isEmpty())
            return 1.0

        return (2 * lcs.distance(s1, s2)) / (s1.length + s2.length + lcs.distance(s1, s2))
    }

    /**
     * Computes the Longest Common Subsequence similarity of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Longest Common Subsequence similarity.
     * @see StringSimilarity
     */
    override fun similarity(s1: String, s2: String): Double {
        return 1.0 - distance(s1, s2)
    }
}