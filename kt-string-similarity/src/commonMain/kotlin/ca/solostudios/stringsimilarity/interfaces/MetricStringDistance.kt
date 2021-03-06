/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file MetricStringDistance.kt is part of kt-fuzzy
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

// TODO: 2021-10-22 Explain this better 
/**
 * String distances that implement this interface are metrics.
 * This means:
 * - `d(x, y) >= 0` (non-negativity, or separation axiom)
 *    * The result can never be negative.
 * - `d(x, y) = 0` if and only if x = y (identity, or coincidence axiom)
 *    * If `x` and `y` are the same, the result must be `0`.
 * - `d(x, y) = d(y, x)` (symmetry)
 *    * The distance between `x` and `y` must be equal to the distance between `y` and `x`. (Independent of order)
 * - `d(x, z) <= d(x, y) + d(y, z)` (triangle inequality).
 *    * The points `x`, `y`, and `z` can be rendered as a triangle in 3d space.
 *      Therefore, the sum of the length of any side (eg. `d(x, z)`) must be
 *      equal to or less than the sum of the lengths of the other two sides.
 *      (eg. `d(x, y) + d(y, z)`)
 *
 * @author Thibault Debatty
 */
public interface MetricStringDistance : StringDistance {
    /**
     * Compute and return the metric distance.
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The distance between the strings.
     */
    override fun distance(s1: String, s2: String): Double
}