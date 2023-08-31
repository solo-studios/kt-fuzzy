/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file DamerauLevenshtein.kt is part of kotlin-fuzzy
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
import ca.solostudios.stringsimilarity.util.min
import kotlin.math.min

/**
 * Implementation of Damerau-Levenshtein distance with transposition (also
 * sometimes calls unrestricted Damerau-Levenshtein distance).
 * It is the minimum number of operations needed to transform one string into
 * the other, where an operation is defined as an insertion, deletion, or
 * substitution of a single character, or a transposition of two adjacent
 * characters.
 * It does respect triangle inequality, and is thus a [metric distance][MetricStringDistance].
 *
 * This is not to be confused with the optimal string alignment distance, which
 * is an extension where no substring can be edited more than once.
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
 * @param transpositionWeight The weight of a substitution. Represented as \(w_t\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 *
 * @see StringEditMeasure
 * @see MetricStringDistance
 * @see StringDistance
 * @see StringSimilarity
 *
 * @author Thibault Debatty, solonovamax
 */
public class DamerauLevenshtein(
    insertionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    deletionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    substitutionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    transpositionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
) : AbstractStringEditMeasure(insertionWeight, deletionWeight, substitutionWeight, transpositionWeight) {
    override fun initializeCostMatrix(shorter: String, longer: String): Array<DoubleArray> {
        val maxDistance = (shorter.length + longer.length).toDouble()

        // This has a 1-wide side zone for initial values
        val costMatrix = Array(longer.length + 2) { DoubleArray(shorter.length + 2) }

        // initialize the left and top edges of costMatrix
        for (i in 0..longer.length) {
            costMatrix[i + 1][0] = maxDistance
            costMatrix[i + 1][1] = i.toDouble()
        }
        for (j in 0..shorter.length) {
            costMatrix[0][j + 1] = maxDistance
            costMatrix[1][j + 1] = j.toDouble()
        }
        return costMatrix
    }

    override fun fillCostMatrix(costMatrix: Array<DoubleArray>, shorter: String, longer: String) {
        // we could instead use an array of size Char.MAX_VALUE, but that would *probably* use more space
        val lastRowId = mutableMapOf<Char, Int>()

        longer.forEachIndexed { i, c1 ->
            var currentMin = costMatrix[i + 1][1]
            var lastColId = 0
            shorter.forEachIndexed { j, c2 ->
                val deletionCost = costMatrix[i + 1][j + 2] + deletionWeight
                val insertionCost = costMatrix[i + 2][j + 1] + insertionWeight
                val substitutionCost = costMatrix[i + 1][j + 1] + (if (c1 == c2) 0.0 else substitutionWeight)

                // What are l and k? idfk.
                val k = lastRowId[c2] ?: 0
                val l = lastColId
                val transpositionCost = costMatrix[k][l] + ((i - k) + 1 + (j - lastColId)) * transpositionWeight

                if (c1 == c2)
                    lastColId = j + 1

                costMatrix[i + 2][j + 2] = min(deletionCost, insertionCost, substitutionCost, transpositionCost)

                currentMin = min(costMatrix[i + 1][j + 1], currentMin)
            }

            lastRowId[c1] = i + 1
        }
    }

    /**
     * Default Damerau-Levenshtein instance
     */
    public companion object : MetricStringDistance, StringDistance, StringSimilarity {
        private val defaultMeasure = DamerauLevenshtein()
        override fun distance(s1: String, s2: String): Double = defaultMeasure.distance(s1, s2)
        override fun similarity(s1: String, s2: String): Double = defaultMeasure.similarity(s1, s2)
    }
}
