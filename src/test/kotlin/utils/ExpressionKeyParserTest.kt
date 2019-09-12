package utils

import com.eny.i18n.plugin.utils.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ExpressionKeyParserTest {
    fun extractTexts(list: List<Token>) = list.map {token -> token.text()}
//
//    @Test
//    fun parseSimpleLiteral() {
//        val literal = listOf(
//            KeyElement.fromLiteral("fileName:ROOT.Key2.Key3")
//        )
//        val parser = ExpressionKeyParser()
//        val expected = FullKey(
//            listOf(Literal("fileName")),
//            listOf(
//                Literal("ROOT"),
//                Literal("Key2"),
//                Literal("Key3")
//            )
//        )
//        assertEquals(expected, parser.parse(literal))
//    }
//
//    @Test
//    fun parseDefaultFileLiteral() {
//        val literal = listOf(
//            KeyElement.fromLiteral("ROOT.Key2.Key3")
//        )
//        val parser = ExpressionKeyParser()
//        val expected = FullKey(
//            listOf(),
//            listOf(
//                Literal("ROOT"),
//                Literal("Key2"),
//                Literal("Key3")
//            )
//        )
//        val parseOld = parser.parse(literal)
//        assertEquals(expected, parseOld)
//    }
//
//    @Test
//    fun parseExpressionWithFilePartInTemplate() {
//        val elements = listOf(
//            KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
//            KeyElement(":ROOT.Key1.Key31", ":ROOT.Key1.Key31", KeyElementType.LITERAL)
//        )
//        val parser = ExpressionKeyParser()
//        val expectedFileName = listOf("sample")
//        val expectedKey = listOf("ROOT", "Key1", "Key31")
//        val parsed = parser.parse(elements)
//        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
//        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
//    }
//
//    @Test
//    fun parsePrefixedExpressionWithFilePartInTemplate() {
//        val elements = listOf(
//                KeyElement("prefix", "prefix", KeyElementType.LITERAL),
//                KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
//                KeyElement(":ROOT.Key4.Key5", ":ROOT.Key4.Key5", KeyElementType.LITERAL)
//        )
//        val parser = ExpressionKeyParser()
//        val expectedFileName = listOf("prefix", "sample")
//        val expectedKey = listOf("ROOT", "Key4", "Key5")
//        val parsed = parser.parse(elements)
//        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
//        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
//    }
//
//    @Test
//    fun parsePostfixedExpressionWithFilePartInTemplate() {
//        val elements = listOf(
//            KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
//            KeyElement("postfix", "postfix", KeyElementType.LITERAL),
//            KeyElement(":ROOT.Key4.Key5", ":ROOT.Key4.Key5", KeyElementType.LITERAL)
//        )
//        val parser = ExpressionKeyParser()
//        val expectedFileName = listOf("sample", "postfix")
//        val expectedKey = listOf("ROOT", "Key4", "Key5")
//        val parsed = parser.parse(elements)
//        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
//        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
//    }
//
//    @Test
//    fun parseMixedExpressionWithFilePartInTemplate() {
//        val elements = listOf(
//                KeyElement("prefix", "prefix", KeyElementType.LITERAL),
//                KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
//                KeyElement("postfix", "postfix", KeyElementType.LITERAL),
//                KeyElement(":ROOT.Key4.Key5", ":ROOT.Key4.Key5", KeyElementType.LITERAL)
//        )
//        val parser = ExpressionKeyParser()
//        val expectedFileName = listOf("prefix", "sample", "postfix")
//        val expectedKey = listOf("ROOT", "Key4", "Key5")
//        val parsed = parser.parse(elements)
//        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
//        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
//    }
//
//    @Test
//    fun parseExpressionWithKeyInTemplate() {
//        val elements = listOf(
//            KeyElement("filename:", "filename:", KeyElementType.LITERAL),
//            KeyElement("\${key}", "Key0.Key2.Key21", KeyElementType.TEMPLATE)
//        )
//        val parser = ExpressionKeyParser()
//        val expectedFileName = listOf("filename")
//        val expectedKey = listOf("Key0", "Key2", "Key21")
//        val parsed = parser.parse(elements)
//        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
//        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
//    }
//
//    @Test
//    fun partOfKeyIsExpression() {
//        val elements = listOf(
//            KeyElement("filename:root.", "filename:root.", KeyElementType.LITERAL),
//            KeyElement("\${key}", "Key0.Key2.Key21", KeyElementType.TEMPLATE)
//        )
//        val parser = ExpressionKeyParser()
//        val expectedFileName = listOf("filename")
//        val expectedKey = listOf("root", "Key0", "Key2", "Key21")
//        val parsed = parser.parse(elements)
//        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
//        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
//    }

    @Test
    fun parseNsSeparatorInExpression() {
        val elements = listOf(
                KeyElement("prefix", "prefix", KeyElementType.LITERAL),
                KeyElement("\${fileExpr}", "partFile:partKey", KeyElementType.TEMPLATE),
                KeyElement("postfix", "postfix", KeyElementType.LITERAL),
                KeyElement(":ROOT.Key4.Key5", ":ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("prefix", "partFile")
        val expectedKey = listOf("postfix", "partKey", "ROOT", "Key4", "Key5")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }
}