/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NormalizedWeightedLevenshtein.kt is part of kotlin-fuzzy
 * Last modified on 31-07-2023 06:14 p.m.
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

import ca.solostudios.stringsimilarity.annotations.ExperimentalStringMeasurement
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance
import kotlin.math.max

/**
 * Normalized implementation of Levenshtein that allows to define different weights for
 * different character substitutions.
 *
 * Instantiate with provided character substitution, insertion, and
 * deletion weights.
 *
 * Marked as experimental, as weights can be provided in a way that break the normalized string distance interface.
 *
 * @param charSubstitutionWeight The strategy to determine character substitution weights.
 * @param charInsertionDeletionWeight The strategy to determine character insertion/deletion weights.
 * @author Thibault Debatty, solonovamax
 */
@ExperimentalStringMeasurement
public class NormalizedWeightedLevenshtein(
    charSubstitutionWeight: (Char, Char) -> Double,
    charInsertionDeletionWeight: (Char) -> WeightedLevenshtein.Weights = { WeightedLevenshtein.Weights(1.0, 1.0) },
) : NormalizedStringDistance {
    private val weightedLevenshtein: WeightedLevenshtein = WeightedLevenshtein(charSubstitutionWeight, charInsertionDeletionWeight)

    /**
     * Compute distance as WeightedLevenshtein(s1, s2) / max(|s1|, |s2|).
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return If the weights always return a value between 1 and 0, then the resulting value will be between those.
     *         The resulting value can be as *high as* the highest weight provided.
     *         Will always be >= 0.
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty() || s2.isEmpty())
            return 1.0

        return weightedLevenshtein.distance(s1, s2) / max(s1.length, s2.length)
    }
}
