/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Levenshtein.kt is part of kotlin-fuzzy
 * Last modified on 02-08-2023 12:34 a.m.
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

package ca.solostudios.stringsimilarity.edit

import ca.solostudios.stringsimilarity.interfaces.MetricStringDistance
import ca.solostudios.stringsimilarity.interfaces.StringDistance
import ca.solostudios.stringsimilarity.interfaces.StringEditMeasure
import ca.solostudios.stringsimilarity.interfaces.StringSimilarity

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
 * **Note: Because this class currently implements the dynamic programming approach,
 * it has a space requirement \(O(m \times n)\)**
 *
 * @param insertionWeight The weight of an insertion. Represented as \(w_i\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param deletionWeight The weight of a deletion. Represented as \(w_d\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param substitutionWeight The weight of a substitution. Represented as \(w_s\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 *
 * @see StringEditMeasure
 * @see MetricStringDistance
 * @see StringDistance
 * @see StringSimilarity
 *
 * @author Thibault Debatty, solonovamax
 */
public class Levenshtein(
    insertionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    deletionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    substitutionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
) : AbstractStringEditMeasure(
    insertionWeight = insertionWeight,
    deletionWeight = deletionWeight,
    substitutionWeight = substitutionWeight,
) {
    override fun fillCostMatrix(costMatrix: Array<DoubleArray>, shorter: String, longer: String) {
        longer.forEachIndexed { i, c1 ->
            shorter.forEachIndexed { j, c2 ->
                val deletionCost = costMatrix[i][j + 1] + deletionWeight
                val insertionCost = costMatrix[i + 1][j] + insertionWeight
                val substitutionCost = costMatrix[i][j] + if (c1 == c2) 0.0 else substitutionWeight

                costMatrix[i + 1][j + 1] = minOf(deletionCost, insertionCost, substitutionCost)
            }
        }
    }

    /**
     * Default Levenshtein instance
     */
    public companion object : MetricStringDistance, StringDistance, StringSimilarity {
        private val defaultMeasure = Levenshtein()
        override fun distance(s1: String, s2: String): Double = defaultMeasure.distance(s1, s2)
        override fun similarity(s1: String, s2: String): Double = defaultMeasure.similarity(s1, s2)
    }
}
