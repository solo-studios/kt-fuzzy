/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file RatcliffObershelpTest.kt is part of kotlin-fuzzy
 * Last modified on 04-09-2023 05:57 p.m.
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
import ca.solostudios.stringsimilarity.factories.normalizedDistanceTests
import ca.solostudios.stringsimilarity.factories.normalizedSimilarityTests
import ca.solostudios.stringsimilarity.factories.precomputedDistanceTests
import ca.solostudios.stringsimilarity.factories.precomputedSimilarityTests
import io.kotest.core.spec.style.FunSpec

class RatcliffObershelpTest : FunSpec({
    val ratcliffObershelp = RatcliffObershelp

    include(normalizedDistanceTests(ratcliffObershelp))
    include(normalizedSimilarityTests(ratcliffObershelp))

    val precomputed = listOf(
        FuzzyTestData("My string", "My tsring", 0.88888),
        FuzzyTestData("My string", "My ntrisg", 0.77777),
        FuzzyTestData("MATEMATICA", "MATHEMATICS", 0.85714),
        FuzzyTestData("aleksander", "alexandre", 0.73684),
        FuzzyTestData("pennsylvania", "pencilvaneya", 0.66666),
        FuzzyTestData("WIKIMEDIA", "WIKIMANIA", 0.77777),
        FuzzyTestData("GESTALT PATTERN MATCHING", "GESTALT PRACTICE", 0.6),
        FuzzyTestData("GESTALT PRACTICE", "GESTALT PATTERN MATCHING", 0.65),
    )

    include(precomputedSimilarityTests(precomputed, ratcliffObershelp))
    include(precomputedDistanceTests(precomputed.map { it.copy(result = 1 - it.result) }, ratcliffObershelp))
})
