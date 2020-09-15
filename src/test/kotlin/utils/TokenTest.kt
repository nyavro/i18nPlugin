package utils

import com.eny.i18n.plugin.key.lexer.Literal
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TokenTest {
    @Test
    fun mergeTwoLiterals() {
        val a = Literal("first", 5)
        val b = Literal("Second", 6)
        val ab = a.merge(b)
        assertEquals("firstSecond", ab.text)
        assertEquals(11, ab.length)
    }
}