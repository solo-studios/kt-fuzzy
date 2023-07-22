/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file QGram.kt is part of kotlin-fuzzy
 * Last modified on 22-07-2023 04:43 p.m.
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
import kotlin.math.abs

/**
 * Q-gram distance, as defined by Ukkonen in
 * ["Approximate string-matching with q-grams and maximal matches"](http://www.sciencedirect.com/science/article/pii/0304397592901434).
 * The distance between two strings is defined as
 * the L1 norm of the difference of their profiles (the number of occurences of
 * each n-gram): SUM( |V1_i - V2_i| ). Q-gram distance is a lower bound on
 * Levenshtein distance, but can be computed in O(m + n), where Levenshtein
 * requires O(m.n).
 * @param k The length of k-shingles.
 * @throws IllegalArgumentException if k is &lt;= 0
 * @author Thibault Debatty, solonovamax
 */
public class QGram(k: Int = DEFAULT_K) : ShingleBased(k),
                                         StringDistance {
    /**
     * The distance between two strings is defined as the L1 norm of the
     * difference of their profiles (the number of occurence of each k-shingle).
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed Q-gram distance.
     * @throws NullPointerException if s1 or s2 is null.
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2) {
            return 0.0
        }
        val profile1 = profile(s1)
        val profile2 = profile(s2)
        return distance(profile1, profile2)
    }

    /**
     * Compute QGram distance using precomputed profiles.
     *
     * @param profile1
     * @param profile2
     * @return
     */
    public fun distance(
        profile1: Map<String, Int>,
        profile2: Map<String, Int>,
    ): Double {
        val union = mutableSetOf<String>()
        union.addAll(profile1.keys)
        union.addAll(profile2.keys)

        var agg = 0
        for (key in union) {
            val v1 = profile1[key] ?: 0
            val v2 = profile2[key] ?: 0
            agg += abs(v1 - v2)
        }
        return agg.toDouble()
    }
}
