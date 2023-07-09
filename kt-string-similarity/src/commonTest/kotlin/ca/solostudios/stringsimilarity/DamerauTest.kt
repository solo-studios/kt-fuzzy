/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file DamerauTest.kt is part of kotlin-fuzzy
 * Last modified on 09-07-2023 06:57 p.m.
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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 *
 * @author Thibault Debatty
 */
class DamerauTest {
    /**
     * Test of distance method, of class Damerau.
     */
    @Test
    fun testDistance() {
        val damerau = Damerau()
        assertEquals(1.0, damerau.distance("ABCDEF", "ABDCEF"), absoluteTolerance = 0.001)
        assertEquals(2.0, damerau.distance("ABCDEF", "BACDFE"), absoluteTolerance = 0.001)
        assertEquals(1.0, damerau.distance("ABCDEF", "ABCDE"), absoluteTolerance = 0.001)
    }

    @Test
    fun testMetricDistance() {
        val damerau = Damerau()

        // Identify axiom
        //   The distance d(x, y) for any x and y is equal to 0 if and only if x = y
        run {
            assertEquals(0.0, damerau.distance("TEST", "TEST"), absoluteTolerance = 0.001)
            assertNotEquals(0.0, damerau.distance("TEST", "TEST1"), absoluteTolerance = 0.001)
        }

        // Symmetry axiom
        //   The distance between any x and y is equal to the distance between y and x
        run {
            val x = "t60e2wc12qM8"
            val y = "6V00MIbZQn9bWW6hQAtpAY"
            val distanceXY = damerau.distance(x, y)
            val distanceYX = damerau.distance(y, x)
            assertEquals(distanceXY, distanceYX, absoluteTolerance = 0.001)
        }

        // Non-Negativity or Separation axiom
        //   The distance between any x and y is never negative
        run {
            val x = "1AlD1RzAABOZ2entPMPV"
            val y = "wL01U69oC9"
            assertTrue {
                damerau.distance(x, y) >= 0
            }
        }
    }
}
