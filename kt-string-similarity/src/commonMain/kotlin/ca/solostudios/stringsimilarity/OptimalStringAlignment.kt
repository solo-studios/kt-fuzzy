/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file OptimalStringAlignment.kt is part of kotlin-fuzzy
 * Last modified on 18-07-2023 09:46 p.m.
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
 * Implementation of the the Optimal String Alignment (sometimes called the
 * restricted edit distance) variant of the Damerau-Levenshtein distance.
 *
 * The difference between the two algorithms consists in that the Optimal String
 * Alignment algorithm computes the number of edit operations needed to make the
 * strings equal under the condition that no substring is edited more than once,
 * whereas Damerau-Levenshtein presents no such restriction.
 *
 * @author Michail Bogdanos, solonovamax
 */
public class OptimalStringAlignment : StringDistance {
    /**
     * Compute the distance between strings: the minimum number of operations
     * needed to transform one string into the other (insertion, deletion,
     * substitution of a single character, or a transposition of two adjacent
     * characters) while no substring is edited more than once.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return the OSA distance
     */
    override fun distance(s1: String, s2: String): Double {

        if (s1 == s2) {
            return 0.0
        }
        val n = s1.length
        val m = s2.length
        if (n == 0) {
            return m.toDouble()
        }
        if (m == 0) {
            return n.toDouble()
        }

        // Create the distance matrix H[0 .. s1.length+1][0 .. s2.length+1]
        val d = Array(n + 2) { IntArray(m + 2) }

        // initialize top row and leftmost column
        for (i in 0..n) {
            d[i][0] = i
        }
        for (j in 0..m) {
            d[0][j] = j
        }

        // fill the distance matrix
        for (i in 1..n) {
            for (j in 1..m) {

                // if s1[i - 1] = s2[j - 1] then cost = 0, else cost = 1
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1

                d[i][j] = minOf(
                    d[i - 1][j - 1] + cost, // substitution
                    d[i][j - 1] + 1,        // insertion
                    d[i - 1][j] + 1         // deletion
                )

                // transposition check
                if (i > 1 && j > 1 && s1[i - 1] == s2[j - 2] && s1[i - 2] == s2[j - 1]) {
                    d[i][j] = min(d[i][j], d[i - 2][j - 2] + cost)
                }
            }
        }
        return d[n][m].toDouble()
    }
}
