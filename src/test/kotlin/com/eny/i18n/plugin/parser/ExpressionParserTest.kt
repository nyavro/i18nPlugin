package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
//TODO rename class
internal class ExpressionParserTest {

    @Test
    fun parse() {
        val text = ":ROOT.Key1.Key31"
        val elements = listOf(
            KeyElement.literal("`"),
            KeyElement.literal("$"),
            KeyElement.literal("{"),
            KeyElement.template("fileExpr"),
            KeyElement.literal("}"),
            KeyElement.literal(text),
            KeyElement.literal("`")
        )
        val parser = ExpressionNormalizer()
        val expected = listOf(
            KeyElement.template("\${fileExpr}"),
            KeyElement.literal(text)
        )
        assertEquals(elements.mapNotNull { parser.normalize(it) }, expected)
    }
}