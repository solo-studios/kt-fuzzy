/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file SorensenDice.kt is part of kotlin-fuzzy
 * Last modified on 04-09-2023 07:33 p.m.
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
 * Implements the Sørensen-Dice coefficient, also known as Sørensen index (Sørensen, 1948), Dice's coefficient, or
 * Czekanowski's binary (non-quantitative) index between strings.
 *
 * The strings are first converted to boolean sets of k-shingles (sequences
 * of k characters), then the similarity is computed as
 * \(\frac{2 \times \lVert V_1 \cap V_2 \rVert}{\lVert V_1 \rVert + \lVert V_2 \rVert}\)
 *
 * Similar to Jaccard index, but this time the similarity is computed as 2 * |V1
 * inter V2| / (|V1| + |V2|). Distance is computed as 1 - cosine similarity.
 *
 * The distance is computed as
 * \(1 - similarity(X, Y)\).
 *
 * #### References
 * Sørensen, T. J. (1948). A method of establishing group of equal amplitude in plant
 * sociobiology based on similarity of species content and its application to
 * analyses of the vegetation on danish commons.
 * *Kongelige Danske Videnskabernes Selskab.*
 *
 * @author Thibault Debatty, solonovamax
 */
public class SorensenDice(k: Int = DEFAULT_K) : ShingleBased(k), NormalizedStringDistance, NormalizedStringSimilarity {
    /**
     * Computes the Sørensen-Dice similarity of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Sørensen-Dice similarity.
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
     * Computes the Sørensen-Dice distance of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Sørensen-Dice distance.
     * @see NormalizedStringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.length < k || s2.length < k)
            return 1.0

        return distance(profile(s1), profile(s2))
    }

    /**
     * Computes the Sørensen-Dice similarity of precomputed profiles.
     *
     * @param profile1 The profile of the first string.
     * @param profile2 The profile of the second string.
     * @return The normalized Sørensen-Dice similarity.
     * @see NormalizedStringSimilarity
     */
    public fun similarity(profile1: Map<String, Int>, profile2: Map<String, Int>): Double {
        if (profile1.isEmpty() && profile2.isEmpty())
            return 0.0 // if they're both empty, it causes problems

        val inter = (profile1.keys intersect profile2.keys).size
        return 2.0 * inter / (profile1.size + profile2.size)
    }

    /**
     * Computes the Sørensen-Dice distance of precomputed profiles.
     *
     * @param profile1 The profile of the first string.
     * @param profile2 The profile of the second string.
     * @return The normalized Sørensen-Dice distance.
     * @see NormalizedStringDistance
     */
    public fun distance(profile1: Map<String, Int>, profile2: Map<String, Int>): Double {
        return 1.0 - similarity(profile1, profile2)
    }
}
