package com.eny.i18n.plugin.key

import com.eny.i18n.plugin.key.lexer.Literal
import org.junit.Assert.assertEquals
import org.junit.Test

internal class TokenTest {

    @Test
    fun testMergeTwoLiterals() {
        val a = Literal("first", 5)
        val b = Literal("Second", 6)
        val ab = a.merge(b)
        assertEquals("firstSecond", ab.text)
        assertEquals(11, ab.length)
    }
}
