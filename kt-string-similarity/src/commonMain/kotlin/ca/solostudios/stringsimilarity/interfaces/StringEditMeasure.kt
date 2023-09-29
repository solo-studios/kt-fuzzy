/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file StringEditMeasure.kt is part of kotlin-fuzzy
 * Last modified on 01-08-2023 11:33 p.m.
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
 * String edit measure returns a similarity or distance,
 * relative to the number of edits that must be performed to a string.
 *
 * The similarity between strings \(X\) and \(Y\) is:
 * \(\frac{w_d \lvert X \rvert + w_i \lvert Y \rvert - distance(X, Y)}{2}\).
 *
 * @see MetricStringDistance
 * @see StringSimilarity
 * @see StringDistance
 *
 * @author solonovamax
 */
public interface StringEditMeasure : MetricStringDistance, StringSimilarity, StringDistance {
    /**
     * The weight of an insertion. Represented as \(w_i\).
     *
     * Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
     *
     * [DEFAULT_WEIGHT] should be used as a default.
     */
    public val insertionWeight: Double

    /**
     * The weight of a deletion. Represented as \(w_d\).
     *
     * Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
     *
     * [DEFAULT_WEIGHT] should be used as a default.
     */
    public val deletionWeight: Double

    /**
     * The weight of a substitution. Represented as \(w_s\).
     *
     * Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
     *
     * [DEFAULT_WEIGHT] should be used as a default.
     */
    public val substitutionWeight: Double

    /**
     * The weight of a transposition. Represented as \(w_t\).
     *
     * Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
     *
     * [DEFAULT_WEIGHT] should be used as a default.
     */
    public val transpositionWeight: Double

    public companion object {
        /**
         * The default weight for operations.
         *
         * @see insertionWeight
         * @see deletionWeight
         * @see substitutionWeight
         * @see transpositionWeight
         */
        public const val DEFAULT_WEIGHT: Double = 1.0

        /**
         * The maximum weight that should reasonably be used.
         * If there is no maximum, then it can sometimes return
         * [Double.POSITIVE_INFINITY], which is very unwanted.
         */
        public const val MAX_REASONABLE_WEIGHT: Double = 1E10
    }
}
