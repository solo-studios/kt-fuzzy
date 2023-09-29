/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NGram.kt is part of kotlin-fuzzy
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

package ca.solostudios.stringsimilarity

import ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity
import ca.solostudios.stringsimilarity.util.countIndexed
import ca.solostudios.stringsimilarity.util.min
import ca.solostudios.stringsimilarity.util.minMaxByLength

/**
 * Implements the N-Gram Similarity (Kondrak, 2005) between strings.
 *
 * The algorithm uses affixing with special character `'\0'` to increase the
 * weight of first characters. The normalization is achieved by dividing the
 * total similarity score the original length of the longest word.
 *
 * The similarity is computed as
 * \(1 - distance(X, Y)\).
 *
 * #### References
 * Kondrak, G. (2005-11-02). N-gram similarity and distance. In String processing
 * and information retrieval, lecture notes in computer science (Pages 115-126).
 * Springer Berlin Heidelberg.
 * <https://doi.org/10.1007/11575832_13><sup>[&#91;sci-hub&#93;](https://sci-hub.st/10.1007/11575832_13)</sup>
 *
 * @see NormalizedStringDistance
 * @see NormalizedStringSimilarity
 *
 * @author Thibault Debatty, solonovamax
 */
public class NGram(public val n: Int = DEFAULT_N) : NormalizedStringDistance, NormalizedStringSimilarity {
    /**
     * Computes the N-Gram distance of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The N-Gram distance.
     * @see NormalizedStringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty() || s2.isEmpty())
            return 1.0

        val special = 0.toChar()

        val (shorter, longer) = minMaxByLength(s1, s2)

        when {
            shorter.length < n || longer.length < n -> {
                val cost = shorter.countIndexed { i, c1 ->
                    c1 == longer[i]
                }
                return cost.toDouble() / longer.length
            }

            else -> {
                // 'previous' cost array, horizontally
                var previousCost = DoubleArray(shorter.length + 1) { it.toDouble() }
                var currentCost = DoubleArray(shorter.length + 1) // cost array, horizontally

                val longerCharArray = longer.toCharArray()
                val slice = CharArray(shorter.length + n - 1) { i ->
                    if (i < n - 1) special else shorter[i - n + 1]
                }

                for (j in longer.indices) {
                    // construct t_j n-gram
                    val tJ = CharArray(n + 1) // jth n-gram of t
                    if (j + 1 < n) {
                        // add prefix
                        repeat(n - (j + 1)) { ti ->
                            tJ[ti] = special
                        }
                        for (ti in n - (j + 1) until n)
                            tJ[ti] = longer[ti - (n - (j + 1))]
                    } else {
                        longerCharArray.copyInto(tJ, 0, j + 1 - n, j + 1)
                    }

                    currentCost[0] = (j + 1).toDouble()

                    for (i in shorter.indices) {
                        var cost = 0
                        var tn = n
                        // compare slice to t_j
                        repeat(n) { ni ->
                            if (slice[i + ni] != tJ[ni])
                                cost++
                            else if (slice[i + ni] == special)
                                tn-- // discount matches on prefix
                        }

                        // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                        currentCost[i + 1] = min(
                            currentCost[i] + 1, // left + 1
                            previousCost[i + 1] + 1, // top + 1
                            previousCost[i] + cost.toDouble() / tn, // diagonally left and up + cost
                        )
                    }

                    // copy current distance counts to 'previous row' distance counts
                    previousCost = currentCost.also { currentCost = previousCost }
                }

                // our last action in the above loop was to switch d and p, so p now
                // actually has the most recent cost counts
                return previousCost[shorter.length] / longer.length
            }
        }
    }

    /**
     * Computes the N-Gram similarity of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized N-Gram similarity.
     * @see NormalizedStringSimilarity
     */
    override fun similarity(s1: String, s2: String): Double {
        return 1.0 - distance(s1, s2)
    }

    private companion object {
        private const val DEFAULT_N = 2
    }
}
