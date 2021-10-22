/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015 Thibault Debatty
 *
 * The file JaroWinkler.kt is part of kt-fuzzy
 * Last modified on 22-10-2021 05:54 p.m.
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
 * KT-STRING-SIMILARITY IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
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
import kotlin.math.max
import kotlin.math.min

/**
 * The Jaro–Winkler distance metric is designed and best suited for short
 * strings such as person names, and to detect typos; it is (roughly) a
 * variation of Damerau-Levenshtein, where the substitution of 2 close
 * characters is considered less important then the substitution of 2 characters
 * that a far from each other.
 * Jaro-Winkler was developed in the area of record linkage (duplicate
 * detection) (Winkler, 1990). It returns a value in the interval [0.0, 1.0].
 * The distance is computed as 1 - Jaro-Winkler similarity.
 * @param threshold Returns the current value of the threshold used for adding the Winkler
 * bonus. The default value is 0.7.
 * @author Thibault Debatty
 */
public class JaroWinkler(public val threshold: Double = DEFAULT_THRESHOLD) : NormalizedStringSimilarity,
                                                                             NormalizedStringDistance {
    
    /**
     * Compute Jaro-Winkler similarity.
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The Jaro-Winkler similarity in the range [0, 1]
     * @throws NullPointerException if s1 or s2 is null.
     */
    override fun similarity(s1: String, s2: String): Double {
        if (s1 == s2) {
            return 1.0
        }
        val mtp = matches(s1, s2)
        val m = mtp[0].toFloat()
        if (m == 0f) {
            return 0.0
        }
        val j = ((m / s1.length + m / s2.length + (m - mtp[1]) / m)
                / THREE).toDouble()
        var jw = j
        if (j > threshold) {
            jw = j + min(JW_COEFFICIENT, 1.0 / mtp[THREE]) * mtp[2] * (1 - j)
        }
        return jw
    }
    
    /**
     * Return 1 - similarity.
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1 - similarity.
     * @throws NullPointerException if s1 or s2 is null.
     */
    override fun distance(s1: String, s2: String): Double {
        return 1.0 - similarity(s1, s2)
    }
    
    private fun matches(s1: String, s2: String): IntArray {
        val max: String
        val min: String
        if (s1.length > s2.length) {
            max = s1
            min = s2
        } else {
            max = s2
            min = s1
        }
        val range: Int = max(max.length / 2 - 1, 0)
        val matchIndexes = IntArray(min.length)
        matchIndexes.fill(-1)
        
        val matchFlags = BooleanArray(max.length)
        
        var matches = 0
        for (mi in min.indices) {
            val c1 = min[mi]
            var xi: Int = max(mi - range, 0)
            val xn: Int = min(mi + range + 1, max.length)
            while (xi < xn) {
                if (!matchFlags[xi] && c1 == max[xi]) {
                    matchIndexes[mi] = xi
                    matchFlags[xi] = true
                    matches++
                    break
                }
                xi++
            }
        }
        val ms1 = CharArray(matches)
        val ms2 = CharArray(matches)
        
        var i2 = 0
        var si2 = 0
        while (i2 < min.length) {
            if (matchIndexes[i2] != -1) {
                ms1[si2] = min[i2]
                si2++
            }
            i2++
        }
        
        var i = 0
        var si = 0
        while (i < max.length) {
            if (matchFlags[i]) {
                ms2[si] = max[i]
                si++
            }
            i++
        }
        var transpositions = 0
        for (mi in ms1.indices) {
            if (ms1[mi] != ms2[mi]) {
                transpositions++
            }
        }
        var prefix = 0
        for (mi in 0 until min.length) {
            if (s1[mi] == s2[mi]) {
                prefix++
            } else {
                break
            }
        }
        return intArrayOf(matches, transpositions / 2, prefix, max.length)
    }
    
    public companion object {
        private const val DEFAULT_THRESHOLD = 0.7
        private const val THREE = 3
        private const val JW_COEFFICIENT = 0.1
    }
}