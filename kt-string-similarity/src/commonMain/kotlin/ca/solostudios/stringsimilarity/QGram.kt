/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file QGram.kt is part of kotlin-fuzzy
 * Last modified on 02-09-2023 12:16 a.m.
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

import ca.solostudios.stringsimilarity.interfaces.StringDistance
import kotlin.math.abs

/**
 * Q-gram distance, as defined by
 * Esko Ukkonen. Bo, "Approximate string-matching with q-grams and maximal matches",
 * in Theoretical Computer Science,
 * vol. 92, no. 1, pp. 191-211, Elsevier BV, Jan. 1992, pp. 191–211, doi: 10.1016/0304-3975(92)90143-4.
 * <sup>[&#91;sci-hub&#93;](https://sci-hub.st/https://doi.org/10.1016/0304-3975(92)90143-4)</sup>
 * The distance between two strings is defined as
 * the number of occurrences of different q-grams in each string:
 * \(\sum_{i=1}^n \lVert \vec{v1_i} - \vec{v2_i} \rVert\).
 * Q-gram distance is a lower bound on Levenshtein distance, but can be computed in \(O(m + n)\),
 * whereas the Levenshtein distance has a time complexity of \(O(m \times n)\).
 *
 * This distance measure is pseudo-metric.
 * It is not a metric because two non-identical strings can have identical q-gram profiles,
 * resulting in \(distance(X, Y) = 0\) where \(X \neq Y\).
 * However, it does respect the other 3 axioms.
 *
 * @param q The length of each q-gram.
 *
 * @throws IllegalArgumentException if \(k \leqslant 0\)
 *
 * @author Thibault Debatty, solonovamax
 */
public class QGram(q: Int = DEFAULT_K) : ShingleBased(q), StringDistance {
    /**
     * Computes the Q-gram distance of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Q-gram similarity.
     * @see StringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0

        val profile1 = profile(s1)
        val profile2 = profile(s2)

        return distance(profile1, profile2)
    }

    /**
     * Computes the Q-gram distance of precomputed profiles.
     *
     * @param profile1 The profile of the first string.
     * @param profile2 The profile of the second string.
     * @return The normalized Q-gram distance.
     * @see StringDistance
     */
    public fun distance(
        profile1: Map<String, Int>,
        profile2: Map<String, Int>,
    ): Double {
        val union = profile1.keys union profile2.keys

        var agg = 0
        for (key in union) {
            val v1 = profile1[key] ?: 0
            val v2 = profile2[key] ?: 0
            agg += abs(v1 - v2)
        }
        return agg.toDouble()
    }

    /**
     * Default QGram instance
     */
    public companion object : StringDistance {
        private val defaultMeasure = QGram()
        override fun distance(s1: String, s2: String): Double = defaultMeasure.distance(s1, s2)
    }
}
