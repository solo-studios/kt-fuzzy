/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file RatcliffObershelp.kt is part of kotlin-fuzzy
 * Last modified on 02-09-2023 12:13 a.m.
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
 * Implements Ratcliff/Obershelp pattern recognition (Ratcliff & Metzener, 1988), also known as Gestalt pattern matching,
 * similarity between strings.
 *
 * The similarity is defined as
 * \(D_{ro} = \frac{2K_m}{\lVert X \rVert + \lVert Y \rVert}\).
 * Where \(K_m\) us the number of matching characters.
 *
 * The distance is computed as
 * \(1 - similarity(X, Y)\).
 *
 * #### References
 * Ratcliff, J., & Metzener, D. E. (1988-07-01). Pattern matching: The gestalt
 * approach. *Dr. Dobb’s Journal*, *13*(7), 46.
 * https://www.drdobbs.com/database/pattern-matching-the-gestalt-approach/184407970?pgno=5
 *
 * @author [Ligi](https://github.com/dxpux), solonovamax, Ported to java from .net by denmase
 */
public object RatcliffObershelp : NormalizedStringSimilarity, NormalizedStringDistance {
    /**
     * Compute the Ratcliff-Obershelp similarity between strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Ratcliff-Obershelp similarity.
     * @see NormalizedStringSimilarity
     */
    override fun similarity(s1: String, s2: String): Double {
        if (s1 == s2)
            return 1.0
        if (s1.isEmpty() || s2.isEmpty())
            return 0.0

        val matches = getMatchCount(s1, s2)
        return (2.0 * matches) / (s1.length + s2.length)
    }

    /**
     * Computes the Ratcliff-Obershelp distance of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Ratcliff-Obershelp distance.
     * @see NormalizedStringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        return 1.0 - similarity(s1, s2)
    }

    private fun getMatchCount(s1: String, s2: String): Int {
        val anchor = findAnchor(s1, s2)
        return when {
            anchor.isEmpty() -> 0
            else -> {
                val s1MatchIndex = s1.indexOf(anchor)
                val s2MatchIndex = s2.indexOf(anchor)
                val frontS1 = s1.take(s1MatchIndex)
                val frontS2 = s2.take(s2MatchIndex)
                val endS1 = s1.substring(s1MatchIndex + anchor.length)
                val endS2 = s2.substring(s2MatchIndex + anchor.length)

                val frontCount = getMatchCount(frontS1, frontS2)
                val endCount = getMatchCount(endS1, endS2)
                return frontCount + anchor.length + endCount
            }
        }
    }

    private fun findAnchor(s1: String, s2: String): String {
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
