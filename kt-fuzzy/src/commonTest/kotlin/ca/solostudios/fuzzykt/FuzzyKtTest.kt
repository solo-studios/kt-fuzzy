/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2021-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file FuzzyKtTest.kt is part of kotlin-fuzzy
 * Last modified on 29-09-2023 07:53 p.m.
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

import ca.solostudios.fuzzykt.utils.FuzzyTestData
import io.kotest.core.spec.style.FunSpec

class FuzzyKtTest : FunSpec({
    val precomputedRatios =
        listOf(
            FuzzyTestData("this is a test", "this is a test!", 0.93333),
            FuzzyTestData("fuzzy wuzzy was a bear", "wuzzy fuzzy was a bear", 0.91304),
            FuzzyTestData("abcd", "abcd", 1.0),
            FuzzyTestData("mysmilarstring", "myawfullysimilarstirng", 0.56521),
            FuzzyTestData("mysmilarstring", "mysimilarstring", 0.93333)
        )
    testPrecomputed("Ratio should match all pre-computed results", precomputedRatios) { s1, s2 ->
        FuzzyKt.ratio(s1, s2)
    }

    val precomputedPartialRatios =
        listOf(
            FuzzyTestData("this is a test", "this is a test!", 1.0),
            FuzzyTestData("fuzzy wuzzy was a bear", "wuzzy fuzzy was a bear", 0.91304),
            FuzzyTestData("abcd", "abcd", 1.0),
            FuzzyTestData("mysmilarstring", "myawfullysimilarstirng", 0.43589),
            FuzzyTestData("mysmilarstring", "mysimilarstring", 0.86666),
            FuzzyTestData("similar", "somewhresimlrbetweenthisstring", 0.55555)
        )
    testPrecomputed("Partial ratio should match all pre-computed results", precomputedPartialRatios) { s1, s2 ->
        FuzzyKt.partialRatio(s1, s2)
    }

    val precomputedSubstrings =
        listOf(
            Triple("abcd", "bc", 1 to 3),
            Triple("abcd", "123", 0 to 0),
            Triple("abcd", "cdef", 2 to 4),
            Triple("aaaa", "a", 0 to 1),
            Triple("aabbaaa", "aaa", 4 to 7)
        )

    testPrecomputed("Longest common substrings should match all pre-computed results", precomputedSubstrings) { s1, s2 ->
        FuzzyKt.longestCommonSubstring(s1, s2)
    }
})
