/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file NormalizedLevenshtein.kt is part of kt-fuzzy
 * Last modified on 22-10-2021 08:10 p.m.
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

/**
 * This distance is computed as levenshtein distance divided by the length of
 * the longest string. The resulting value is always in the interval [0.0 1.0]
 * but it is not a metric anymore! The similarity is computed as 1 - normalized
 * distance.
 *
 * @author Thibault Debatty
 */
public class NormalizedLevenshtein : NormalizedStringDistance,
                                     NormalizedStringSimilarity {
    private val levenshtein: Levenshtein = Levenshtein()
    
    /**
     * Compute distance as Levenshtein(s1, s2) / max(|s1|, |s2|).
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed distance in the range [0, 1]
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2) {
            return 0.0
        }
        
        val maxLen: Int = max(s1.length, s2.length)
        
        return if (maxLen == 0) {
            0.0
        } else levenshtein.distance(s1, s2) / maxLen
    }
    
    /**
     * Return 1 - distance.
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1.0 - the computed distance
     */
    override fun similarity(s1: String, s2: String): Double {
        return 1.0 - distance(s1, s2)
    }
}