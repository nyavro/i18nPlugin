package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import org.junit.Test
import kotlin.test.assertEquals

internal class CustomSeparatorsParserTest: ParserTestBase {
//fileName:ROOT.Key2.Key3                   /                       / fileName{8}:ROOT.Key2.Key3{14}
    @Test
    fun testParseSimpleLiteral() {
        val elements = listOf(
                KeyElement.literal("fileName:ROOT.Key2.Key3")
        )
        val parsed = parse(elements, false, ":", "$")
        assertEquals("fileName{8}:ROOT.Key2.Key3{14}", toTestString(parsed))
        assertEquals("fileName:ROOT.Key2.Key3", parsed?.source)
    }

//${fileExpr}$ROOT^Key1^Key31               / *         *{11}:ROOT{4}.Key1{4}.Key31{5}
    @Test
    fun testParseExpressionWithFilePartInTemplate2() {
        val elements = listOf(
                KeyElement.template("\${fileExpr}"),
                KeyElement.literal("\$ROOT^Key1^Key31")
        )
        val parsed = parse(elements, false, "$", "^")
        assertEquals("*{11}:ROOT{4}.Key1{4}.Key31{5}", toTestString(parsed))
    }
}