package ca.solostudios.fuzzykt

import ca.solostudios.stringsimilarity.NormalizedLevenshtein

public object FuzzyKt {
    private val normalizedLevenshtein = NormalizedLevenshtein()
    
    public fun ratio(s1: String, s2: String): Double {
        return normalizedLevenshtein.similarity(s1, s2)
    }
    
    /**
     * Returns the ratio between the smallest string and the 
     *
     * @param s1
     * @param s2
     * @return
     */
    public fun partialRatio(s1: String, s2: String): Double {
        
    }
}