/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file OptimalStringAlignment.kt is part of kotlin-fuzzy
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
 * Implementation of the the Optimal String Alignment (sometimes called the
 * restricted edit distance) variant of the Damerau-Levenshtein distance.
 *
 * The difference between the two algorithms consists in that the Optimal String
 * Alignment algorithm computes the number of edit operations needed to make the
 * strings equal under the condition that no substring is edited more than once,
 * whereas Damerau-Levenshtein presents no such restriction.
 *
 * Computes the distance between strings: the minimum number of operations
 * needed to transform one string into the other (insertion, deletion,
 * substitution of a single character, or a transposition of two adjacent
 * characters) while no substring is edited more than once.
 *
 * The similarity is computed as
 * \(\frac{w_d \lvert X \rvert + w_i \lvert Y \rvert - distance(X, Y)}{2}\).
 *
 * @see StringEditMeasure
 * @see MetricStringDistance
 * @see StringDistance
 * @see StringSimilarity
 *
 * @author Thibault Debatty, solonovamax
 */
public class OptimalStringAlignment(
    insertionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    deletionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    substitutionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    transpositionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
) : AbstractStringEditMeasure(insertionWeight, deletionWeight, substitutionWeight, transpositionWeight) {
    override fun fillCostMatrix(costMatrix: Array<DoubleArray>, shorter: String, longer: String) {
        longer.forEachIndexed { i, c1 ->
            shorter.forEachIndexed { j, c2 ->
                val deletionCost = costMatrix[i][j + 1] + deletionWeight
                val insertionCost = costMatrix[i + 1][j] + insertionWeight
                val substitutionCost = costMatrix[i][j] + if (c1 == c2) 0.0 else substitutionWeight

                costMatrix[i + 1][j + 1] = minOf(deletionCost, insertionCost, substitutionCost)

                if (i > 0 && j > 0 && c1 == shorter[j - 1] && longer[i - 1] == c2)
                    costMatrix[i + 1][j + 1] = minOf(costMatrix[i][j], costMatrix[i - 1][j - 1] + transpositionWeight)
            }
        }
    }

    /**
     * Default Optimal String Alignment instance
     */
    public companion object : MetricStringDistance, StringDistance, StringSimilarity {
        private val defaultMeasure = OptimalStringAlignment()
        override fun distance(s1: String, s2: String): Double = defaultMeasure.distance(s1, s2)
        override fun similarity(s1: String, s2: String): Double = defaultMeasure.similarity(s1, s2)
    }
}
