/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file NormalizedDamerauLevenshtein.kt is part of kotlin-fuzzy
 * Last modified on 19-10-2023 05:19 p.m.
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

package ca.solostudios.stringsimilarity.normalized

import ca.solostudios.stringsimilarity.DamerauLevenshtein
import ca.solostudios.stringsimilarity.interfaces.MetricStringDistance
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance
import ca.solostudios.stringsimilarity.interfaces.NormalizedStringSimilarity
import ca.solostudios.stringsimilarity.interfaces.StringDistance
import ca.solostudios.stringsimilarity.interfaces.StringSimilarity
import ca.solostudios.stringsimilarity.util.Constants

/**
 * Implements a normalized metric based the [Damerau Levenshtein][DamerauLevenshtein]
 * distance (Yujian & Bo, 2007).
 *
 * The normalized Damerau Levenshtein distance between Strings \(X\) and \(Y\) is:
 * \(\frac{2 \times distance_{damerau levenshtein}(X, Y)}{w_d \lvert X \rvert + w_i \lvert Y \rvert + distance_{damerau levenshtein}(X, Y)}\).
 *
 * The similarity is computed as
 * \(1 - distance(X, Y)\).
 *
 * #### References
 * Yujian, L., & Bo, L. (2007-06). A normalized levenshtein distance metric.
 * IEEE Transactions on Pattern Analysis and Machine Intelligence, 29(6),
 * 1091-1095.
 * <https://doi.org/10.1109/tpami.2007.1078><sup>[&#91;sci-hub&#93;](https://sci-hub.st/10.1109/tpami.2007.1078)</sup>
 *
 * @param insertionWeight The weight of an insertion. Represented as \(w_i\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param deletionWeight The weight of a deletion. Represented as \(w_d\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param substitutionWeight The weight of a substitution. Represented as \(w_s\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 * @param transpositionWeight The weight of a substitution. Represented as \(w_t\). Must be in the range \(&#91;0, 1 \times 10^{10} &#93;\).
 *
 * @see DamerauLevenshtein
 * @see MetricStringDistance
 * @see StringDistance
 * @see StringSimilarity
 *
 * @author solonovamax
 */
public class NormalizedDamerauLevenshtein(
    insertionWeight: Double = Constants.DEFAULT_WEIGHT,
    deletionWeight: Double = Constants.DEFAULT_WEIGHT,
    substitutionWeight: Double = Constants.DEFAULT_WEIGHT,
    transpositionWeight: Double = Constants.DEFAULT_WEIGHT,
) : MetricStringDistance, NormalizedStringDistance, NormalizedStringSimilarity {
    private val damerauLevenshtein = DamerauLevenshtein(insertionWeight, deletionWeight, substitutionWeight, transpositionWeight)

    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty() || s2.isEmpty())
            return 1.0

        val distance = damerauLevenshtein.distance(s1, s2)
        return (2 * distance) / (s1.length + s2.length + distance)
    }

    override fun similarity(s1: String, s2: String): Double {
        return 1.0 - distance(s1, s2)
    }
}
