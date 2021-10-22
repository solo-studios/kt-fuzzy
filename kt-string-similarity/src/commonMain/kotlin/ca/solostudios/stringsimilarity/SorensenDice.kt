/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015 Thibault Debatty
 *
 * The file SorensenDice.kt is part of kt-fuzzy
 * Last modified on 22-10-2021 05:46 p.m.
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

/**
 * Sorensen-Dice coefficient, aka Sørensen index, Dice's coefficient or
 * Czekanowski's binary (non-quantitative) index.
 *
 * The strings are first converted to boolean sets of k-shingles (sequences
 * of k characters), then the similarity is computed as 2 * |A inter B| /
 * (|A| + |B|). Attention: Sorensen-Dice distance (and similarity) does not
 * satisfy triangle inequality.
 *
 * Similar to Jaccard index, but this time the similarity is computed as 2 * |V1
 * inter V2| / (|V1| + |V2|). Distance is computed as 1 - cosine similarity.
 *
 * @author Thibault Debatty
 */
public class SorensenDice(k: Int = DEFAULT_K) : ShingleBased(k),
                                                NormalizedStringDistance,
                                                NormalizedStringSimilarity {
    /**
     * Similarity is computed as 2 * |A inter B| / (|A| + |B|).
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return The computed Sorensen-Dice similarity.
     */
    override fun similarity(s1: String, s2: String): Double {
        if (s1 == s2) {
            return 1.0
        }
        val profile1: Map<String, Int> = getProfile(s1)
        val profile2: Map<String, Int> = getProfile(s2)
        val union: MutableSet<String> = HashSet()
        union.addAll(profile1.keys)
        union.addAll(profile2.keys)
        var inter = 0
        for (key in union) {
            if (profile1.containsKey(key) && profile2.containsKey(key)) {
                inter++
            }
        }
        return 2.0 * inter / (profile1.size + profile2.size)
    }
    
    /**
     * Returns 1 - similarity.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return 1.0 - the computed similarity
     * @throws NullPointerException if s1 or s2 is null.
     */
    override fun distance(s1: String, s2: String): Double {
        return 1 - similarity(s1, s2)
    }
}