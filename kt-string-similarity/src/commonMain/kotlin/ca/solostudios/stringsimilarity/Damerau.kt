/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015 Thibault Debatty
 *
 * The file Damerau.kt is part of kt-fuzzy
 * Last modified on 22-10-2021 05:49 p.m.
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

import ca.solostudios.stringsimilarity.interfaces.MetricStringDistance
import kotlin.math.min

/**
 * Implementation of Damerau-Levenshtein distance with transposition (also
 * sometimes calls unrestricted Damerau-Levenshtein distance).
 * It is the minimum number of operations needed to transform one string into
 * the other, where an operation is defined as an insertion, deletion, or
 * substitution of a single character, or a transposition of two adjacent
 * characters.
 * It does respect triangle inequality, and is thus a metric distance.
 *
 * This is not to be confused with the optimal string alignment distance, which
 * is an extension where no substring can be edited more than once.
 *
 * @author Thibault Debatty
 */
public class Damerau : MetricStringDistance {
    /**
     * Compute the distance between strings: the minimum number of operations
     * needed to transform one string into the other (insertion, deletion,
     * substitution of a single character, or a transposition of two adjacent
     * characters).
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed distance.
     * @throws NullPointerException if s1 or s2 is null.
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2) {
            return 0.0
        }
        
        // INFinite distance is the max possible distance
        val inf = s1.length + s2.length
        
        // Create and initialize the character array indices
        val da = mutableMapOf<Char, Int>()
        
        for (element in s1) {
            da[element] = 0
        }
        for (element in s2) {
            da[element] = 0
        }
        
        // Create the distance matrix H[0 .. s1.length+1][0 .. s2.length+1]
        val h = Array(s1.length + 2) { IntArray(s2.length + 2) }
        
        // initialize the left and top edges of H
        for (i in 0 .. s1.length) {
            h[i + 1][0] = inf
            h[i + 1][1] = i
        }
        for (j in 0 .. s2.length) {
            h[0][j + 1] = inf
            h[1][j + 1] = j
        }
        
        // fill in the distance matrix H
        // look at each character in s1
        for (i in 1 .. s1.length) {
            var db = 0
            
            // look at each character in b
            for (j in 1 .. s2.length) {
                val i1 = da[s2[j - 1]]!!
                val j1 = db
                var cost = 1
                if (s1[i - 1] == s2[j - 1]) {
                    cost = 0
                    db = j
                }
                h[i + 1][j + 1] = min(
                        h[i][j] + cost,  // substitution
                        h[i + 1][j] + 1,  // insertion
                        h[i][j + 1] + 1,  // deletion
                        h[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1))
            }
            da[s1[i - 1]] = i
        }
        return h[s1.length + 1][s2.length + 1].toDouble()
    }
    
    private companion object {
        private inline fun min(a: Int, b: Int, c: Int, d: Int): Int = min(a, min(b, min(c, d)))
    }
}