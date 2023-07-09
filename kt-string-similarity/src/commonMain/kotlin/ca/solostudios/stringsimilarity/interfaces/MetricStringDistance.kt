/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file MetricStringDistance.kt is part of kotlin-fuzzy
 * Last modified on 09-07-2023 03:43 p.m.
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
 * Metric string distances return a distance metric.
 *
 * A metric space is a pair \((M, d)\), where \(M\) is a set and \(d\)
 * is a function \(M \times M \to \mathbb{R}\) satisfying the following axioms:
 * 1. Symmetry Axiom:
 *    - \(d(x, y) = d(y, x)\)
 *    - The distance from \(x\) to \(y\) always be the same as the distance from \(y\) to \(x\). (Independent of order)
 * 2. Identity/Coincidence Axiom:
 *    - \(d(x, y) = 0 \iff x = y\)
 *    - If the points \(x\) and \(y\) are the same, the distance between them must be \(0\).
 * 3. Non-negativity Axiom:
 *    - \(d(x, y) \geqslant 0\)
 *    - The distance between any two points can never be negative.
 * 4. Triangle Inequality Axiom:
 *    - \(d(x, z) \leqslant d(x, y) + d(y, z)\)
 *    - The points \(x\), \(y\), and \(z\) can construct a triangle in n-dimensional space.
 *    - The points \(x\), \(y\), and \(z\) can construct a triangle in n-dimensional space.
 *      Therefore, the length of any side (eg. \(d(x, z)\)) must be
 *      equal to or less than the sum of the lengths of the other two sides.
 *      (eg. \(d(x, z) \leqslant d(x, y) + d(y, z)\))
 *    - > This is a natural property of both physical and metaphorical notions of distance:
 *      you can arrive at z from x by taking a detour through y,
 *      but this will not make your journey any faster than the shortest path.
 *
 *      [Wikipedia](https://en.wikipedia.org/wiki/Metric_space#Definition)
 *      <sup>[&#91;archive.org&#93;](https://web.archive.org/web/20230709193203/https://en.wikipedia.org/wiki/Metric_space#Definition)</sup>
 *
 * Where \(d(x, y)\) is the distance between the strings \(x\) and \(y\).
 *
 * @author Thibault Debatty, solonovamax
 */
public interface MetricStringDistance : StringDistance {
    /**
     * Compute and return the metric distance.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The distance between the strings.
     *
     * @see MetricStringDistance
     */
    override fun distance(s1: String, s2: String): Double
}
