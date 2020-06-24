package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ExpressionKeyParserTest : ParserTestBase {

//fileName:ROOT.Key2.Key3                   /                       / fileName{8}:ROOT{4}.Key2{4}.Key3{4}
    @Test
    fun parseSimpleLiteral() {
        val elements = listOf(
            KeyElement.literal("fileName:ROOT.Key2.Key3")
        )
        val parsed = parse(elements)
        assertEquals("fileName{8}:ROOT{4}.Key2{4}.Key3{4}", toTestString(parsed))
        assertEquals("fileName:ROOT.Key2.Key3", parsed?.source)
    }

//fileName:ROOT.Key2.Key3.                  /                       / fileName{8}:ROOT{4}.Key2{4}.Key3{4}.{0}
    @Test
    fun parseSimpleLiteral2() {
        val elements = listOf(
            KeyElement.literal("fileName:ROOT.Key2.Key3.")
        )
        val parsed = parse(elements)
        assertEquals("fileName{8}:ROOT{4}.Key2{4}.Key3{4}.{0}", toTestString(parsed))
        assertEquals("fileName:ROOT.Key2.Key3.", parsed?.source)
    }

    @Test
    fun parseSimpleLiteralList() {
        val elements = listOf(
            KeyElement.literal("file"),
            KeyElement.literal("Name:ROOT.Key3.Key4.")
        )
        val parsed = parse(elements)
        assertEquals("fileName{8}:ROOT{4}.Key3{4}.Key4{4}.{0}", toTestString(parsed))
        assertEquals("fileName:ROOT.Key3.Key4.", parsed?.source)
    }
}
