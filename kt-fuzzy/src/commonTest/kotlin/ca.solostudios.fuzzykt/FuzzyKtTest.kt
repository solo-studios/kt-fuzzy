/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2021-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file FuzzyKtTest.kt is part of kotlin-fuzzy
 * Last modified on 22-07-2023 05:08 p.m.
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

package ca.solostudios.fuzzykt

import kotlin.test.Test
import kotlin.test.assertEquals

class FuzzyKtTest {
    @Test
    fun testRatio() {
        val ratioTests =
                listOf( // s1, s2, expected
                        Triple("this is a test", "this is a test!", 0.9333333333333333),
                        Triple("fuzzy wuzzy was a bear", "wuzzy fuzzy was a bear", 0.9090909090909091),
                        Triple("abcd", "abcd", 1.0),
                        Triple("mysmilarstring", "myawfullysimilarstirng", 0.5454545454545454),
                        Triple("mysmilarstring", "mysimilarstring", 0.9333333333333333)
                      )
        for ((s1, s2, expected) in ratioTests) {
            assertEquals(expected = expected, actual = FuzzyKt.ratio(s1, s2), absoluteTolerance = 0.001)
        }
    }

    @Test
    fun testPartialRatio() {
        val partialRatioTests =
                listOf( // s1, s2, expected
                    Triple("this is a test", "this is a test!", 1.0),
                    Triple("fuzzy wuzzy was a bear", "wuzzy fuzzy was a bear", 0.9130434782608696),
                    Triple("abcd", "abcd", 1.0),
                    Triple("mysmilarstring", "myawfullysimilarstirng", 0.4358974358974359),
                    Triple("mysmilarstring", "mysimilarstring", 0.8666666666666667),
                    Triple("similar", "somewhresimlrbetweenthisstring", 0.5555555555555556)
                )
        for ((s1, s2, expected) in partialRatioTests) {
            assertEquals(expected = expected, actual = FuzzyKt.partialRatio(s1, s2), absoluteTolerance = 0.001)
        }
    }

    @Test
    fun testLongestCommonSubstring() {
        val substringTests =
                listOf( // s1, s2, expected result
                        Triple("abcd", "bc", 1 to 3),
                        Triple("abcd", "123", 0 to 0),
                        Triple("abcd", "cdef", 2 to 4),
                        Triple("aaaa", "a", 0 to 1),
                        Triple("aabbaaa", "aaa", 4 to 7)
                      )

        for ((s1, s2, expected) in substringTests) {
            assertEquals(expected = expected, actual = FuzzyKt.longestCommonSubstring(s1, s2))
        }
    }
}
