/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file JaccardTest.kt is part of kt-fuzzy
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
class JaccardTest {
    /**
     * Test of similarity method, of class Jaccard.
     */
    @Test
    fun testSimilarity() {
        println("similarity")
        val instance = Jaccard(2)
        
        // AB BC CD DE DF
        // 1  1  1  1  0
        // 1  1  1  0  1
        // => 3 / 5 = 0.6
        val result: Double = instance.similarity("ABCDE", "ABCDF")
        assertEquals(0.6, result, 0.0)
    }
    
    /**
     * Test of distance method, of class Jaccard.
     */
    @Test
    fun testDistance() {
        println("distance")
        val instance = Jaccard(2)
        val expResult = 0.4
        val result: Double = instance.distance("ABCDE", "ABCDF")
        assertEquals(expResult, result, 0.0)
    }
}