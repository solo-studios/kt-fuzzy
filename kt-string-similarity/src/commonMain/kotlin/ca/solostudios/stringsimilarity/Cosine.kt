/*
 * kotlin-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file Cosine.kt is part of kotlin-fuzzy
 * Last modified on 16-07-2023 05:01 p.m.
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
 * KOTLIN-FUZZY IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ca.solostudios.stringsimilarity

import ca.solostudios.stringsimilarity.annotations.ExperimentalSimilarity
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity
import kotlin.math.sqrt

/**
 * Implements Soft Cosine Similarity between strings. The strings are first
 * transformed in vectors of occurrences of k-shingles (sequences of k
 * characters). In this n-dimensional space, the similarity between the two
 * strings is the cosine of their respective vectors.
 *
 * The similarity between the two strings is the cosine of the angle between
 * these two vectors representation. It is computed as
 * \(\frac{\vec{v_1} \cdot \vec{v_2}}{\lVert\vec{v_1}\rVert \times \lVert\vec{v_2}\rVert}\).
 *
 * The cosine distance is computed as \(1 - \text{cosine similarity}\).
 *
 * **This class is currently marked as experimental, as I believe it is broken.
 * Further testing is required.**
 *
 * @author Thibault Debatty, solonovamax
 * @see NormalizedStringDistance
 * @see NormalizedStringSimilarity
 */
@ExperimentalSimilarity
public class Cosine(k: Int = DEFAULT_K) : ShingleBased(k),
                                          NormalizedStringDistance,
                                          NormalizedStringSimilarity {
    /**
     * Computes the cosine similarity of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized cosine similarity.
     * @see NormalizedStringSimilarity
     */
    override fun similarity(s1: String, s2: String): Double {
        if (s1 == s2) {
            return 1.0
        }
        if (s1.length < k || s2.length < k) {
            return 0.0
        }
        val profile1 = profile(s1)
        val profile2 = profile(s2)
        return (dotProduct(profile1, profile2) / (norm(profile1) * norm(profile2))).coerceIn(0.0, 1.0)
    }

    /**
     * Computes the cosine distance of two strings.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return The normalized cosine distance.
     * @see NormalizedStringDistance
     */
    override fun distance(s1: String, s2: String): Double {
        return 1.0 - similarity(s1, s2)
    }

    /**
     * Computes the cosine similarity of precomputed profiles.
     *
     * @param profile1 The profile of the first string.
     * @param profile2 The profile of the second string.
     * @return The normalized cosine similarity.
     * @see NormalizedStringSimilarity
     */
    public fun similarity(profile1: Map<String, Int>, profile2: Map<String, Int>): Double {
        return (dotProduct(profile1, profile2) / (norm(profile1) * norm(profile2)))
    }

    /**
     * Computes the cosine distance of precomputed profiles.
     *
     * @param profile1 The profile of the first string.
     * @param profile2 The profile of the second string.
     * @return The normalized cosine distance.
     * @see NormalizedStringDistance
     */
    public fun distance(profile1: Map<String, Int>, profile2: Map<String, Int>): Double {
        return 1.0 - similarity(profile1, profile2)
    }

    private companion object {
        /**
         * Computes the \(L^2\) norm.
         *
         * \(L^2 \coloneqq \lVert\vec{v}\rVert_2 = \sqrt{\sum_{i=1}^{n} (v_i^2)}\)
         *
         * @param profile
         * @return The \(L^2) norm
         */
        inline fun norm(profile: Map<String, Int>): Double {
            var agg = 0.0
            for ((_, value) in profile) {
                agg += value * value
            }
            return sqrt(agg)
        }

        inline fun dotProduct(profile1: Map<String, Int>, profile2: Map<String, Int>): Double {
            // Loop over the smallest map
            val smallProfile = minOf(profile1, profile2, compareBy { it.size })
            val largeProfile = maxOf(profile1, profile2, compareBy { it.size })
            var agg = 0.0
            for ((key, value) in smallProfile) {
                val i = largeProfile[key] ?: continue
                agg += 1.0 * value * i
            }
            return agg
        }
    }
}
