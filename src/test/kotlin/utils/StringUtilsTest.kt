package utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

//@Ignore
class StringUtilsTest : TestBase {

    @Test
    fun matchPattern() {
        val str = "startsWithEndsWithElse"
        val notMatch = "doesNotstartWithEndsWithElse2"
        val regex = Regex("starts.*")
        assertTrue(str.matches(regex))
        assertFalse(notMatch.matches(regex))
    }

    @Test
    fun matchPattern2() {
        val str = "startsWith1EndsWithElse"
        val notMatch = "doesNotstartWithEndsWithElse3"
        val regex = Regex(".*Else")
        assertTrue(str.matches(regex))
        assertFalse(notMatch.matches(regex))
    }
}