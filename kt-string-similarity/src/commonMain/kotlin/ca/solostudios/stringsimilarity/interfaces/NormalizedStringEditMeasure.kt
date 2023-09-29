/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NormalizedStringEditMeasure.kt is part of kotlin-fuzzy
 * Last modified on 01-08-2023 11:41 p.m.
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
 * Normalized string edit measure returns a similarity or distance,
 * relative to the number of edits that must be performed to a string,
 * which is then normalized according to the function.
 * It is normalized according to "A normalized levenshtein distance metric."
 * (Yujian & Bo, 2007)
 *
 * The normalized edit distance between strings \(X\) and \(Y\) is:
 * \(\frac{2 \times distance(X, Y)}{w_d \lvert X \rvert + w_i \lvert Y \rvert + distance(X, Y)}\).
 *
 * #### References
 * Yujian, L., & Bo, L. (2007-06). A normalized levenshtein distance metric.
 * IEEE Transactions on Pattern Analysis and Machine Intelligence, 29(6),
 * 1091-1095.
 * <https://doi.org/10.1109/tpami.2007.1078><sup>[&#91;sci-hub&#93;](https://sci-hub.st/10.1109/tpami.2007.1078)</sup>
 *
 * @see StringEditMeasure
 * @see NormalizedStringSimilarity
 * @see NormalizedStringDistance
 *
 * @author solonovamax
 */
public interface NormalizedStringEditMeasure : StringEditMeasure, NormalizedStringDistance, NormalizedStringSimilarity {
    /**
     * Computes the edit distance metric of two strings.
     * The distance will be normalized using the number of operations that are performed.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     *
     * @return The normalized edit distance.
     *
     * @see MetricStringDistance
     * @see NormalizedStringDistance
     */
    override fun distance(s1: String, s2: String): Double

    /**
     * Computes the edit similarity of two strings.
     * The similarity will be normalized using the number of operations that are performed.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     *
     * @return The normalized edit similarity.
     *
     * @see NormalizedStringSimilarity
     */
    override fun similarity(s1: String, s2: String): Double
}
