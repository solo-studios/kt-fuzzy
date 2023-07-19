/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NormalizedLCS.kt is part of kotlin-fuzzy
 * Last modified on 18-07-2023 09:48 p.m.
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
import ca.solostudios.stringsimilarity.util.maxLength

/**
 * Distance metric based on Longest Common Subsequence, from the notes
 * "An LCS-based string metric" by Daniel Bakkelund.
 *
 * The normalized LCS distance between Strings \(X\) and \(Y\) is:
 * \(1 - \frac{\lvert LCS(X, Y) \rvert}{max(\lvert X \rvert, \vert Y \rvert)}\).
 *
 * The similarity is \(1.0 - distance(X, Y)\).
 *
 * @author Thibault Debatty, solonovamax
 * @see LCS
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

        return 1.0 - (lcs.lcsLength(s1, s2).toDouble() / maxLength(s1, s2))
    }

    override fun similarity(s1: String, s2: String): Double {
        return 1.0 - distance(s1, s2)
    }
}
