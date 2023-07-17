/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NormalizedStringSimilarity.kt is part of kotlin-fuzzy
 * Last modified on 17-07-2023 03:39 p.m.
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
package ca.solostudios.stringsimilarity.interfaces

/**
 * Normalized string similarities return a normalized similarity between two strings.
 *
 * The returned distance is always in the range \(&#91;0, 1]\).
 * - `0` indicates that neither string have anything in common.
 * - `1` indicates that both strings are equivalent. Equivalent strings are not necessarily identical.
 * - If two strings are identical, then it should always return `1`.
 *
 * As stated in
 * [Computation of Normalized Edit Distance and Applications](https://www.csie.ntu.edu.tw/~b93076/Computation%20of%20Normalized%20Edit%20Distance%20and%20Applications.pdf)
 * <sup>[&#91;archive.org&#93;](https://web.archive.org/web/20220303061601/https://www.csie.ntu.edu.tw/~b93076/Computation%20of%20Normalized%20Edit%20Distance%20and%20Applications.pdf)</sup>,
 *
 * > Given two strings \(x\) and \(y\) over a finite alphabet,
 * > the normalized edit distance between \(x\) and \(y\), \(d(x,y)\)
 * > is defined as the minimum of \(W(p)/L(p)\),
 * > here \(p\) is an editing path between \(x\) and \(y\), \(W(p)\)
 * > is the sum of the weights of the elementary edit operations of \(p\),
 * > and \(L(p)\) is the number of these operations (length of \(p\)).
 *
 * @author Thibault Debatty, solonovamax
 */
public interface NormalizedStringSimilarity : StringSimilarity {
    /**
     * Computes the similarity of two strings.
     * The similarity will be normalized using the number of operations that are performed.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized similarity.
     * @see NormalizedStringSimilarity
     */
    override fun similarity(s1: String, s2: String): Double
}
