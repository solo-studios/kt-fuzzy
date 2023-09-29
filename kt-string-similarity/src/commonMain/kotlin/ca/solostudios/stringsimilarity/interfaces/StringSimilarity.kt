/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2021-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file StringSimilarity.kt is part of kotlin-fuzzy
 * Last modified on 19-07-2023 04:07 p.m.
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
 * String similarities return a similarity between two strings.
 *
 * The returned similarity will always be \(\geqslant 0\).
 *
 * - `0` indicates that neither string have anything in common.
 * - If two strings are identical and non-empty, then it should never return `0`.
 *
 * @see StringDistance
 *
 * @author Thibault Debatty, solonovamax
 */
public interface StringSimilarity {
    /**
     * Computes the similarity of two strings.
     *
     * @param s1 The first string
     * @param s2 The second string
     * @return The similarity.
     * @see StringDistance
     */
    public fun similarity(s1: String, s2: String): Double
}
