package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.key.KeyElement
import org.junit.Test
import kotlin.test.assertNull

//@Ignore
internal class InvalidExpressionTest : ParserTestBase {

//invalid:file:literal.ROOT.Key1.Key31
    @Test
    fun testParseInvalidFilenameInLiteral() {
        val invalidExpression = listOf(
            KeyElement.literal("invalid:file:literal.ROOT.Key1.Key31")
        )     
        assertNull(parse(invalidExpression))
    }

//invalid:literal..key.ROOT.Key1.Key31
    @Test
    fun testParseInvalidKeySeparatorInLiteral() {
        val invalidExpression = listOf(
            KeyElement.literal("invalid:literal..key.ROOT.Key1.Key31")
        )
        assertNull(parse(invalidExpression))
    }

//invalid.literal..key.ROOT.Key1.Key31
    @Test
    fun testParseInvalidDefaultNsKeySeparatorInLiteral() {
        val invalidExpression = listOf(
            KeyElement.literal("invalid.literal..key.ROOT.Key1.Key31")
        )     
        assertNull(parse(invalidExpression))
    }

//invalid.literal.test:.key.ROOT.Key1.Key31
    @Test
    fun testParseInvalidDefaultNsKeySeparatorInLiteral2() {
        val invalidExpression = listOf(
            KeyElement.literal("invalid.literal.test:.key.ROOT.Key1.Key31")
        )
        assertNull(parse(invalidExpression))
    }

//.invalid.start.test
    @Test
    fun testParseInvalidStart() {
        val invalidExpression = listOf(
            KeyElement.literal(".invalid.start.test")
        )     
        assertNull(parse(invalidExpression))
    }

//:invalid.start.test
    @Test
    fun testParseInvalidStart2() {
        val invalidExpression = listOf(
            KeyElement.literal(":invalid.start.test")
        )
        assertNull(parse(invalidExpression))
    }
}