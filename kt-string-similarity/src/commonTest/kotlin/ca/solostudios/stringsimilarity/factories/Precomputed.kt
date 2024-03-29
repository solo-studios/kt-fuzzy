/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Precomputed.kt is part of kotlin-fuzzy
 * Last modified on 29-09-2023 08:01 p.m.
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

package ca.solostudios.stringsimilarity.factories

import ca.solostudios.fuzzykt.testPrecomputed
import ca.solostudios.fuzzykt.utils.FuzzyTestData
import ca.solostudios.stringsimilarity.interfaces.StringDistance
import ca.solostudios.stringsimilarity.interfaces.StringSimilarity
import io.kotest.core.spec.style.funSpec

fun precomputedSimilarityTests(precomputed: List<FuzzyTestData>, similarity: StringSimilarity) = funSpec {
    testPrecomputed("Similarity should match all pre-computed similarities", precomputed) { s1, s2 -> similarity.similarity(s1, s2) }
}

fun precomputedDistanceTests(precomputed: List<FuzzyTestData>, distance: StringDistance) = funSpec {
    testPrecomputed("Distance should match all pre-computed distances", precomputed) { s1, s2 -> distance.distance(s1, s2) }
}
