/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Sift4Test.kt is part of kotlin-fuzzy
 * Last modified on 04-09-2023 07:36 p.m.
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

import ca.solostudios.fuzzykt.utils.FuzzyTestData
import ca.solostudios.stringsimilarity.annotations.ExperimentalStringMeasure
import ca.solostudios.stringsimilarity.factories.distanceTests
import ca.solostudios.stringsimilarity.factories.precomputedDistanceTests
import io.kotest.core.spec.style.FunSpec

@OptIn(ExperimentalStringMeasure::class)
class Sift4Test : FunSpec({
    val sift4 = Sift4()

    include(distanceTests(sift4))

    val precomputed = listOf(
        FuzzyTestData("My string", "My tsring", 1.0),
        FuzzyTestData("My string", "My ntrisg", 2.0),
        FuzzyTestData("ABCDE", "ABCDF", 1.0),
        FuzzyTestData("ABCDEF", "ABDCEF", 1.0),
        FuzzyTestData("ABCDEF", "BACDFE", 2.0),
        FuzzyTestData("ABCDEF", "ABCDE", 1.0),
        FuzzyTestData("U5NvE5B242q6YtIc5", "cXV7655wniS37", 16.0),
        FuzzyTestData("pYmO5Wv8z2Jk", "7zdJH16A0d42q8r78dh", 17.0),
        FuzzyTestData("AwjI1Z6Gc58qKgh429IMk", "8Uw64CO0W1zBU6519uD0b2", 18.0),
        FuzzyTestData("AHu5hCc4wGsz6sK583lL", "837zdBejiKzPHWLw3", 17.0),
        FuzzyTestData("rxGFJothWFimR9YURkSR3V", "W5CbF", 21.0),
        FuzzyTestData("m75tEQEf4p6", "AOFn5fm", 9.0),
        FuzzyTestData("903F7nNC0YP1", "8ADG5jBAry", 12.0),
    )

    include(precomputedDistanceTests(precomputed, sift4))
})
