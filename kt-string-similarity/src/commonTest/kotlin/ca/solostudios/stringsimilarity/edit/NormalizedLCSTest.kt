/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NormalizedLCSTest.kt is part of kotlin-fuzzy
 * Last modified on 01-08-2023 09:43 p.m.
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

import ca.solostudios.fuzzykt.utils.FuzzyTestData
import ca.solostudios.stringsimilarity.factories.metricDistanceTests
import ca.solostudios.stringsimilarity.factories.normalizedDistanceTests
import ca.solostudios.stringsimilarity.factories.normalizedSimilarityTests
import ca.solostudios.stringsimilarity.factories.precomputedDistanceTests
import ca.solostudios.stringsimilarity.factories.precomputedSimilarityTests
import io.kotest.core.spec.style.FunSpec

class NormalizedLCSTest : FunSpec({
    val normalizedLCS = NormalizedLCS()

    include(metricDistanceTests(normalizedLCS))
    include(normalizedDistanceTests(normalizedLCS, false))
    include(normalizedSimilarityTests(normalizedLCS))

    val precomputed = listOf(
        FuzzyTestData("AGCAT", "GAC", 0.66666),
        FuzzyTestData("AGCAT", "AGCT", 0.2),
        FuzzyTestData("ABCDE", "ABCDF", 0.33333),
        FuzzyTestData("ABCDEF", "ABDCEF", 0.28571),
        FuzzyTestData("ABCDEF", "BACDFE", 0.5),
        FuzzyTestData("ABCDEF", "ABCDE", 0.16666),
        FuzzyTestData("U5NvE5B242q6YtIc5", "cXV7655wniS37", 0.92857),
        FuzzyTestData("pYmO5Wv8z2Jk", "7zdJH16A0d42q8r78dh", 0.93103),
        FuzzyTestData("AwjI1Z6Gc58qKgh429IMk", "8Uw64CO0W1zBU6519uD0b2", 0.86842),
        FuzzyTestData("AHu5hCc4wGsz6sK583lL", "837zdBejiKzPHWLw3", 0.91176),
        FuzzyTestData("rxGFJothWFimR9YURkSR3V", "W5CbF", 0.92),
        FuzzyTestData("m75tEQEf4p6", "AOFn5fm", 0.875),
        FuzzyTestData("903F7nNC0YP1", "8ADG5jBAry", 1.0),
    )

    include(precomputedDistanceTests(precomputed, normalizedLCS))
    include(precomputedSimilarityTests(precomputed.map { it.copy(result = 1 - it.result) }, normalizedLCS))
})
