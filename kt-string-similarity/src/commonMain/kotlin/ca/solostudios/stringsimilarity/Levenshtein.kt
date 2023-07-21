/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Levenshtein.kt is part of kotlin-fuzzy
 * Last modified on 21-07-2023 06:00 p.m.
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
import ca.solostudios.stringsimilarity.interfaces.StringDistance
import ca.solostudios.stringsimilarity.interfaces.StringSimilarity
import ca.solostudios.stringsimilarity.util.minMaxByLength

/**
 * The Levenshtein distance, or edit distance, between two words is the
 * minimum number of single-character edits (insertions, deletions, or
 * substitutions) required to change one word into the other.
 *
 * [Levenshtein distance](http://en.wikipedia.org/wiki/Levenshtein_distance)
 *
 * - It is always at least the difference of the sizes of the two strings.
 * - It is at most the length of the longer string.
 * - It is zero if and only if the strings are equal.
 * - If the strings are the same size, the Hamming distance is an upper bound
 *   on the Levenshtein distance.
 * - The Levenshtein distance obeys the triangle inequality (the distance
 *   between two strings is no greater than the sum Levenshtein distances from
 *   a third string), so it is a [metric distance][MetricStringDistance].
 *
 * The similarity is computed as
 * \(\frac{w_d \lvert X \rvert + w_i \lvert Y \rvert - distance(X, Y)}{2}\).
 *
 * Implementation uses dynamic programming (Wagner–Fischer algorithm), with
 * only 2 rows of data. The space requirement is thus \(O(m)\) and the algorithm
 * runs in \(O(m \times n)\).
 *
 * @param limit The maximum result to compute before stopping.
 * @param insertionWeight The weight of an insertion. Represented as \(w_i\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param deletionWeight The weight of a deletion. Represented as \(w_d\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param substitutionWeight The weight of a substitution. Represented as \(w_s\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 *
 * @author Thibault Debatty, solonovamax
 *
 * @see MetricStringDistance
 * @see StringDistance
 * @see StringSimilarity
 */
public class Levenshtein(
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
) : MetricStringDistance, StringDistance, StringSimilarity {
    init {
        // 1E10 is a reasonable upper limit
        require(insertionWeight > 0 && insertionWeight < 1E10)
        require(deletionWeight > 0 && deletionWeight < 1E10)
        require(deletionWeight > 0 && deletionWeight < 1E10)
    }

    /**
     * Computes the Levenshtein distance of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The Levenshtein distance.
     * @see MetricStringDistance
     * @see StringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty())
            return s2.length * insertionWeight
        if (s2.isEmpty())
            return s1.length * deletionWeight

        val (shorter, longer) = minMaxByLength(s1, s2)

        var v0 = DoubleArray(longer.length + 1) { it * deletionWeight }
        var v1 = DoubleArray(longer.length + 1)

        shorter.forEachIndexed { i, c1 ->
            v1[0] = (i + 1) * deletionWeight

            var currentMin = v1[0]
            longer.forEachIndexed { j, c2 ->
                val deletionCost = v0[j + 1] + deletionWeight
                val insertionCost = v1[j] + insertionWeight
                val substitutionCost = if (c1 == c2) 0.0 else substitutionWeight

                v1[j + 1] = minOf(deletionCost, insertionCost, v0[j] + substitutionCost)
                currentMin = minOf(v1[j + 1], currentMin)
            }

            if (currentMin >= limit)
                return limit

            val tmp = v0
            v0 = v1
            v1 = tmp
        }

        return v0[longer.length]
    }

    /**
     * Computes the Levenshtein similarity of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The Levenshtein similarity.
     * @see StringSimilarity
     */
    override fun similarity(s1: String, s2: String): Double {
        return ((insertionWeight * s1.length + deletionWeight * s2.length) - distance(s1, s2)) / 2
    }
}
