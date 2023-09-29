/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LCS.kt is part of kotlin-fuzzy
 * Last modified on 31-08-2023 04:33 p.m.
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
import kotlin.math.min

/**
 * Implements the Longest Common Subsequence (LCS) problem consists in finding the longest
 * subsequence common to two (or more) sequences. It differs from problems of
 * finding common substrings: unlike substrings, subsequences are not required
 * to occupy consecutive positions within the original sequences.
 *
 * It is used by the diff utility, by Git for reconciling multiple changes, etc.
 *
 * The LCS edit distance between strings \(X\) and \(Y\) is:
 * \(\lvert X \rvert + \lvert Y \rvert - 2 \times \lvert LCS(X, Y) \rvert\)
 * - \(\text{min} = 0\)
 * - \(\text{max} = \lvert X \rvert + \lvert Y \rvert\).
 *
 * LCS distance is equivalent to Levenshtein distance, when only insertion and
 * deletion is allowed (no substitution), or when the cost of the substitution
 * is the double of the cost of an insertion or deletion.
 *
 * The similarity is computed as
 * \(\frac{w_d \lvert X \rvert + w_i \lvert Y \rvert - distance(X, Y)}{2}\).
 *
 * **Note: Because this class currently implements the dynamic programming approach,
 * it has a space requirement \(O(m \times n)\)**
 *
 * @param insertionWeight The weight of an insertion. Represented as \(w_i\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param deletionWeight The weight of a deletion. Represented as \(w_d\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 *
 * @see StringEditMeasure
 * @see MetricStringDistance
 * @see StringDistance
 * @see StringSimilarity
 *
 * @author Thibault Debatty, solonovamax
 */
public class LCS(
    insertionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    deletionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
) : AbstractStringEditMeasure(
    insertionWeight = insertionWeight,
    deletionWeight = deletionWeight,
) {
    override fun fillCostMatrix(costMatrix: Array<DoubleArray>, shorter: String, longer: String) {
        longer.forEachIndexed { i, c1 ->
            shorter.forEachIndexed { j, c2 ->
                val deletionCost = costMatrix[i][j + 1] + deletionWeight
                val insertionCost = costMatrix[i + 1][j] + insertionWeight

                costMatrix[i + 1][j + 1] = if (c1 == c2)
                    costMatrix[i][j]
                else
                    min(deletionCost, insertionCost)
            }
        }
    }

    /**
     * Default LCS instance
     */
    public companion object : MetricStringDistance, StringDistance, StringSimilarity {
        private val defaultMeasure = LCS()
        override fun distance(s1: String, s2: String): Double = defaultMeasure.distance(s1, s2)
        override fun similarity(s1: String, s2: String): Double = defaultMeasure.similarity(s1, s2)
    }
}
