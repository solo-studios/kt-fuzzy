/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file WeightedLevenshteinTest.kt is part of kt-fuzzy
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

class WeightedLevenshteinTest {
    @Test
    fun testDistance() {
        val instance = WeightedLevenshtein(
                { c1: Char, c2: Char ->
                    // The cost for substituting 't' and 'r' is considered
                    // smaller as these 2 are located next to each other
                    // on a keyboard
                    return@WeightedLevenshtein if (c1 == 't' && c2 == 'r') {
                        0.5
                    } else 1.0
                    
                    // For most cases, the cost of substituting 2 characters
                    // is 1.0
                },
                                          )
        assertEquals(0.0, instance.distance("String1", "String1"), 0.1)
        assertEquals(0.5, instance.distance("String1", "Srring1"), 0.1)
        assertEquals(1.5, instance.distance("String1", "Srring2"), 0.1)
        
        // One insert or delete.
        assertEquals(1.0, instance.distance("Strng", "String"), 0.1)
        assertEquals(1.0, instance.distance("String", "Strng"), 0.1)
        
        // With limits.
        assertEquals(0.0, instance.distance("String1", "String1", Double.MAX_VALUE), 0.1)
        assertEquals(0.0, instance.distance("String1", "String1", 2.0), 0.1)
        assertEquals(1.5, instance.distance("String1", "Srring2", Double.MAX_VALUE), 0.1)
        assertEquals(1.5, instance.distance("String1", "Srring2", 2.0), 0.1)
        assertEquals(1.5, instance.distance("String1", "Srring2", 1.5), 0.1)
        assertEquals(1.0, instance.distance("String1", "Srring2", 1.0), 0.1)
        assertEquals(4.0, instance.distance("String1", "Potato", 4.0), 0.1)
    }
    
    @Test
    fun testDistanceCharacterInsDelInterface() {
        val instance = WeightedLevenshtein(
                { c1: Char, c2: Char ->
                    return@WeightedLevenshtein if (c1 == 't' && c2 == 'r') {
                        0.5
                    } else 1.0
                },
                { c ->
                    WeightedLevenshtein.Weights(if (c == 'i') {
                        0.8
                    } else 1.0, if (c == 'i') {
                        0.5
                    } else 1.0)
                },
                                          )
        
        // Same as testDistance above.
        assertEquals(0.0, instance.distance("String1", "String1"), 0.1)
        assertEquals(0.5, instance.distance("String1", "Srring1"), 0.1)
        assertEquals(1.5, instance.distance("String1", "Srring2"), 0.1)
        
        // Cost of insert of 'i' is less than normal, so these scores are
        // different than testDistance above.  Note that the cost of delete
        // has been set differently than the cost of insert, so the distance
        // call is not symmetric in its arguments if an 'i' has changed.
        assertEquals(0.5, instance.distance("Strng", "String"), 0.1)
        assertEquals(0.8, instance.distance("String", "Strng"), 0.1)
        assertEquals(1.0, instance.distance("Strig", "String"), 0.1)
        assertEquals(1.0, instance.distance("String", "Strig"), 0.1)
        
        // Same as above with limits.
        assertEquals(0.0, instance.distance("String1", "String1", Double.MAX_VALUE), 0.1)
        assertEquals(0.0, instance.distance("String1", "String1", 2.0), 0.1)
        assertEquals(0.5, instance.distance("String1", "Srring1", Double.MAX_VALUE), 0.1)
        assertEquals(0.5, instance.distance("String1", "Srring1", 2.0), 0.1)
        assertEquals(1.5, instance.distance("String1", "Srring2", 2.0), 0.1)
        assertEquals(1.5, instance.distance("String1", "Srring2", 1.5), 0.1)
        assertEquals(1.0, instance.distance("String1", "Srring2", 1.0), 0.1)
        assertEquals(4.0, instance.distance("String1", "Potato", 4.0), 0.1)
    }
}