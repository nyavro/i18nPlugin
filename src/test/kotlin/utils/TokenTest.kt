package utils

import com.eny.i18n.plugin.utils.Literal
import org.junit.Test
import kotlin.test.assertEquals

class TokenTest {
    @Test
    fun mergeTwoLiterals() {
        val a = Literal("first", 5, 0)
        val b = Literal("Second", 6, 0)
        val ab = a.merge(b)
        assertEquals("firstSecond", ab.text)
        assertEquals(11, ab.length)
    }
}