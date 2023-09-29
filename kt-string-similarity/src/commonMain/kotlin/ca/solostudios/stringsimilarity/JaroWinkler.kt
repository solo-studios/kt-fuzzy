/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file JaroWinkler.kt is part of kotlin-fuzzy
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

package ca.solostudios.stringsimilarity

import ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity
import ca.solostudios.stringsimilarity.util.minMaxByLength
import kotlin.math.max
import kotlin.math.min

/**
 * Implements the Jaro-Winkler distance (Winkler, 1990) between strings.
 *
 * The Jaro–Winkler distance is designed and best suited for short
 * strings such as person names, and to detect typos; it is (roughly) a
 * variation of Damerau-Levenshtein, where the substitution of 2 close
 * characters is considered less important then the substitution of 2 characters
 * that a far from each other.
 *
 * Jaro-Winkler was developed in the area of record linkage (duplicate
 * detection) (Winkler, 1990). It returns a value in the range \(&#91;0, 1]\).
 *
 * The distance is computed as
 * \(1 - similarity(X, Y)\).
 *
 * #### References
 * Winkler, W. E. (1990). String comparator metrics and enhanced decision rules
 * in the fellegi-sunter model of record linkage. *Proceedings of the Survey
 * Research Methods Section*, 354-359. <https://eric.ed.gov/?id=ED325505>
 *
 * @param threshold The threshold value used for adding the Winkler bonus.
 *
 * @see NormalizedStringDistance
 * @see NormalizedStringSimilarity
 *
 * @author Thibault Debatty, solonovamax
 */
public class JaroWinkler(
    /**
     * The threshold value used for adding the Winkler bonus.
     */
    public val threshold: Double = DEFAULT_THRESHOLD,
) : NormalizedStringDistance, NormalizedStringSimilarity {
    /**
     * Computes the Jaro-Winkler similarity of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Jaro-Winkler similarity.
     * @see NormalizedStringSimilarity
     */
    override fun similarity(s1: String, s2: String): Double {
        if (s1 == s2)
            return 1.0
        if (s1.isEmpty() || s2.isEmpty())
            return 0.0

        val mtp = matches(s1, s2)
        val m = mtp.matches.toDouble()

        if (mtp.matches == 0)
            return 0.0

        val jaroSimilarity = ((m / s1.length) + (m / s2.length) + (m - mtp.transpositions) / m) / 3

        return if (jaroSimilarity > threshold)
            jaroSimilarity + min(
                JW_COEFFICIENT,
                1.0 / mtp.longestLength
            ) * mtp.commonPrefixLength * (1 - jaroSimilarity)
        else
            jaroSimilarity
    }

    /**
     * Computes the Jaro-Winkler distance of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized Jaro-Winkler distance.
     * @see NormalizedStringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        return 1.0 - similarity(s1, s2)
    }

    private fun matches(s1: String, s2: String): Matches {
        val (shortest, longest) = minMaxByLength(s1, s2)
        val searchRange = max(longest.length / 2 - 1, 0)
        val matchIndexes = IntArray(shortest.length) { -1 }

        val matchFlags = BooleanArray(longest.length)

        val matches = shortest.mapIndexedNotNull { index, char ->
            val low = max(index - searchRange, 0)
            val high = min(index + searchRange + 1, longest.length)
            val matchIndex = (low until high).firstOrNull { i ->
                !matchFlags[i] && char == longest[i]
            }
            if (matchIndex != null) {
                matchIndexes[index] = matchIndex
                matchFlags[matchIndex] = true
                char
            } else {
                null
            }
        }.size

        val ms1 = shortest.filterIndexed { i, _ -> matchIndexes[i] != -1 }.toCharArray()
        val ms2 = longest.filterIndexed { i, _ -> matchFlags[i] }.toCharArray()

        // val transpositions = ms1.zip(ms2).count { (c1, c2) -> c1 != c2 } / 2
        var transpositions = 0
        ms1.forEachIndexed { i, c1 ->
            if (c1 != ms2[i])
                transpositions++
        }

        val commonPrefixLength = shortest.commonPrefixWith(longest).length

        return Matches(matches, transpositions / 2, commonPrefixLength, longest.length)
    }

    private data class Matches(
        val matches: Int,
        val transpositions: Int,
        val commonPrefixLength: Int,
        val longestLength: Int,
    )

    /**
     * Default Jaro-Winkler instance
     */
    public companion object : NormalizedStringDistance, NormalizedStringSimilarity {
        private val defaultMeasure = JaroWinkler()

        private const val DEFAULT_THRESHOLD = 0.7
        private const val JW_COEFFICIENT = 0.1

        override fun distance(s1: String, s2: String): Double = defaultMeasure.distance(s1, s2)
        override fun similarity(s1: String, s2: String): Double = defaultMeasure.similarity(s1, s2)
    }
}
