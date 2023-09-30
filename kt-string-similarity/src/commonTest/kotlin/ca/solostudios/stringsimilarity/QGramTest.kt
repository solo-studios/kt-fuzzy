/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file QGramTest.kt is part of kotlin-fuzzy
 * Last modified on 01-09-2023 11:36 p.m.
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

import ca.solostudios.fuzzykt.utils.FuzzyTestData
import ca.solostudios.stringsimilarity.factories.distanceTests
import ca.solostudios.stringsimilarity.factories.precomputedDistanceTests
import io.kotest.core.spec.style.FunSpec

class QGramTest : FunSpec({
    val qGram = QGram(2)

    include(distanceTests(qGram))

    val precomputed = listOf(
        FuzzyTestData("01000", "001111", 5.0),
        FuzzyTestData("ABCD", "ABCE", 2.0),
        FuzzyTestData("S", "S", 0.0),
        FuzzyTestData("012345", "012345", 0.0),
        FuzzyTestData("", "", 0.0),
        FuzzyTestData("", "foo", 2.0),
        FuzzyTestData("foo", "", 2.0),
    )

    include(precomputedDistanceTests(precomputed, qGram))
})
