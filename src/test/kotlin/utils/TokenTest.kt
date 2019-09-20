package utils

import com.eny.i18n.plugin.utils.Literal
import com.eny.i18n.plugin.utils.TemplateExpression
import org.junit.Test
import kotlin.test.assertEquals

class TokenTest {
    @Test
    fun mergeTwoLiterals() {
        val a = Literal("first", 5, 0)
        val b = Literal("Second", 6, 0)
        val ab = a.merge(b)
        assertEquals("firstSecond", ab.text())
        assertEquals(11, ab.textLength())
    }

    @Test
    fun mergeLiteralWithTemplate() {
        val a = Literal("first", 5, 0)
        val b = TemplateExpression("\${v}", listOf(Literal("Second", 6, 0), Literal("Third", 5, 0))).resolved.get(0)
        val ab = a.merge(b)
        assertEquals("firstSecond", ab.text())
        assertEquals(9, ab.textLength())
    }
}