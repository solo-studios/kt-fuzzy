/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file NormalizedStringDistance.kt is part of kt-fuzzy
 * Last modified on 22-10-2021 04:48 p.m.
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
package ca.solostudios.stringsimilarity.interfaces

/**
 * Normalized string similarities return a similarity between `0` and `1`.
 *
 * As stated in [Computation of Normalized Edit Distance and Applications](https://www.csie.ntu.edu.tw/~b93076/Computation%20of%20Normalized%20Edit%20Distance%20and%20Applications.pdf),
 * > Given two strings `X` and `Y` over a finite alphabet,
 * > the normalized edit distance between `X` and `Y`, `d(X,Y)`
 * > is defined as the minimum of `W(P)/L(P)`,
 * > here `P` is an editing path between `X` and `Y`, `W(P)`
 * > is the sum of the weights of the elementary edit operations of `P`,
 * > and L(P) is the number of these operations (length of `P`).
 *
 * @author Thibault Debatty
 */
public interface NormalizedStringDistance : StringDistance {
    /**
     * Compute and return a measure of distance.
     * The distance will be normalized using the number of operations that are performed.
     *
     * @param s1 The first string
     * @param s2 The second string
     * @return The string similarity (0 means both strings are completely different)
     */
    override fun distance(s1: String, s2: String): Double
}