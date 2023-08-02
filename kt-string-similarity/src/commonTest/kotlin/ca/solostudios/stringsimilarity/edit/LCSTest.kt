/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LCSTest.kt is part of kotlin-fuzzy
 * Last modified on 01-08-2023 11:29 p.m.
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
package ca.solostudios.stringsimilarity.edit

import ca.solostudios.stringsimilarity.metricDistanceTests
import ca.solostudios.stringsimilarity.precomputedDistanceTests
import ca.solostudios.stringsimilarity.precomputedSimilarityTests
import ca.solostudios.stringsimilarity.similarityTests
import ca.solostudios.stringsimilarity.utils.FuzzyTestData
import io.kotest.core.spec.style.FunSpec

class LCSTest : FunSpec({
    val lcs = LCS()

    include(metricDistanceTests(lcs))
    include(similarityTests(lcs))

    val precomputed = listOf(
        FuzzyTestData("AGCAT", "GAC", 4.0),
        FuzzyTestData("AGCAT", "AGCT", 1.0),
        FuzzyTestData("ABCDE", "ABCDF", 2.0),
        FuzzyTestData("ABCDEF", "ABDCEF", 2.0),
        FuzzyTestData("ABCDEF", "BACDFE", 4.0),
        FuzzyTestData("ABCDEF", "ABCDE", 1.0),
        FuzzyTestData("U5NvE5B242q6YtIc5", "cXV7655wniS37", 26.0),
        FuzzyTestData("pYmO5Wv8z2Jk", "7zdJH16A0d42q8r78dh", 27.0),
        FuzzyTestData("AwjI1Z6Gc58qKgh429IMk", "8Uw64CO0W1zBU6519uD0b2", 33.0),
        FuzzyTestData("AHu5hCc4wGsz6sK583lL", "837zdBejiKzPHWLw3", 31.0),
        FuzzyTestData("rxGFJothWFimR9YURkSR3V", "W5CbF", 23.0),
        FuzzyTestData("m75tEQEf4p6", "AOFn5fm", 14.0),
        FuzzyTestData("903F7nNC0YP1", "8ADG5jBAry", 22.0),
    )

    include(precomputedDistanceTests(precomputed, lcs))
    include(
        precomputedSimilarityTests(
            precomputed.map { it.copy(similarity = ((it.first.length + it.second.length) - it.similarity) / 2) },
            lcs
        )
    )
})
