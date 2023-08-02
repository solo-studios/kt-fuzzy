/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file MetricTests.kt is part of kotlin-fuzzy
 * Last modified on 02-08-2023 06:33 p.m.
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

import ca.solostudios.stringsimilarity.interfaces.MetricStringDistance
import ca.solostudios.stringsimilarity.utils.DEFAULT_TOLERANCE
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.matchers.doubles.shouldBeZero
import io.kotest.matchers.doubles.shouldNotBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

fun metricDistanceTests(metricDistance: MetricStringDistance, includeStandard: Boolean = true) = funSpec {
    if (includeStandard)
        include(distanceTests(metricDistance))

    test("Metric distance should respect the symmetry axiom") {
        checkAll<String, String> { a, b ->
            withClue("distance(a, b) should be equal to distance(b, a)") {
                metricDistance.distance(a, b) shouldBe (metricDistance.distance(b, a) plusOrMinus DEFAULT_TOLERANCE)
            }
        }
    }

    test("Metric distance should respect the identity axiom") {
        checkAll<String> { a ->
            metricDistance.distance(a, a).shouldBeZero()
        }
    }

    test("Metric distance should respect the non-negativity axiom") {
        checkAll<String, String> { a, b ->
            metricDistance.distance(a, b) shouldNotBeLessThan 0.0
        }
    }

    test("Metric distance should respect the triangle inequality") {
        checkAll<String, String, String> { a, b, c ->
            val ab = metricDistance.distance(a, b)
            val ac = metricDistance.distance(a, c)
            val bc = metricDistance.distance(b, c)
            // in a triangle, distance AB must be >= distance AC + BC
            withClue("ab=$ab, ac=$ac, bc=$bc") {
                ab shouldBeLessThanOrEqual ac + bc
            }
        }
    }
}
