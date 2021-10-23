/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file QGramTest.kt is part of kt-fuzzy
 * Last modified on 22-10-2021 08:05 p.m.
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
 * KT-STRING-SIMILARITY IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
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

/**
 *
 * @author Thibault Debatty
 */
class QGramTest {
    /**
     * Test of distance method, of class QGram.
     */
    @Test
    fun testDistance() {
        println("distance")
        val instance = QGram(2)
        // AB BC CD CE
        // 1  1  1  0
        // 1  1  0  1
        // Total: 2
        val result: Double = instance.distance("ABCD", "ABCE")
        assertEquals(2.0, result, 0.0)
        assertEquals(
                0.0,
                instance.distance("S", "S"),
                0.0)
        assertEquals(0.0,
                     instance.distance("012345", "012345"),
                     0.0)
        
        // NOTE: not using null/empty tests in NullEmptyTests because QGram is different
        assertEquals(0.0, instance.distance("", ""), 0.1)
        assertEquals(2.0, instance.distance("", "foo"), 0.1)
        assertEquals(2.0, instance.distance("foo", ""), 0.1)
    }
}