/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file LCS.kt is part of kotlin-fuzzy
 * Last modified on 19-10-2023 04:33 p.m.
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

import ca.solostudios.stringsimilarity.interfaces.MetricStringDistance
import ca.solostudios.stringsimilarity.interfaces.StringDistance
import ca.solostudios.stringsimilarity.interfaces.StringSimilarity
import ca.solostudios.stringsimilarity.util.minMaxByLength
import kotlin.math.max


/**
 * Implements the Longest Common Subsequence (LCS) problem consists in finding the longest
 * subsequence common to two (or more) sequences. It differs from problems of
 * finding common substrings: unlike substrings, subsequences are not required
 * to occupy consecutive positions within the original sequences.
 *
 * It is used by the diff utility, by Git for reconciling multiple changes, etc.
 *
 * The LCS edit distance between strings \(X\) and \(Y\) is:
 * \(\lvert X \rvert + \lvert Y \rvert - 2 \times \lvert LCS(X, Y) \rvert\)
 * - \(\text{min} = 0\)
 * - \(\text{max} = \lvert X \rvert + \lvert Y \rvert\).
 *
 * LCS distance is equivalent to Levenshtein distance, when only insertion and
 * deletion is allowed (no substitution), or when the cost of the substitution
 * is the double of the cost of an insertion or deletion.
 *
 * @see MetricStringDistance
 * @see StringDistance
 * @see StringSimilarity
 *
 * @author solonovamax
 */
public object LCS : MetricStringDistance, StringSimilarity, StringDistance {
    override fun distance(s1: String, s2: String): Double {
        return s1.length + s2.length - (similarity(s1, s2)) * 2
    }

    override fun similarity(s1: String, s2: String): Double {
        if (s1 == s2)
            return s1.length.toDouble()
        if (s1.isEmpty())
            return 0.0
        if (s2.isEmpty())
            return 0.0

        val (shorter, longer) = minMaxByLength(s1, s2)

        val previous = DoubleArray(shorter.length + 1)
        val current = DoubleArray(shorter.length + 1)

        longer.forEachIndexed { _, c1 ->
            shorter.forEachIndexed { j, c2 ->
                current[j + 1] = if (c1 == c2)
                    previous[j] + 1
                else
                    max(current[j], previous[j + 1])
            }
            current.copyInto(previous)
        }

        return current.last()
    }
}
