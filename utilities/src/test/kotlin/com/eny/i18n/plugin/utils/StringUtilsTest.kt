package com.eny.i18n.plugin.utils

import org.junit.Assert.*
import org.junit.Test

class StringUtilsTest {

    @Test
    fun testMatchPattern() {
        val str = "startsWithEndsWithElse"
        val notMatch = "doesNotstartWithEndsWithElse2"
        val regex = Regex("starts.*")
        assertTrue(str.matches(regex))
        assertFalse(notMatch.matches(regex))
    }

    @Test
    fun testMatchPattern2() {
        val str = "startsWith1EndsWithElse"
        val notMatch = "doesNotstartWithEndsWithElse3"
        val regex = Regex(".*Else")
        assertTrue(str.matches(regex))
        assertFalse(notMatch.matches(regex))
    }

    @Test
    fun testIsQuoted() {
        listOf("'", "`", "\"").forEach {
            assertFalse("${it}sub".isQuoted())
            assertFalse("sub${it}'".isQuoted())
            assertTrue("${it}subr${it}".isQuoted())
        }
        assertFalse("u".isQuoted())
    }

    @Test
    fun tetUnQuote() {
        listOf("'", "`", "\"").forEach {
            val left = "${it}subs"
            assertEquals(left.unQuote(), left)
            val right = "subs${it}"
            assertEquals(right.unQuote(), right)
            val wrapped = "${it}subs${it}"
            assertEquals(wrapped.unQuote(), "subs")
        }
    }

    @Test
    fun testDoubleUnQuote() {
        listOf("'", "`", "\"").forEach {
            val wrapped = "\"${it}'`subs`'${it}\""
            assertEquals(wrapped.unQuote(), "${it}'`subs`'${it}")
        }
    }
}
