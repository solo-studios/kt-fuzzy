/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file PreComputed.kt is part of kotlin-fuzzy
 * Last modified on 17-07-2023 06:18 p.m.
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

import ca.solostudios.stringsimilarity.interfaces.StringDistance
import ca.solostudios.stringsimilarity.interfaces.StringSimilarity
import ca.solostudios.stringsimilarity.utils.FuzzyTestData
import io.kotest.core.spec.style.funSpec
import io.kotest.core.spec.style.scopes.FunSpecRootScope

fun precomputedSimilarityTests(precomputed: List<FuzzyTestData>, similarity: StringSimilarity) = funSpec {
    testPrecomputed("Similarity should match all pre-computed similarities", precomputed) { s1, s2 -> similarity.similarity(s1, s2) }
}

fun precomputedDistanceTests(precomputed: List<FuzzyTestData>, distance: StringDistance) = funSpec {
    testPrecomputed("Distance should match all pre-computed distances", precomputed) { s1, s2 -> distance.distance(s1, s2) }
}

/**
 * Tests a precomputed value with a context.
 *
 * This has to be platform-specific because js hates nested tests, so on js it does a hack to not be nested.
 *
 * @param context The context name
 * @param precomputed The precomputed data
 * @param similarityFunction The similarity function
 */
expect fun FunSpecRootScope.testPrecomputed(
    context: String,
    precomputed: List<FuzzyTestData>,
    similarityFunction: (String, String) -> Double,
)
