/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2021-2021 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file FuzzyKt.kt is part of kt-fuzzy
 * Last modified on 27-10-2021 08:03 p.m.
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

package ca.solostudios.fuzzykt

import ca.solostudios.stringsimilarity.NormalizedLevenshtein
import kotlin.math.min

public object FuzzyKt {
    private val normalizedLevenshtein = NormalizedLevenshtein()

    /**
     * The similarity ratio between the two strings.
     *
     * @param s1
     * @param s2
     * @return
     */
    public fun ratio(s1: String, s2: String): Double {
        return normalizedLevenshtein.similarity(s1, s2)
    }

    /**
     * The ratio between the smallest of the two strings and the most similar substring.
     * (Longest common substring, adjusted to the same length as the shortest string.)
     *
     * @param s1
     * @param s2
     * @return
     */
    public fun partialRatio(s1: String, s2: String): Double {
        val (shorter, longer) = if (s1.length > s2.length) s2 to s1 else s1 to s2

        val (start, _) = longestCommonSubstring(s1, s2)

        val upperBound = min(start + shorter.length, longer.length)
        val lowerBound = upperBound - shorter.length

        val splitString = longer.substring(lowerBound, upperBound)

        return normalizedLevenshtein.similarity(splitString, shorter)
    }

    /**
     * Returns the start and end index of the longest common substring,
     * in reference to the first string passed.
     *
     * @param s1
     * @param s2
     * @return
     */
    public fun longestCommonSubstring(s1: String, s2: String): Pair<Int, Int> { // black magic I took from google
        var max = 0
        val dp = Array(s1.length) { IntArray(s2.length) }
        var endIndex = -1
        for (i in s1.indices) {
            for (j in s2.indices) {
                if (s1[i] == s2[j]) {

                    // If first row or column
                    if (i == 0 || j == 0) {
                        dp[i][j] = 1
                    } else {
                        // Add 1 to the diagonal value
                        dp[i][j] = dp[i - 1][j - 1] + 1
                    }
                    if (max < dp[i][j]) {
                        max = dp[i][j]
                        endIndex = i
                    }
                }
            }
        }
        // We want String upto endIndex, we are using endIndex+1 in substring.
        return (endIndex - max + 1) to (endIndex + 1)
    }
}
