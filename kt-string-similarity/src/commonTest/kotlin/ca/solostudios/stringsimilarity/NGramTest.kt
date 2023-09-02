/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NGramTest.kt is part of kotlin-fuzzy
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

import ca.solostudios.stringsimilarity.utils.FuzzyTestData
import io.kotest.core.spec.style.FunSpec

class NGramTest : FunSpec({
    val ngram = NGram(3)

    include(normalizedDistanceTests(ngram))
    include(normalizedSimilarityTests(ngram))

    val precomputed = listOf(
        FuzzyTestData("university", "univearsitty", 0.75),
        FuzzyTestData("university", "university", 1.0),
        FuzzyTestData("hello", "jello", 0.633333),
        FuzzyTestData("hello", "heloll", 0.666667),
        FuzzyTestData("hello", "saint", 0.0000),
        FuzzyTestData("hello", "", 0.0000),
        FuzzyTestData("ABABABAB", "POIULKJH", 0.0),
        FuzzyTestData("SIJK", "SIJK", 1.0),
        FuzzyTestData("S", "S", 1.0),
    )

    include(precomputedSimilarityTests(precomputed, ngram))
    include(precomputedDistanceTests(precomputed.map { it.copy(similarity = 1 - it.similarity) }, ngram))
})
