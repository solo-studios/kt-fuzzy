/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2015-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file ShingleBased.kt is part of kotlin-fuzzy
 * Last modified on 08-07-2023 06:34 p.m.
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

/**
 * Abstract class for string similarities that rely on set operations (like
 * cosine similarity or jaccard index).
 *
 * k-shingling is the operation of transforming a string (or text document) into
 * a set of n-grams, which can be used to measure the similarity between two
 * strings or documents.
 *
 * Generally speaking, a k-gram is any sequence of k tokens. We use here the
 * definition from Leskovec, Rajaraman & Ullman (2014), "Mining of Massive
 * Datasets", Cambridge University Press: Multiple subsequent spaces are
 * replaced by a single space, and a k-gram is a sequence of k characters.
 *
 * Default value of [k] is `3`. A good rule of thumb is to imagine that there are
 * only 20 characters and estimate the number of k-shingles as \(20^k\). For small
 * documents like e-mails, \(k = 5\) is a recommended value. For large documents,
 * such as research articles, \(k = 9\) is considered a safe choice.
 * @param k The length of k-shingles.
 * @throws IllegalArgumentException if \(k \leqslant 0\)
 * @author Thibault Debatty
 */
public abstract class ShingleBased(public val k: Int = DEFAULT_K) {
    /**
     * Compute and return the profile of s, as defined by Ukkonen "Approximate
     * string-matching with q-grams and maximal matches".
     * https://www.cs.helsinki.fi/u/ukkonen/TCS92.pdf The profile is the number
     * of occurrences of k-shingles, and is used to compute q-gram similarity,
     * Jaccard index, etc. Pay attention: the memory requirement of the profile
     * can be up to k * size of the string
     *
     * @param string
     * @return the profile of this string, as an unmodifiable Map
     */
    public fun profile(string: String): Map<String, Int> {
        val shingles = mutableMapOf<String, Int>()
        val stringNoSpace: String = SPACE_REG.replace(string, " ")

        for (i in 0 until stringNoSpace.length - k + 1) {
            val shingle = stringNoSpace.substring(i, i + k)
            val old = shingles[shingle]
            if (old != null) {
                shingles[shingle] = old + 1
            } else {
                shingles[shingle] = 1
            }
        }
        return shingles
    }

    public companion object {
        public const val DEFAULT_K: Int = 3

        /**
         * Pattern for finding multiple following spaces.
         */
        private val SPACE_REG: Regex = Regex("\\s+")
    }

    init {
        require(k > 0) { "k must be positive!" }
    }
}
