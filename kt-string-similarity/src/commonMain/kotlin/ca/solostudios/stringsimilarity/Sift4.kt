/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Sift4.kt is part of kotlin-fuzzy
 * Last modified on 09-08-2023 11:13 p.m.
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

import ca.solostudios.stringsimilarity.annotations.ExperimentalStringMeasure
import ca.solostudios.stringsimilarity.interfaces.StringDistance
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Sift4 - a general purpose string distance algorithm inspired by JaroWinkler
 * and Longest Common Subsequence.
 * Original JavaScript algorithm by siderite, java port by Nathan Fischer 2016.
 * [https://siderite.dev/blog/super-fast-and-accurate-string-distance.html]
 * [https://blackdoor.github.io/blog/sift4-java/]
 *
 * @author Thibault Debatty, solonovamax
 */
@ExperimentalStringMeasure
public class Sift4(
    /**
     * Set the maximum distance to search for character transposition.
     * Compute cost of algorithm is `O(n * maxOffset)`
     */
    private val maxOffset: Int = DEFAULT_MAX_OFFSET,
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
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty())
            return s2.length.toDouble()
        if (s2.isEmpty())
            return s1.length.toDouble()

        var cursor1 = 0 // cursor for string 1
        var cursor2 = 0 // cursor for string 2
        var largestCommonSubstringLength = 0 // largest common subsequence
        var localCommonSubstringLength = 0 // local common substring
        var transpositionCount = 0 // number of transpositions ('ab' vs 'ba')

        /**
         * Used to store relation between same character in different positions
         * c1 and c2 in the input strings.
         */
        class Offset(val cursor1: Int, val cursor2: Int, var transposition: Boolean)

        // offset pair array, for computing the transpositions
        val offsets = mutableListOf<Offset>()

        // wtf is this doing
        while (cursor1 < s1.length && cursor2 < s2.length) {
            val c1 = s1[cursor1]
            val c2 = s2[cursor2]

            if (c1 == c2) {
                localCommonSubstringLength++
                var transposition = false
                // see if current match is a transposition
                var i = 0
                while (i < offsets.size) {
                    val offset = offsets[i]
                    if (cursor1 <= offset.cursor1 || cursor2 <= offset.cursor2) {
                        // when two matches cross, the one considered a
                        // transposition is the one with the largest difference
                        // in offsets
                        transposition = abs(cursor2 - cursor1) >= abs(offset.cursor2 - offset.cursor1)
                        if (transposition) {
                            transpositionCount++
                        } else {
                            if (!offset.transposition) {
                                offset.transposition = true
                                transpositionCount++
                            }
                        }
                        break
                    } else {
                        if (cursor1 > offset.cursor2 && cursor2 > offset.cursor1) {
                            offsets.removeAt(i)
                        } else {
                            i++
                        }
                    }
                }
                offsets.add(Offset(cursor1, cursor2, transposition))
            } else {
                largestCommonSubstringLength += localCommonSubstringLength
                localCommonSubstringLength = 0
                if (cursor1 != cursor2) {
                    cursor1 = min(cursor1, cursor2) // using min allows the computation of transpositions
                    cursor2 = cursor1
                }

                // if matching characters are found, remove 1 from both cursors
                // (they get incremented at the end of the loop)
                // so that we can have only one code block handling matches
                var i = 0
                while (i < maxOffset && (cursor1 + i < s1.length || cursor2 + i < s2.length)) {
                    if (cursor1 + i < s1.length && s1[cursor1 + i] == c2) {
                        cursor1 += i - 1
                        cursor2--
                        break
                    }
                    if (cursor2 + i < s2.length && c1 == s2[cursor2 + i]) {
                        cursor1--
                        cursor2 += i - 1
                        break
                    }
                    i++
                }
            }
            cursor1++
            cursor2++

            // this covers the case where the last match is on the last token
            // in list, so that it can compute transpositions correctly
            if (cursor1 >= s1.length || cursor2 >= s2.length) {
                largestCommonSubstringLength += localCommonSubstringLength
                localCommonSubstringLength = 0
                cursor1 = min(cursor1, cursor2)
                cursor2 = cursor1
            }
        }
        largestCommonSubstringLength += localCommonSubstringLength
        // add the cost of transpositions to the final result
        return (max(s1.length, s2.length) - largestCommonSubstringLength + transpositionCount).toDouble()
    }

    /**
     * Default Sift4 dice instance
     */
    @ExperimentalStringMeasure
    public companion object : StringDistance {
        private val defaultMeasure = Sift4()

        private const val DEFAULT_MAX_OFFSET = 10

        override fun distance(s1: String, s2: String): Double = defaultMeasure.distance(s1, s2)
    }
}
