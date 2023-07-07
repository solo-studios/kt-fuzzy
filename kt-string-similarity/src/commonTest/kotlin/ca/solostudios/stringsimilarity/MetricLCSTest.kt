/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file MetricLCSTest.kt is part of kotlin-fuzzy
 * Last modified on 06-07-2023 04:38 p.m.
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

class MetricLCSTest {
    @Test
    fun testDistance() {
        val metricLCS = MetricLCS()

        // LCS: "ABCDEF" => length = 6
        // longest = "ABCDEFHJKL" => length = 10
        // => 1 - 6/10 = 0.4
        assertEquals(metricLCS.distance("ABCDEFG", "ABCDEFHJKL"), 0.4, absoluteTolerance = 0.001) // prints 0.4

        // LCS: "ABDF" => length = 4
        // longest = "ABDEF" => length = 5
        // => 1 - 4 / 5 = 0.2
        assertEquals(metricLCS.distance("ABDEF", "ABDIF"), 0.2, absoluteTolerance = 0.001) // prints 0.2
    }
}
