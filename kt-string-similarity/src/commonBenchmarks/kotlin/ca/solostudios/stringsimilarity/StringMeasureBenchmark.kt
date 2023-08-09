/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file StringMeasureBenchmark.kt is part of kotlin-fuzzy
 * Last modified on 09-08-2023 07:46 p.m.
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

import ca.solostudios.stringsimilarity.annotations.ExperimentalStringMeasure
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
@OptIn(ExperimentalStringMeasure::class)
class StringMeasureBenchmark {
    val s1: String = "mysmilarstring"
    val s2: String = "myawfullysimilarstirng"
    val cosine = Cosine()
    val jaccard = Jaccard()
    val jaroWinkler = JaroWinkler()
    val nGram = NGram()
    val qGram = QGram()
    val ratcliffObershelp = RatcliffObershelp()
    val sift4 = Sift4()

    @Benchmark
    fun testCosine(blackhole: Blackhole) {
        blackhole.consume(cosine.similarity(s1, s2))
    }

    @Benchmark
    fun testJaccard(blackhole: Blackhole) {
        blackhole.consume(jaccard.similarity(s1, s2))
    }

    @Benchmark
    fun testJaroWinkler(blackhole: Blackhole) {
        blackhole.consume(jaroWinkler.similarity(s1, s2))
    }

    @Benchmark
    fun testNGram(blackhole: Blackhole) {
        blackhole.consume(nGram.distance(s1, s2))
    }

    @Benchmark
    fun testQGram(blackhole: Blackhole) {
        blackhole.consume(qGram.distance(s1, s2))
    }

    @Benchmark
    fun testRatcliffObershelp(blackhole: Blackhole) {
        blackhole.consume(ratcliffObershelp.similarity(s1, s2))
    }

    @Benchmark
    fun testSift4(blackhole: Blackhole) {
        blackhole.consume(sift4.distance(s1, s2))
    }
}
