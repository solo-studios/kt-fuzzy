/*
 * kt-string-similarity - A library implementing different string similarity and distance measures.
 * Copyright (c) 2015-2015 Thibault Debatty
 *
 * The file DefaultStringSimilarities.kt is part of kt-fuzzy
 * Last modified on 22-10-2021 06:05 p.m.
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

import ca.solostudios.stringsimilarity.annotations.ExperimentalSimilarity

public object StringMatcher {
    public val cosine: Cosine = Cosine()
    
    public val damerau: Damerau = Damerau()
    
    public val jaccard: Jaccard = Jaccard()
    
    public val jaroWinkler: JaroWinkler = JaroWinkler()
    
    public val levenshtein: Levenshtein = Levenshtein()
    
    public val longestCommonSubsequence: LongestCommonSubsequence = LongestCommonSubsequence()
    
    public val metricLCS: MetricLCS = MetricLCS()
    
    public val nGram: NGram = NGram()
    
    public val normalizedLevenshtein: NormalizedLevenshtein = NormalizedLevenshtein()
    
    public val optimalStringAlignment: OptimalStringAlignment = OptimalStringAlignment()
    
    public val qGram: QGram = QGram()
    
    public val ratcliffObershelp: RatcliffObershelp = RatcliffObershelp()
    
    @ExperimentalSimilarity
    public val sift4: Sift4 = Sift4()
    
    public val sorensenDice: SorensenDice = SorensenDice()
    
    public val weightedLevenshtein: WeightedLevenshtein = WeightedLevenshtein({ _, _ -> 1.0 })
}