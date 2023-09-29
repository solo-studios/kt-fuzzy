/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file AbstractNormalizedStringEditMeasure.kt is part of kotlin-fuzzy
 * Last modified on 09-08-2023 12:51 a.m.
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

package ca.solostudios.stringsimilarity.edit

import ca.solostudios.stringsimilarity.interfaces.NormalizedStringEditMeasure
import ca.solostudios.stringsimilarity.interfaces.StringEditMeasure

/**
 * Class that abstracts common logic for edit measures.
 *
 * @author solonovamax
 */
public abstract class AbstractNormalizedStringEditMeasure(
    public val editMeasure: StringEditMeasure,
) : NormalizedStringEditMeasure {
    public final override val insertionWeight: Double by editMeasure::insertionWeight
    public final override val deletionWeight: Double by editMeasure::deletionWeight
    public final override val substitutionWeight: Double by editMeasure::substitutionWeight
    public final override val transpositionWeight: Double by editMeasure::transpositionWeight

    final override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty() || s2.isEmpty())
            return 1.0

        val distance = editMeasure.distance(s1, s2)
        return (2 * distance) / (s1.length + s2.length + distance)
    }

    final override fun similarity(s1: String, s2: String): Double {
        return 1.0 - distance(s1, s2)
    }
}
