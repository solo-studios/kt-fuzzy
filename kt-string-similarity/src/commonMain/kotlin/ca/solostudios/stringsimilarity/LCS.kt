/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LCS.kt is part of kotlin-fuzzy
 * Last modified on 18-07-2023 09:48 p.m.
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
import ca.solostudios.stringsimilarity.util.maxLength
import kotlin.math.max

/**
 * The longest common subsequence (LCS) problem consists in finding the longest
 * subsequence common to two (or more) sequences. It differs from problems of
 * finding common substrings: unlike substrings, subsequences are not required
 * to occupy consecutive positions within the original sequences.
 *
 * It is used by the diff utility, by Git for reconciling multiple changes, etc.
 *
 * The LCS distance between Strings \(X\) and \(Y\) is:
 * \(\lvert X \rvert + \lvert Y \rvert - 2 \times \lvert LCS(X, Y) \rvert\)
 * - \(\text{min} = 0\)
 * - \(\text{max} = \lvert X \rvert + \lvert Y \rvert\).
 *
 * LCS distance is equivalent to Levenshtein distance, when only insertion and
 * deletion is allowed (no substitution), or when the cost of the substitution
 * is the double of the cost of an insertion or deletion.
 *
 * ! This class currently implements the dynamic programming approach, which has
 * a space requirement \(O(m \times n)\) !
 *
 * @author Thibault Debatty, solonovamax
 * @see StringDistance
 */
public class LCS : StringDistance {
    /**
     * Computes the Longest Common Subsequence distance of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The Longest Common Subsequence distance.
     * @see StringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty() || s2.isEmpty())
            return maxLength(s1, s2).toDouble() // return the length of the non-empty one

        return (s1.length + s2.length - 2 * lcsLength(s1, s2).toDouble())
    }

    /**
     * Return the length of the Longest Common Subsequence (LCS) between strings [s1] and [s2].
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The length of \(LCS(s1, s2)\).
     */
    public fun lcsLength(s1: String, s2: String): Int {
        /* https://en.wikipedia.org/wiki/Longest_common_subsequence
        function LCSLength(X[1..m], Y[1..n])
            C = array(0..m, 0..n)

            for i := 0..m
                C[i,0] = 0

            for j := 0..n
                C[0,j] = 0

            for i := 1..m
                for j := 1..n
                    if X[i] = Y[j]
                        C[i,j] := C[i-1,j-1] + 1
                    else
                        C[i,j] := max(C[i,j-1], C[i-1,j])
            return C[m,n]
         */

        val s1Length = s1.length
        val s2Length = s2.length

        val c = Array(s1Length + 1) { IntArray(s2Length + 1) }
        for (i in 1..s1Length) {
            for (j in 1..s2Length) {
                c[i][j] = if (s1[i - 1] == s2[j - 1])
                    c[i - 1][j - 1] + 1
                else
                    max(c[i][j - 1], c[i - 1][j])
            }
        }
        return c[s1Length][s2Length]
    }
}
