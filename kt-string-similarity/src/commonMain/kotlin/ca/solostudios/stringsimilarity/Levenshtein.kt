/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Levenshtein.kt is part of kotlin-fuzzy
 * Last modified on 19-10-2023 05:33 p.m.
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
import ca.solostudios.stringsimilarity.util.Constants
import ca.solostudios.stringsimilarity.util.min
import ca.solostudios.stringsimilarity.util.minMaxByLength

/**
 * Implements the Levenshtein distance (Levenshtein, 1966), or edit distance, between two words is the
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
 * #### References
 * Levenshtein, V. I. (1966-02). Binary codes capable of correcting deletions,
 * insertions and reversals. *Soviet Physics Doklady*, *10*, 707-710.
 *
 * @param insertionWeight The weight of an insertion. Represented as \(w_i\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param deletionWeight The weight of a deletion. Represented as \(w_d\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param substitutionWeight The weight of a substitution. Represented as \(w_s\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 *
 * @see MetricStringDistance
 * @see StringDistance
 * @see StringSimilarity
 *
 * @author solonovamax
 */
public class Levenshtein(
    /**
     * The weight of an insertion. Represented as \(w_i\).
     *
     * Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
     */
    public val insertionWeight: Double = Constants.DEFAULT_WEIGHT,
    /**
     * The weight of a deletion. Represented as \(w_d\).
     *
     * Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
     */
    public val deletionWeight: Double = Constants.DEFAULT_WEIGHT,
    /**
     * The weight of a substitution. Represented as \(w_s\).
     *
     * Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
     */
    public val substitutionWeight: Double = Constants.DEFAULT_WEIGHT,
) : MetricStringDistance, StringSimilarity, StringDistance {
    init {
        require(insertionWeight > Constants.MIN_REASONABLE_WEIGHT && insertionWeight < Constants.MAX_REASONABLE_WEIGHT) {
            "Insertion weight ($insertionWeight) should be between 0 and 1*10^10"
        }
        require(deletionWeight > Constants.MIN_REASONABLE_WEIGHT && deletionWeight < Constants.MAX_REASONABLE_WEIGHT) {
            "Deletion weight ($deletionWeight) should be between 0 and 1*10^10"
        }
        require(substitutionWeight > Constants.MIN_REASONABLE_WEIGHT && substitutionWeight < Constants.MAX_REASONABLE_WEIGHT) {
            "Substitution weight ($substitutionWeight) should be between 0 and 1*10^10"
        }
    }

    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty())
            return s2.length * insertionWeight
        if (s2.isEmpty())
            return s1.length * deletionWeight

        val (shorter, longer) = minMaxByLength(s1, s2)

        var previousRow = DoubleArray(shorter.length + 1)
        var currentRow = DoubleArray(shorter.length + 1)

        for (i in 0..shorter.length)
            previousRow[i] = i.toDouble()

        longer.forEachIndexed { i, c1 ->
            // Initialize first element of current row
            currentRow[0] = (i + 1).toDouble()

            shorter.forEachIndexed { j, c2 ->
                val deletionCost = previousRow[j + 1] + deletionWeight
                val insertionCost = currentRow[j] + insertionWeight
                val substitutionCost = previousRow[j] + if (c1 == c2) 0.0 else substitutionWeight

                currentRow[j + 1] = min(deletionCost, insertionCost, substitutionCost)
            }
            // Swap current & previous rows
            previousRow = currentRow.also { currentRow = previousRow }
        }

        // We return the last value from the "previous row" because after the last swap,
        // the current results are in "previousRow"
        return previousRow.last()
    }

    override fun similarity(s1: String, s2: String): Double {
        return (insertionWeight * s1.length + deletionWeight * s2.length - distance(s1, s2)) / 2
    }
}
