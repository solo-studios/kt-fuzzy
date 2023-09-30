/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NormalizedTests.kt is part of kotlin-fuzzy
 * Last modified on 29-09-2023 08:00 p.m.
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

import ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.matchers.doubles.shouldBeZero
import io.kotest.matchers.doubles.shouldNotBeGreaterThan
import io.kotest.matchers.doubles.shouldNotBeLessThan
import io.kotest.property.checkAll

fun normalizedSimilarityTests(similarity: NormalizedStringSimilarity, includeStandard: Boolean = true) = funSpec {
    if (includeStandard)
        include(similarityTests(similarity))

    test("Normalized similarity should be 1 for identical strings") {
        checkAll<String> { a ->
            similarity.similarity(a, a) shouldBeExactly 1.0
        }
    }

    test("Normalized similarity should not be greater than 1 or less than 1") {
        checkAll<String, String> { a, b ->
            similarity.similarity(a, b) shouldNotBeGreaterThan 1.0
            similarity.similarity(a, b) shouldNotBeLessThan 0.0
        }
    }
}

fun normalizedDistanceTests(distance: NormalizedStringDistance, includeStandard: Boolean = true) = funSpec {
    if (includeStandard)
        include(distanceTests(distance))

    test("Normalized distance should be 0 for identical strings") {
        checkAll<String> { a ->
            distance.distance(a, a).shouldBeZero()
        }
    }

    test("Normalized distance should not be greater than 1 or less than 1") {
        checkAll<String, String> { a, b ->
            distance.distance(a, b) shouldNotBeGreaterThan 1.0
            distance.distance(a, b) shouldNotBeLessThan 0.0
        }
    }
}
