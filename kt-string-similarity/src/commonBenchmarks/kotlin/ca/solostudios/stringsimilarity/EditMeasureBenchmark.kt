/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file EditMeasureBenchmark.kt is part of kotlin-fuzzy
 * Last modified on 09-08-2023 05:20 p.m.
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

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.NANOSECONDS)
class EditMeasureBenchmark {
    val s1: String = "mysmilarstring"
    val s2: String = "myawfullysimilarstirng"
    val damerauLevenshtein = DamerauLevenshtein()
    val levenshtein = Levenshtein()
    val optimalStringAlignment = OptimalStringAlignment()

    @Benchmark
    fun testDamerauLevenshtein(blackhole: Blackhole) {
        blackhole.consume(damerauLevenshtein.distance(s1, s2))
    }

    @Benchmark
    fun testLCS(blackhole: Blackhole) {
        blackhole.consume(LCS.distance(s1, s2))
    }

    @Benchmark
    fun testLevenshtein(blackhole: Blackhole) {
        blackhole.consume(levenshtein.distance(s1, s2))
    }

    @Benchmark
    fun testOptimalStringAlignment(blackhole: Blackhole) {
        blackhole.consume(optimalStringAlignment.distance(s1, s2))
    }
}
