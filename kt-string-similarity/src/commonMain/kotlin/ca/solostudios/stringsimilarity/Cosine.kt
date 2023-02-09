/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file Cosine.kt is part of kt-fuzzy
 * Last modified on 09-02-2023 12:55 p.m.
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
package ca.solostudios.stringsimilarity

import ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity
import kotlin.math.sqrt

/**
 * Implements Cosine Similarity between strings. The strings are first
 * transformed in vectors of occurrences of k-shingles (sequences of k
 * characters). In this n-dimensional space, the similarity between the two
 * strings is the cosine of their respective vectors.
 *
 * The similarity between the two strings is the cosine of the angle between
 * these two vectors representation. It is computed as V1 . V2 / (|V1| * |V2|)
 * The cosine distance is computed as 1 - cosine similarity.
 *
 * @author Thibault Debatty
 */
public class Cosine(k: Int = DEFAULT_K) : ShingleBased(k),
                                          NormalizedStringDistance,
                                          NormalizedStringSimilarity {
    
    /**
     * Compute the cosine similarity between strings.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The cosine similarity in the range [0, 1]
     * @throws NullPointerException if s1 or s2 is null.
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
        return (dotProduct(profile1, profile2) / (norm(profile1) * norm(profile2)))
    }
    
    /**
     * Return 1.0 - similarity.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1.0 - the cosine similarity in the range [0, 1]
     * @throws NullPointerException if s1 or s2 is null.
     */
    override fun distance(s1: String, s2: String): Double {
        return 1.0 - similarity(s1, s2)
    }
    
    /**
     * Compute similarity between precomputed profiles.
     *
     * @param profile1
     * @param profile2
     * @return
     */
    public fun similarity(
            profile1: Map<String, Int>,
            profile2: Map<String, Int>
                         ): Double {
        return (dotProduct(profile1, profile2)
                / (norm(profile1) * norm(profile2)))
    }
    
    public companion object {
        /**
         * Compute the norm L2 : sqrt(Sum_i( v_i²)).
         *
         * @param profile
         * @return L2 norm
         */
        private inline fun norm(profile: Map<String, Int>): Double {
            var agg = 0.0
            for ((_, value) in profile) {
                agg += 1.0 * value * value
            }
            return sqrt(agg)
        }
        
        private inline fun dotProduct(
                profile1: Map<String, Int>,
                profile2: Map<String, Int>
                                     ): Double {
    
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