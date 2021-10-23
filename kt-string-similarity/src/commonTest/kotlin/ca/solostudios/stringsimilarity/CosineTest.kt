/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file CosineTest.kt is part of kt-fuzzy
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
class CosineTest {
    /**
     * Test of similarity method, of class Cosine.
     */
    @Test
    fun testSimilarity() {
        println("similarity")
        val instance = Cosine()
        val result: Double = instance.similarity("ABC", "ABCE")
        assertEquals(0.71, result, 0.01)
    }
    
    /**
     * If one of the strings is smaller than k, the similarity should be 0.
     */
    @Test
    fun testSmallString() {
        println("test small string")
        val instance = Cosine(3)
        val result: Double = instance.similarity("AB", "ABCE")
        assertEquals(0.0, result, 0.00001)
    }
    
    /*
        @Test
        @Throws(IOException::class)
        fun testLargeString() {
            println("Test with large strings")
            val cos = Cosine()
            
            // read from 2 text files
            val string1 = readResourceFile("71816-2.txt")
            val string2 = readResourceFile("11328-1.txt")
            val similarity: Double = cos.similarity(string1, string2)
            assertEquals(0.8115, similarity, 0.001)
        }
    */
    
    @Test
    fun testDistance() {
        val instance = Cosine()
        val result: Double = instance.distance("ABC", "ABCE")
        assertEquals(0.29, result, 0.01)
    }
    
    @Test
    fun testDistanceSmallString() {
        println("test small string")
        val instance = Cosine(3)
        val result: Double = instance.distance("AB", "ABCE")
        assertEquals(1.0, result, 0.00001)
    }
    
    /*
        @Test
        fun testDistanceLargeString() {
            println("Test with large strings")
            val cos = Cosine()
            
            // read from 2 text files
            val string1 = readResourceFile("71816-2.txt")
            val string2 = readResourceFile("11328-1.txt")
            val similarity: Double = cos.distance(string1, string2)
            assertEquals(0.1885, similarity, 0.001)
        }
    */
}