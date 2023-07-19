/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file WeightedLevenshtein.kt is part of kotlin-fuzzy
 * Last modified on 18-07-2023 09:26 p.m.
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
import kotlin.math.min

/**
 * Implementation of Levenshtein that allows to define different weights for
 * different character substitutions.
 * Instantiate with provided character substitution, insertion, and
 * deletion weights.
 *
 * @param charSubstitutionWeight The strategy to determine character substitution weights.
 * @param charInsertionDeletionWeight The strategy to determine character insertion/deletion weights.
 * @author Thibault Debatty, solonovamax
 */
public class WeightedLevenshtein(
    public val charSubstitutionWeight: (Char, Char) -> Double,
    public val charInsertionDeletionWeight: (Char) -> Weights = { Weights(1.0, 1.0) },
) : StringDistance {

    override fun distance(s1: String, s2: String): Double = distance(s1, s2, limit = Double.MAX_VALUE)

    /**
     * Compute Levenshtein distance using provided weights for substitution.
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @param limit The maximum result to compute before stopping. This
     * means that the calculation can terminate early if you
     * only care about strings with a certain similarity.
     * Set this to Double.MAX_VALUE if you want to run the
     * calculation to completion in every case.
     * @return The computed weighted Levenshtein distance.
     */
    public fun distance(s1: String, s2: String, limit: Double = Double.MAX_VALUE): Double {
        if (s1 == s2) {
            return 0.0
        }
        if (s1.isEmpty()) {
            return s2.length.toDouble()
        }
        if (s2.isEmpty()) {
            return s1.length.toDouble()
        }

        // create two work vectors of floating point (i.e. weighted) distances
        var v0 = DoubleArray(s2.length + 1)
        var v1 = DoubleArray(s2.length + 1)
        var vtemp: DoubleArray

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s1
        // the distance is the cost of inserting each character of s2
        v0[0] = 0.0
        for (i in 1 until v0.size) {
            v0[i] = v0[i - 1] + charInsertionDeletionWeight(s2[i - 1]).insertionWeight
        }

        for (element in s1) {
            val s1i = element
            val deletionCost = charInsertionDeletionWeight(s1i).deletionWeight

            // calculate v1 (current row distances) from the previous row v0
            // first element of v1 is A[i+1][0]
            // Edit distance is the cost of deleting characters from s1
            // to match empty t.
            v1[0] = v0[0] + deletionCost
            var minv1 = v1[0]

            // use formula to fill in the rest of the row
            for (j in s2.indices) {
                val s2j = s2[j]
                var cost = 0.0
                if (s1i != s2j) {
                    cost = charSubstitutionWeight(s1i, s2j)
                }
                val insertionCost = charInsertionDeletionWeight(s2j).insertionWeight

                v1[j + 1] = min(
                    v1[j] + insertionCost,  // Cost of insertion
                    min(
                        v0[j + 1] + deletionCost,  // Cost of deletion
                        v0[j] + cost, // Cost of substitution
                    )
                )
                minv1 = min(minv1, v1[j + 1])
            }
            if (minv1 >= limit) {
                return limit
            }

            // copy v1 (current row) to v0 (previous row) for next iteration
            // System.arraycopy(v1, 0, v0, 0, v0.length);
            // Flip references to current and previous row
            vtemp = v0
            v0 = v1
            v1 = vtemp
        }
        return v0[s2.length]
    }

    public data class Weights(val deletionWeight: Double, val insertionWeight: Double)
}
