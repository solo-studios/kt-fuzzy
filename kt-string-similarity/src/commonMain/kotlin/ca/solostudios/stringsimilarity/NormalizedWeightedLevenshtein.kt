package ca.solostudios.stringsimilarity

import ca.solostudios.stringsimilarity.interfaces.NormalizedStringDistance
import kotlin.math.max

public class NormalizedWeightedLevenshtein(
        charSubstitutionWeight: (Char, Char) -> Double,
        charInsertionDeletionWeight: (Char) -> WeightedLevenshtein.Weights = { WeightedLevenshtein.Weights(1.0, 1.0) },
                                          ) : NormalizedStringDistance {
    private val weightedLevenshtein: WeightedLevenshtein = WeightedLevenshtein(charSubstitutionWeight, charInsertionDeletionWeight)
    
    /**
     * Compute distance as WeightedLevenshtein(s1, s2) / max(|s1|, |s2|).
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return If the weights always return a value between 1 and 0, then the resulting value will be between those.
     *         The resulting value can be as *high as* the highest weight provided.
     *         Will always be >= 0.
     */
    override fun distance(s1: String, s2: String): Double {
        if (s1 == s2) {
            return 0.0
        }
        
        val maxLen: Int = max(s1.length, s2.length)
        
        
        return if (maxLen == 0) 0.0 else weightedLevenshtein.distance(s1, s2) / maxLen
        // the python library "thefuzz" (renamed from "fuzzywuzzy" uses the sum of the lengths, rather than the max.
        // val lenSum = s1.length + s2.length
        // return if (lenSum == 0) 0.0 else (weightedLevenshtein.distance(s1, s2)) / lenSum
    }
}