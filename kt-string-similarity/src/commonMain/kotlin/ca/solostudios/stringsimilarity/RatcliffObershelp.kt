/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file RatcliffObershelp.kt is part of kotlin-fuzzy
 * Last modified on 01-09-2023 11:32 p.m.
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

/**
 * Ratcliff/Obershelp pattern recognition
 * The Ratcliff/Obershelp algorithm computes the similarity of two strings a
 * the doubled number of matching characters divided by the total number of
 * characters in the two strings. Matching characters are those in the longest
 * common subsequence plus, recursively, matching characters in the unmatched
 * region on either side of the longest common subsequence.
 * The Ratcliff/Obershelp distance is computed as 1 - Ratcliff/Obershelp
 * similarity.
 *
 * @author [Ligi](https://github.com/dxpux), solonovamax, Ported to java from .net by denmase
 */
public class RatcliffObershelp : NormalizedStringSimilarity, NormalizedStringDistance {
    /**
     * Compute the Ratcliff-Obershelp similarity between strings.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The Ratcliff-Obershelp similarity in the range [0, 1]
     * @throws NullPointerException if s1 or s2 is null.
     */
    override fun similarity(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty() || s2.isEmpty())
            return 1.0

        val matches = getMatchList(s1, s2)
        return 2.0 * matches / (s1.length + s2.length)
    }

    /**
     * Return 1 - similarity.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1 - similarity
     * @throws NullPointerException if s1 or s2 is null.
     */
    override fun distance(s1: String, s2: String): Double {
        return 1.0 - similarity(s1, s2)
    }

    /**
     * Default Ratcliff-Obershelp instance
     */
    public companion object : NormalizedStringDistance, NormalizedStringSimilarity {
        private val defaultMeasure = RatcliffObershelp()

        override fun distance(s1: String, s2: String): Double = defaultMeasure.distance(s1, s2)
        override fun similarity(s1: String, s2: String): Double = defaultMeasure.similarity(s1, s2)

        private fun getMatchList(s1: String, s2: String): Int {
            val match = frontMaxMatch(s1, s2)
            return when {
                match.isEmpty() -> 0
                else -> {
                    val frontSource = s1.substring(0, s1.indexOf(match))
                    val frontTarget = s2.substring(0, s2.indexOf(match))
                    val endSource = s1.substring(s1.indexOf(match) + match.length)
                    val endTarget = s2.substring(s2.indexOf(match) + match.length)

                    return getMatchList(frontSource, frontTarget) + match.length + getMatchList(endSource, endTarget)
                }
            }
        }

        private fun frontMaxMatch(s1: String, s2: String): String {
            var longestLength = 0
            var longest = ""
            for (i in s1.indices) {
                for (j in i + 1..s1.length) {
                    val substring = s1.substring(i, j)
                    if (substring.length > longestLength && s2.contains(substring)) {
                        longestLength = substring.length
                        longest = substring
                    }
                }
            }
            return longest
        }
    }
}
