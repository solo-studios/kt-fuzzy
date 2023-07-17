/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file JaccardTest.kt is part of kotlin-fuzzy
 * Last modified on 17-07-2023 05:59 p.m.
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

import ca.solostudios.stringsimilarity.utils.FuzzyTestData
import io.kotest.core.spec.style.FunSpec

/**
 *
 * @author Thibault Debatty
 */
class JaccardTest : FunSpec({
    val jaccard = Jaccard(2)

    include(metricDistanceTests(jaccard))
    include(normalizedSimilarityTests(jaccard))
    include(normalizedDistanceTests(jaccard))

    val precomputed = listOf(
        FuzzyTestData("ABCDE", "ABCDF", 0.6),
        FuzzyTestData("ABCDEF", "ABDCEF", 0.25),
        FuzzyTestData("ABCDEF", "BACDFE", 0.11111),
        FuzzyTestData("ABCDEF", "ABCDE", 0.80000),
        FuzzyTestData("U5NvE5B242q6YtIc5", "cXV7655wniS37", 0.0),
        FuzzyTestData("pYmO5Wv8z2Jk", "7zdJH16A0d42q8r78dh", 0.0),
        FuzzyTestData("AwjI1Z6Gc58qKgh429IMk", "8Uw64CO0W1zBU6519uD0b2", 0.0),
        FuzzyTestData("AHu5hCc4wGsz6sK583lL", "837zdBejiKzPHWLw3", 0.02941),
        FuzzyTestData("rxGFJothWFimR9YURkSR3V", "W5CbF", 0.0),
        FuzzyTestData("m75tEQEf4p6", "AOFn5fm", 0.0),
        FuzzyTestData("903F7nNC0YP1", "8ADG5jBAry", 0.0),
    )

    include(precomputedSimilarityTests(precomputed, jaccard))
    include(precomputedDistanceTests(precomputed.map { it.copy(similarity = 1 - it.similarity) }, jaccard))
})
