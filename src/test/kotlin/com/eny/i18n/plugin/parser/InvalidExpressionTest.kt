package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import org.junit.Test
import kotlin.test.assertNull

//@Ignore
internal class InvalidExpressionTest : ParserTestBase {

//invalid:file:literal.ROOT.Key1.Key31
    @Test
    fun parseInvalidFilenameInLiteral() {
        val invalidExpression = listOf(
            KeyElement.literal("invalid:file:literal.ROOT.Key1.Key31")
        )     
        assertNull(parse(invalidExpression))
    }

//invalid:literal..key.ROOT.Key1.Key31
    @Test
    fun parseInvalidKeySeparatorInLiteral() {
        val invalidExpression = listOf(
            KeyElement.literal("invalid:literal..key.ROOT.Key1.Key31")
        )
        assertNull(parse(invalidExpression))
    }

//invalid.literal..key.ROOT.Key1.Key31
    @Test
    fun parseInvalidDefaultNsKeySeparatorInLiteral() {
        val invalidExpression = listOf(
            KeyElement.literal("invalid.literal..key.ROOT.Key1.Key31")
        )     
        assertNull(parse(invalidExpression))
    }

//invalid.literal.test:.key.ROOT.Key1.Key31
    @Test
    fun parseInvalidDefaultNsKeySeparatorInLiteral2() {
        val invalidExpression = listOf(
            KeyElement.literal("invalid.literal.test:.key.ROOT.Key1.Key31")
        )
        assertNull(parse(invalidExpression))
    }

//.invalid.start.test
    @Test
    fun parseInvalidStart() {
        val invalidExpression = listOf(
            KeyElement.literal(".invalid.start.test")
        )     
        assertNull(parse(invalidExpression))
    }

//:invalid.start.test
    @Test
    fun parseInvalidStart2() {
        val invalidExpression = listOf(
            KeyElement.literal(":invalid.start.test")
        )
        assertNull(parse(invalidExpression))
    }
}