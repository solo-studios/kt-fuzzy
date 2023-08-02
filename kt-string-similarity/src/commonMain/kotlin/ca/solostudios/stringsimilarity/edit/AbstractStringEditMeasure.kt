/*
 * kt-fuzzy - A Kotlin library for fuzzy string matching
 * Copyright (c) 2023-2023 solonovamax <solonovamax@12oclockpoint.com>
 *
 * The file AbstractStringEditMeasure.kt is part of kotlin-fuzzy
 * Last modified on 02-08-2023 12:19 a.m.
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

import ca.solostudios.stringsimilarity.interfaces.StringEditMeasure
import ca.solostudios.stringsimilarity.util.minMaxByLength

/**
 * Class that abstracts common logic for edit measures.
 *
 * @author solonovamax
 */
public abstract class AbstractStringEditMeasure(
    public final override val insertionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    public final override val deletionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    public final override val substitutionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
    public final override val transpositionWeight: Double = StringEditMeasure.DEFAULT_WEIGHT,
) : StringEditMeasure {
    init {
        require(insertionWeight > 0 && insertionWeight < StringEditMeasure.MAX_REASONABLE_WEIGHT) {
            "Insertion weight ($insertionWeight) should be between 0 and 1*10^10"
        }
        require(deletionWeight > 0 && deletionWeight < StringEditMeasure.MAX_REASONABLE_WEIGHT) {
            "Deletion weight ($deletionWeight) should be between 0 and 1*10^10"
        }
        require(substitutionWeight > 0 && substitutionWeight < StringEditMeasure.MAX_REASONABLE_WEIGHT) {
            "Substitution weight ($substitutionWeight) should be between 0 and 1*10^10"
        }
        require(transpositionWeight > 0 && transpositionWeight < StringEditMeasure.MAX_REASONABLE_WEIGHT) {
            "Transposition weight ($transpositionWeight) should be between 0 and 1*10^10"
        }
    }

    final override fun distance(s1: String, s2: String): Double {
        if (s1 == s2)
            return 0.0
        if (s1.isEmpty())
            return s2.length * insertionWeight
        if (s2.isEmpty())
            return s1.length * deletionWeight

        val (shorter, longer) = minMaxByLength(s1, s2)

        val costMatrix = initializeCostMatrix(shorter, longer)

        fillCostMatrix(costMatrix, shorter, longer)

        return costMatrix.last().last()
    }

    final override fun similarity(s1: String, s2: String): Double {
        return (doubleMaximum(s1, s2) - distance(s1, s2)) / 2
    }

    protected open fun initializeCostMatrix(shorter: String, longer: String): Array<DoubleArray> {
        val costMatrix = Array(longer.length + 1) { DoubleArray(shorter.length + 1) }

        // initialize the left and top edges of H
        for (i in 0..longer.length) {
            costMatrix[i][0] = i * deletionWeight
        }
        for (j in 0..shorter.length) {
            costMatrix[0][j] = j * insertionWeight
        }
        return costMatrix
    }

    protected abstract fun fillCostMatrix(costMatrix: Array<DoubleArray>, shorter: String, longer: String)

    protected open fun doubleMaximum(s1: String, s2: String): Double {
        return insertionWeight * s1.length + deletionWeight * s2.length
    }
}
