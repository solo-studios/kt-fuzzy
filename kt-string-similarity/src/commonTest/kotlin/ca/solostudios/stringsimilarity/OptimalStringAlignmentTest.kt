/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file OptimalStringAlignmentTest.kt is part of kotlin-fuzzy
 * Last modified on 19-10-2023 05:27 p.m.
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
import ca.solostudios.stringsimilarity.factories.precomputedSimilarityTests
import ca.solostudios.stringsimilarity.factories.similarityTests
import io.kotest.core.spec.style.FunSpec

class OptimalStringAlignmentTest : FunSpec({
    val optimalStringAlignment = OptimalStringAlignment()

    include(distanceTests(optimalStringAlignment))
    include(similarityTests(optimalStringAlignment))

    val precomputed = listOf(
        FuzzyTestData("AGCAT", "GAC", 3.0),
        FuzzyTestData("AGCAT", "AGCT", 1.0),
        FuzzyTestData("ABCDE", "ABCDF", 1.0),
        FuzzyTestData("ABCDEF", "ABDCEF", 1.0),
        FuzzyTestData("ABCDEF", "BACDFE", 2.0),
        FuzzyTestData("ABCDEF", "ABCDE", 1.0),
        FuzzyTestData("U5NvE5B242q6YtIc5", "cXV7655wniS37", 16.0),
        FuzzyTestData("pYmO5Wv8z2Jk", "7zdJH16A0d42q8r78dh", 18.0),
        FuzzyTestData("AwjI1Z6Gc58qKgh429IMk", "8Uw64CO0W1zBU6519uD0b2", 21.0),
        FuzzyTestData("AHu5hCc4wGsz6sK583lL", "837zdBejiKzPHWLw3", 18.0),
        FuzzyTestData("rxGFJothWFimR9YURkSR3V", "W5CbF", 21.0),
        FuzzyTestData("m75tEQEf4p6", "AOFn5fm", 10.0),
        FuzzyTestData("903F7nNC0YP1", "8ADG5jBAry", 12.0),
    )
    include(precomputedDistanceTests(precomputed, optimalStringAlignment))
    include(
        precomputedSimilarityTests(
            precomputed.map { it.copy(result = ((it.first.length + it.second.length) - it.result) / 2) },
            optimalStringAlignment
        )
    )
})
