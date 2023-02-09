/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file Sift4.kt is part of kt-fuzzy
 * Last modified on 09-02-2023 12:37 p.m.
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

import ca.solostudios.stringsimilarity.annotations.ExperimentalSimilarity
import ca.solostudios.stringsimilarity.interfaces.StringDistance
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

/**
 * Sift4 - a general purpose string distance algorithm inspired by JaroWinkler
 * and Longest Common Subsequence.
 * Original JavaScript algorithm by siderite, java port by Nathan Fischer 2016.
 * [https://siderite.dev/blog/super-fast-and-accurate-string-distance.html]
 * [https://blackdoor.github.io/blog/sift4-java/]
 *
 * @author Thibault Debatty
 */
@ExperimentalSimilarity
public class Sift4(
        /**
         * Set the maximum distance to search for character transposition.
         * Compute cost of algorithm is `O(n * maxOffset)`
         */
        private val maxOffset: Int = DEFAULT_MAX_OFFSET
                  ) : StringDistance {
    
    /**
     * Sift4 - a general purpose string distance algorithm inspired by
     * JaroWinkler and Longest Common Subsequence.
     * Original JavaScript algorithm by siderite, java port by Nathan Fischer 2016.
     *
     * [https://siderite.dev/blog/super-fast-and-accurate-string-distance.html]
     * [https://blackdoor.github.io/blog/sift4-java/]
     *
     * @param s1
     * @param s2
     * @return
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1.isEmpty()) {
            return s2.length.toDouble()
        }
        
        if (s2.isEmpty()) {
            return s1.length.toDouble()
        }
        val l1 = s1.length
        val l2 = s2.length
        var c1 = 0 // cursor for string 1
        var c2 = 0 // cursor for string 2
        var lcss = 0 // largest common subsequence
        var localCs = 0 // local common substring
        var trans = 0 // number of transpositions ('ab' vs 'ba')
        
        /**
         * Used to store relation between same character in different positions
         * c1 and c2 in the input strings.
         */
        class Offset(val c1: Int, val c2: Int, var trans: Boolean)
        
        // offset pair array, for computing the transpositions
        val offsetArr = mutableListOf<Offset>()
        
        while (c1 < l1 && c2 < l2) {
            if (s1[c1] == s2[c2]) {
                localCs++
                var isTrans = false //
                // see if current match is a transposition
                var i = 0
                while (i < offsetArr.size) {
                    val ofs: Offset = offsetArr[i]
                    if (c1 <= ofs.c1 || c2 <= ofs.c2) {
                        // when two matches cross, the one considered a
                        // transposition is the one with the largest difference
                        // in offsets
                        isTrans = abs(c2 - c1) >= abs(ofs.c2 - ofs.c1)
                        if (isTrans) {
                            trans++
                        } else {
                            if (!ofs.trans) {
                                ofs.trans = true
                                trans++
                            }
                        }
                        break
                    } else {
                        if (c1 > ofs.c2 && c2 > ofs.c1) {
                            offsetArr.removeAt(i)
                        } else {
                            i++
                        }
                    }
                }
                offsetArr.add(Offset(c1, c2, isTrans))
            } else {
                
                // s1.charAt(c1) != s2.charAt(c2)
                lcss += localCs
                localCs = 0
                if (c1 != c2) {
                    // using min allows the computation of transpositions
                    c1 = min(c1, c2)
                    c2 = c1
                }
                
                // if matching characters are found, remove 1 from both cursors
                // (they get incremented at the end of the loop)
                // so that we can have only one code block handling matches
                var i = 0
                while (i < maxOffset && (c1 + i < l1 || c2 + i < l2)) {
                    if (c1 + i < l1 && s1[c1 + i] == s2[c2]) {
                        c1 += i - 1
                        c2--
                        break
                    }
                    if (c2 + i < l2 && s1[c1] == s2[c2 + i]) {
                        c1--
                        c2 += i - 1
                        break
                    }
                    i++
                }
            }
            c1++
            c2++
            // this covers the case where the last match is on the last token
            // in list, so that it can compute transpositions correctly
            if (c1 >= l1 || c2 >= l2) {
                lcss += localCs
                localCs = 0
                c1 = min(c1, c2)
                c2 = c1
            }
        }
        lcss += localCs
        // add the cost of transpositions to the final result
        return round(((max(l1, l2) - lcss + trans).toDouble()))
    }
    
    private companion object {
        private const val DEFAULT_MAX_OFFSET = 10
    }
}