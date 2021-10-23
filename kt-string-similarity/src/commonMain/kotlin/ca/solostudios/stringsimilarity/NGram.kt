/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file NGram.kt is part of kt-fuzzy
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
import kotlin.math.max
import kotlin.math.min

/**
 * N-Gram Similarity as defined by Kondrak, "N-Gram Similarity and Distance",
 * String Processing and Information Retrieval, Lecture Notes in Computer
 * Science Volume 3772, 2005, pp 115-126.
 *
 * The algorithm uses affixing with special character '\n' to increase the
 * weight of first characters. The normalization is achieved by dividing the
 * total similarity score the original length of the longest word.
 *
 * [http:// webdocs.cs.ualberta.ca/~kondrak/papers/spire05.pdf]
 */
public class NGram(public val n: Int = DEFAULT_N) : NormalizedStringDistance {
    /**
     * Compute n-gram distance.
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed n-gram distance in the range [0, 1]
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2) {
            return 0.0
        }
        val special = '\n'
        val sl = s1.length
        val tl = s2.length
        if (sl == 0 || tl == 0) {
            return 1.0
        }
        var cost = 0
        if (sl < n || tl < n) {
            var i = 0
            val ni: Int = min(sl, tl)
            while (i < ni) {
                if (s1[i] == s2[i]) {
                    cost++
                }
                i++
            }
            return cost.toDouble() / max(sl, tl)
        }
        val sa = CharArray(sl + n - 1)
        var p: DoubleArray // 'previous' cost array, horizontally
        var d: DoubleArray // cost array, horizontally
        var d2: DoubleArray // placeholder to assist in swapping p and d
        
        // construct sa with prefix
        for (i in sa.indices) {
            if (i < n - 1) {
                sa[i] = special // add prefix
            } else {
                sa[i] = s1[i - n + 1]
            }
        }
        p = DoubleArray(sl + 1)
        d = DoubleArray(sl + 1)
        
        // indexes into strings s and t
        var i: Int // iterates through source
        var tJ = CharArray(n) // jth n-gram of t
        i = 0
        while (i <= sl) {
            p[i] = i.toDouble()
            i++
        }
        var j = 1 // iterates through target
        while (j <= tl) {
            
            // construct t_j n-gram
            if (j < n) {
                for (ti in 0 until n - j) {
                    tJ[ti] = special // add prefix
                }
                for (ti in n - j until n) {
                    tJ[ti] = s2[ti - (n - j)]
                }
            } else {
                tJ = s2.substring(j - n, j).toCharArray()
            }
            d[0] = j.toDouble()
            i = 1
            while (i <= sl) {
                cost = 0
                var tn = n
                // compare sa to t_j
                for (ni in 0 until n) {
                    if (sa[i - 1 + ni] != tJ[ni]) {
                        cost++
                    } else if (sa[i - 1 + ni] == special) {
                        // discount matches on prefix
                        tn--
                    }
                }
                val ec = cost.toDouble() / tn
                // minimum of cell to the left+1, to the top+1,
                // diagonally left and up +cost
                d[i] = min(min(d[i - 1] + 1, p[i] + 1), p[i - 1] + ec)
                i++
            }
            // copy current distance counts to 'previous row' distance counts
            d2 = p
            p = d
            d = d2
            j++
        }
        
        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[sl] / max(tl, sl)
    }
    
    private companion object {
        private const val DEFAULT_N = 2
    }
}