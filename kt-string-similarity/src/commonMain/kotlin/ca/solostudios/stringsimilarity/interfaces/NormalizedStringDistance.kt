/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NormalizedStringDistance.kt is part of kotlin-fuzzy
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
 * Normalized string distances return a normalized distance between two strings.
 *
 * The returned distance is always in the range \(&#91;0, 1]\).
 * - `0` indicates that both strings are *equivalent*. Equivalent strings are not necessarily identical.
 * - `1` indicates that neither string have anything in common.
 * - If two strings are identical, then it should always return `0`.
 *
 * The normalized similarity of any normalized string measure can always be computed as is computed as
 * \(1 - distance(X, Y)\).
 *
 * As stated in "Computation of Normalized Edit Distance and Applications",
 * > Given two strings \(x\) and \(y\) over a finite alphabet,
 * > the normalized edit distance between \(x\) and \(y\), \(d(x,y)\)
 * > is defined as the minimum of \(W(p)/L(p)\),
 * > here \(p\) is an editing path between \(x\) and \(y\), \(W(p)\)
 * > is the sum of the weights of the elementary edit operations of \(p\),
 * > and \(L(p)\) is the number of these operations (length of \(p\)).
 * > (Marzal & Vidal, 1993)
 *
 * #### References
 * Marzal, A., & Vidal, E. (1993-09). Computation of normalized edit distance and
 * applications. *IEEE Transactions on Pattern Analysis and Machine Intelligence*,
 * *15*(9), 926–932.
 * <https://doi.org/10.1109/34.232078><sup>[&#91;sci-hub&#93;](https://sci-hub.st/10.1109/34.232078)</sup>
 *
 * @see NormalizedStringSimilarity
 *
 * @author Thibault Debatty, solonovamax
 */
public interface NormalizedStringDistance : StringDistance {
    /**
     * Computes the distance of two strings.
     * The distance will be normalized using the number of operations that are performed.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized distance.
     * @see NormalizedStringDistance
     */
    override fun distance(s1: String, s2: String): Double
}
