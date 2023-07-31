/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Sift4Test.kt is part of kotlin-fuzzy
 * Last modified on 31-07-2023 06:14 p.m.
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

import ca.solostudios.stringsimilarity.annotations.ExperimentalStringMeasurement
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * @author Thibault Debatty
 */
class Sift4Test {
    /**
     * Test of distance method, of class Sift4.
     */
    @Test
    @OptIn(ExperimentalStringMeasurement::class)
    fun testDistance() {
        val sift4Offset5 = Sift4(maxOffset = 5)
        assertEquals(
            11.0,
            sift4Offset5.distance(
                "This is the first string",
                "And this is another string",
            ),
            absoluteTolerance = 0.001,
        )

        val sift4Offset10 = Sift4(maxOffset = 10)
        assertEquals(
            12.0,
            sift4Offset10.distance(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                "Amet Lorm ispum dolor sit amet, consetetur adixxxpiscing elit.",
            ),
            absoluteTolerance = 0.001,
        )
    }
}
