/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Jaccard.kt is part of kotlin-fuzzy
 * Last modified on 04-09-2023 07:31 p.m.
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
 * Implements the Jaccard index, also known as the Jaccard similarity coefficient (Jaccard, 1912).
 *
 * Each input string is converted into a set of n-grams, the Jaccard index is
 * then computed as \(\frac{\lVert V_1 \cap V_2 \rVert}{\lVert V_1 \cup V_2 \rVert}\).
 * Like Q-Gram distance, the input strings \(X\) and \(Y\) are first converted into sets of
 * n-grams \(V_1\) and \(V_2\) (sequences of n characters, also called k-shingles), but this time
 * the cardinality of each n-gram is not taken into account.
 *
 * The distance is computed as
 * \(1 - similarity(X, Y)\).
 *
 * #### References
 * Jaccard, P. (1912-02). The distribution of the flora in the alpine zone.
 * *New Phytologist*, *11*(2), 37–50.
 * <https://doi.org/10.1111/j.1469-8137.1912.tb05611.x><sup>[&#91;sci-hub&#93;](https://sci-hub.st/10.1111/j.1469-8137.1912.tb05611.x)</sup>
 *
 * @see MetricStringDistance
 * @see NormalizedStringDistance
 * @see NormalizedStringSimilarity
 * @see ShingleBased
 *
 * @author Thibault Debatty, solonovamax
 */
public class Jaccard(k: Int = DEFAULT_K) : ShingleBased(k), MetricStringDistance, NormalizedStringDistance, NormalizedStringSimilarity {
    /**
     * Computes the Jaccard similarity of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Jaccard similarity.
     * @see NormalizedStringSimilarity
     */
    override fun similarity(s1: String, s2: String): Double {
        if (s1 == s2)
            return 1.0
        if (s1.isEmpty() || s2.isEmpty())
            return 0.0

        return similarity(profile(s1), profile(s2))
    }

    /**
     * Computes the Jaccard distance of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Jaccard distance.
     * @see MetricStringDistance
     * @see NormalizedStringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        return 1.0 - similarity(s1, s2)
    }

    /**
     * Computes the Jaccard similarity of precomputed profiles.
     *
     * @param profile1 The profile of the first string.
     * @param profile2 The profile of the second string.
     * @return The normalized Jaccard similarity.
     * @see NormalizedStringSimilarity
     */
    public fun similarity(profile1: Map<String, Int>, profile2: Map<String, Int>): Double {
        if (profile1.isEmpty() && profile2.isEmpty())
            return 0.0 // if they're both empty, it causes problems

        val union = profile1.keys union profile2.keys

        val inter = profile1.keys.size + profile2.keys.size - union.size
        return 1.0 * inter / union.size
    }

    /**
     * Computes the Jaccard distance of precomputed profiles.
     *
     * @param profile1 The profile of the first string.
     * @param profile2 The profile of the second string.
     * @return The normalized Jaccard distance.
     * @see MetricStringDistance
     * @see NormalizedStringDistance
     */
    public fun distance(profile1: Map<String, Int>, profile2: Map<String, Int>): Double {
        return 1.0 - similarity(profile1, profile2)
    }

    /**
     * Default Jaccard instance
     */
    public companion object : NormalizedStringDistance, NormalizedStringSimilarity, MetricStringDistance {
        private val defaultMeasure = Jaccard()
        override fun distance(s1: String, s2: String): Double = defaultMeasure.distance(s1, s2)
        override fun similarity(s1: String, s2: String): Double = defaultMeasure.similarity(s1, s2)
    }
}
