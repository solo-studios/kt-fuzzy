/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file SorensenDice.kt is part of kotlin-fuzzy
 * Last modified on 09-08-2023 11:14 p.m.
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

import ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity

/**
 * Sorensen-Dice coefficient, aka Sørensen index, Dice's coefficient or
 * Czekanowski's binary (non-quantitative) index.
 *
 * The strings are first converted to boolean sets of k-shingles (sequences
 * of k characters), then the similarity is computed as 2 * |A inter B| /
 * (|A| + |B|). Attention: Sorensen-Dice distance (and similarity) does not
 * satisfy triangle inequality.
 *
 * Similar to Jaccard index, but this time the similarity is computed as 2 * |V1
 * inter V2| / (|V1| + |V2|). Distance is computed as 1 - cosine similarity.
 *
 * @author Thibault Debatty, solonovamax
 */
public class SorensenDice(k: Int = DEFAULT_K) : ShingleBased(k), NormalizedStringDistance, NormalizedStringSimilarity {
    /**
     * Similarity is computed as 2 * |A inter B| / (|A| + |B|).
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed Sorensen-Dice similarity.
     */
    override fun similarity(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty() || s2.isEmpty())
            return 1.0

        val profile1 = profile(s1)
        val profile2 = profile(s2)

        val inter = (profile1.keys intersect profile2.keys).size
        return 2.0 * inter / (profile1.size + profile2.size)
    }

    /**
     * Returns 1 - similarity.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1.0 - the computed similarity
     * @throws NullPointerException if s1 or s2 is null.
     */
    override fun distance(s1: String, s2: String): Double {
        return 1 - similarity(s1, s2)
    }

    /**
     * Default Sorensen Dice instance
     */
    public companion object : NormalizedStringDistance, NormalizedStringSimilarity {
        private val defaultMeasure = SorensenDice()
        override fun distance(s1: String, s2: String): Double = defaultMeasure.distance(s1, s2)
        override fun similarity(s1: String, s2: String): Double = defaultMeasure.similarity(s1, s2)
    }
}
