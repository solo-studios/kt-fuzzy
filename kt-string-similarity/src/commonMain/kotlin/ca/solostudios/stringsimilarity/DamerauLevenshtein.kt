/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file DamerauLevenshtein.kt is part of kotlin-fuzzy
 * Last modified on 01-08-2023 01:41 a.m.
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
import ca.solostudios.stringsimilarity.util.min
import ca.solostudios.stringsimilarity.util.minMaxByLength

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
public class DamerauLevenshtein(
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
    /**
     * The weight of a transposition. Represented as \(w_t\).
     */
    public val transpositionWeight: Double = 1.0,
) : MetricStringDistance, StringDistance, StringSimilarity {
    init {
        // 1E10 is a reasonable upper limit
        require(insertionWeight > 0 && insertionWeight < 1E10)
        require(deletionWeight > 0 && deletionWeight < 1E10)
        require(deletionWeight > 0 && deletionWeight < 1E10)
    }

    /**
     * Computes the Damerau-Levenshtein distance metric of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The Damerau-Levenshtein distance.
     * @see MetricStringDistance
     * @see StringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty() || s2.isEmpty())
            return maxLength(s1, s2).toDouble() // return the length of the non-empty one

        val (shorter, longer) = minMaxByLength(s1, s2)

        val maxDistance = (shorter.length + longer.length).toDouble()

        // This has a 1-wide side zone for initial values
        val costMatrix = Array(longer.length + 2) { DoubleArray(shorter.length + 2) }

        // we could instead use an array of size Char.MAX_VALUE, but that would *probably* use more space
        val lastRowId = mutableMapOf<Char, Int>()

        // initialize the left and top edges of H
        for (i in 0..longer.length) {
            costMatrix[i + 1][0] = maxDistance
            costMatrix[i + 1][1] = i.toDouble()
        }
        for (j in 0..shorter.length) {
            costMatrix[0][j + 1] = maxDistance
            costMatrix[1][j + 1] = j.toDouble()
        }

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

                currentMin = minOf(costMatrix[i + 1][j + 1], currentMin)
            }

            if (currentMin >= limit)
                return limit

            lastRowId[c1] = i + 1
        }

        return costMatrix[longer.length + 1][shorter.length + 1]
    }

    /**
     * Computes the Longest Common Subsequence similarity of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The Longest Common Subsequence similarity.
     * @see StringSimilarity
     */
    override fun similarity(s1: String, s2: String): Double {
        return ((insertionWeight * s1.length + deletionWeight * s2.length) - distance(s1, s2)) / 2
    }
}
