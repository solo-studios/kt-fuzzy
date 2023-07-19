/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file CosineTest.kt is part of kotlin-fuzzy
 * Last modified on 19-07-2023 03:45 p.m.
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
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe

class CosineTest : FunSpec({
    val cosine = Cosine()
    val cosineSmallK = Cosine(3)

    include(normalizedSimilarityTests(cosine))
    include(normalizedDistanceTests(cosine))

    val precomputed = listOf(
        FuzzyTestData("ABC", "ABCE", 0.7071)
    )

    include(precomputedSimilarityTests(precomputed, cosine))
    include(precomputedDistanceTests(precomputed.map { it.copy(similarity = 1 - it.similarity) }, cosine))

    test("should be 0 for strings smaller than k") {
        cosineSmallK.similarity("AB", "ABCE") shouldBe (0.0 plusOrMinus 0.00001)
    }

    test("should be 1 for strings smaller than k") {
        cosineSmallK.distance("AB", "ABCE") shouldBe (1.0 plusOrMinus 0.00001)
    }

    /*
        test("Cosine similarity for large strings") {
            // read from 2 text files
            val string1 = readResourceFile("71816-2.txt")
            val string2 = readResourceFile("11328-1.txt")
            assertEquals(0.8115, cosine.similarity(string1, string2), 0.001)
        }
    */

    /*
        test("Cosine distance for large strings") {
            // read from 2 text files
            val string1 = readResourceFile("71816-2.txt")
            val string2 = readResourceFile("11328-1.txt")
            assertEquals(0.1885, cosine.distance(string1, string2), 0.001)
        }
    */
})
