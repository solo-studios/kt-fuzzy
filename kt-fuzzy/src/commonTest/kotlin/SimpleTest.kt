import ca.solostudios.fuzzykt.FuzzyKt
import kotlin.test.Test

class SimpleTest {
    @Test
    fun test() {
        println(FuzzyKt.ratio("this is a test", "this is a test!"))
        println(FuzzyKt.ratio("fuzzy wuzzy was a bear", "wuzzy fuzzy was a bear"))
        println(FuzzyKt.ratio("abcd", "abcd"))
        println(FuzzyKt.ratio("fuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a beara", 
                              "fuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearfuzzy wuzzy was a bearb"))
    }
}