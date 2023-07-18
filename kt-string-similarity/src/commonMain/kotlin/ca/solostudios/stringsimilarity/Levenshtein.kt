/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Levenshtein.kt is part of kotlin-fuzzy
 * Last modified on 18-07-2023 07:27 p.m.
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
import ca.solostudios.stringsimilarity.util.maxLength
import kotlin.math.min

/**
 * The Levenshtein distance between two words is the minimum number of
 * single-character edits (insertions, deletions, or substitutions) required to
 * change one string into the other.
 *
 * @param limit The maximum result to compute before stopping.
 * @author Thibault Debatty
 */
public class Levenshtein(
    /**
     * The maximum result to compute before stopping. This
     * means that the calculation can terminate early if you
     * only care about strings with a certain similarity.
     * Set this to Integer.MAX_VALUE if you want to run the
     * calculation to completion in every case.
     */
    public val limit: Int = Int.MAX_VALUE,
) : MetricStringDistance, StringDistance, StringSimilarity {
    /**
     * The Levenshtein distance, or edit distance, between two words is the
     * minimum number of single-character edits (insertions, deletions, or
     * substitutions) required to change one word into the other.
     *
     * [Levenshtein distance](http://en.wikipedia.org/wiki/Levenshtein_distance)
     *
     * It is always at least the difference of the sizes of the two strings.
     * It is at most the length of the longer string.
     * It is zero if and only if the strings are equal.
     * If the strings are the same size, the Hamming distance is an upper bound
     * on the Levenshtein distance.
     * The Levenshtein distance verifies the triangle inequality (the distance
     * between two strings is no greater than the sum Levenshtein distances from
     * a third string).
     *
     * Implementation uses dynamic programming (Wagner–Fischer algorithm), with
     * only 2 rows of data. The space requirement is thus \(O(m)\) and the algorithm
     * runs in \(O(mn)\).
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed Levenshtein distance.
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2) {
            return 0.0
        }
        if (s1.isEmpty()) {
            return s2.length.toDouble()
        }
        if (s2.isEmpty()) {
            return s1.length.toDouble()
        }

        // create two work vectors of integer distances

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        var v0 = IntArray(s2.length + 1) { it }
        var v1 = IntArray(s2.length + 1)
        var vtemp: IntArray

        s1.forEachIndexed { i, c1 ->
            // calculate v1 (current row distances) from the previous row v0
            // first element of v1 is A[i+1][0]
            //   edit distance is delete (i+1) chars from s to match empty t
            v1[0] = i + 1
            var minv1 = v1[0]

            // use formula to fill in the rest of the row
            s2.forEachIndexed { j, c2 ->
                val cost = if (c1 == c2) 0 else 1

                v1[j + 1] = min(
                    v1[j] + 1,  // Cost of insertion
                    min(
                        v0[j + 1] + 1,  // Cost of remove
                        v0[j] + cost, // Cost of substitution
                    )
                )
                minv1 = min(minv1, v1[j + 1])
            }

            if (minv1 >= limit) {
                return limit.toDouble()
            }

            // Flip references to current and previous row
            vtemp = v0
            v0 = v1
            v1 = vtemp
        }

        return v0[s2.length].toDouble()
    }

    /**
     * The similarity is \(\text{length of longest string}- 1\).
     *
     * The Levenshtein distance, or edit distance, between two words is the
     * minimum number of single-character edits (insertions, deletions, or
     * substitutions) required to change one word into the other.
     *
     * [Levenshtein distance](http://en.wikipedia.org/wiki/Levenshtein_distance)
     *
     * It is always at least the difference of the sizes of the two strings.
     * It is at most the length of the longer string.
     * It is zero if and only if the strings are equal.
     * If the strings are the same size, the Hamming distance is an upper bound
     * on the Levenshtein distance.
     * The Levenshtein distance verifies the triangle inequality (the distance
     * between two strings is no greater than the sum Levenshtein distances from
     * a third string).
     *
     * Implementation uses dynamic programming (Wagner–Fischer algorithm), with
     * only 2 rows of data. The space requirement is thus \(O(m)\) and the algorithm
     * runs in \(O(mn)\).
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed Levenshtein similarity.
     */
    override fun similarity(s1: String, s2: String): Double {
        return (maxLength(s1, s2) - distance(s1, s2))
    }
}
