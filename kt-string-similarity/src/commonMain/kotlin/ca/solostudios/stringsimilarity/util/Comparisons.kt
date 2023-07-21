/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Comparisons.kt is part of kotlin-fuzzy
 * Last modified on 21-07-2023 04:37 p.m.
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

package ca.solostudios.stringsimilarity.util

import kotlin.math.max
import kotlin.math.min

/**
 * Returns the min and the max of two values according to the order specified by the given [comparator].
 *
 * If values are equal, they are returned in the order provided.
 *
 * @return A pair of min-max values.
 */
internal fun <T> minMaxOf(a: T, b: T, comparator: Comparator<in T>): Pair<T, T> {
    return if (comparator.compare(a, b) <= 0) a to b else b to a
}

/**
 * Returns the shortest and the longest of two strings.
 *
 * If values are equal, they are returned in the order provided.
 *
 * @return A pair of shortest-longest strings.
 */
internal fun minMaxByLength(a: String, b: String): Pair<String, String> {
    return if (a.length <= b.length) a to b else b to a
}

/**
 * Returns the length of the longest string.
 *
 * @return The length of the longest string.
 */
internal fun maxLength(a: String, b: String): Int {
    return max(a.length, b.length)
}

/**
 * Returns the length of the longest string.
 *
 * @return The length of the longest string.
 */
internal fun minLength(a: String, b: String): Int {
    return min(a.length, b.length)
}
