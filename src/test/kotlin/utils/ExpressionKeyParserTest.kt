package utils

import com.eny.i18n.plugin.utils.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ExpressionKeyParserTest {
    fun extractTexts(list: List<Token>) = list.map {token -> token.text()}

    @Test
    fun parseInvalidFilenameInLiteral() {
        val invalidExpression = listOf(
            KeyElement.fromLiteral("invalid:file:literal.ROOT.Key1.Key31")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

    @Test
    fun parseInvalidKeySeparatorInLiteral() {
        val invalidExpression = listOf(
                KeyElement.fromLiteral("invalid:literal..key.ROOT.Key1.Key31")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

    @Test
    fun parseSimpleLiteral() {
        val literal = listOf(
            KeyElement.fromLiteral("fileName:ROOT.Key2.Key3")
        )
        val parser = ExpressionKeyParser()
        val expected = FullKey(
            listOf(Literal("fileName")),
            listOf(
                Literal("ROOT"),
                Literal("Key2"),
                Literal("Key3")
            )
        )
        assertEquals(expected, parser.parse(literal))
    }

    @Test
    fun parseDefaultFileLiteral() {
        val literal = listOf(
            KeyElement.fromLiteral("ROOT.Key2.Key3")
        )
        val parser = ExpressionKeyParser()
        val expected = FullKey(
            listOf(),
            listOf(
                Literal("ROOT"),
                Literal("Key2"),
                Literal("Key3")
            )
        )
        val parseOld = parser.parse(literal)
        assertEquals(expected, parseOld)
    }

    @Test
    fun tooShortToBeKey() {
        val literal = listOf(
            KeyElement.fromLiteral("probably_not_a_reference_to_i18n")
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(literal))
    }

    @Test
    fun parseInvalidExpression() {
        val invalidExpression = listOf(
            KeyElement("\${fileExpr}", "sample:file", KeyElementType.TEMPLATE),
            KeyElement(":ROOT.Key1.Key31", ":ROOT.Key1.Key31", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        assertNull(parser.parse(invalidExpression))
    }

    @Test
    fun parseExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
            KeyElement(":ROOT.Key1.Key31", ":ROOT.Key1.Key31", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("sample")
        val expectedKey = listOf("ROOT", "Key1", "Key31")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    @Test
    fun parseExpressionWithKeyInTemplate() {
        val elements = listOf(
            KeyElement("filename:", "filename:", KeyElementType.LITERAL),
            KeyElement("\${key}", "Key0.Key2.Key21", KeyElementType.TEMPLATE)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("Key0", "Key2", "Key21")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    @Test
    fun partOfKeyIsExpression() {
        val elements = listOf(
            KeyElement("filename:root.", "filename:root.", KeyElementType.LITERAL),
            KeyElement("\${key}", "Key0.Key2.Key21", KeyElementType.TEMPLATE)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("root", "Key0", "Key2", "Key21")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }
}