/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file Sift4Test.kt is part of kt-fuzzy
 * Last modified on 22-10-2021 06:53 p.m.
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

import ca.solostudios.stringsimilarity.annotations.ExperimentalSimilarity
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
    @Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
    @OptIn(ExperimentalSimilarity::class)
    fun testDistance() {
        println("SIFT4 distance")
        val s1 = "This is the first string"
        val s2 = "And this is another string"
        val sift4 = Sift4()
        sift4.maxOffset = 5
        val expResult = 11.0
        val result = sift4.distance(s1, s2)
        assertEquals(expResult, result, 0.0)
        sift4.maxOffset = 10
        assertEquals(
                12.0,
                sift4.distance(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                        "Amet Lorm ispum dolor sit amet, consetetur adixxxpiscing elit."),
                0.0)
    }
}