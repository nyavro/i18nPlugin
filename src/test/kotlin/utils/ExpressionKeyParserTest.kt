package utils

import com.eny.i18n.plugin.utils.ExpressionKeyParser
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.KeyElementType
import org.junit.Test
import kotlin.math.exp
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ExpressionKeyParserTest {

//    @Test
//    fun parseInvalidFilenameInLiteral() {
//        val invalidExpression = listOf(
//            KeyElement.fromLiteral("invalid:file:literal.ROOT.Key1.Key31")
//        )
//        val parser = ExpressionKeyParser()
//        assertNull(parser.parse(invalidExpression))
//    }
//
//    @Test
//    fun parseInvalidKeySeparatorInLiteral() {
//        val invalidExpression = listOf(
//                KeyElement.fromLiteral("invalid:literal..key.ROOT.Key1.Key31")
//        )
//        val parser = ExpressionKeyParser()
//        assertNull(parser.parse(invalidExpression))
//    }
//
//    @Test
//    fun parseSimpleLiteral() {
//        val literal = listOf(
//            KeyElement.fromLiteral("fileName:ROOT.Key2.Key3")
//        )
//        val parser = ExpressionKeyParser()
//        val expected = FullKey(
//            listOf(KeyElement("fileName", "fileName", KeyElementType.LITERAL)),
//            listOf(
//                KeyElement("ROOT", "ROOT", KeyElementType.LITERAL),
//                KeyElement("Key2", "Key2", KeyElementType.LITERAL),
//                KeyElement("Key3", "Key3", KeyElementType.LITERAL)
//            )
//        )
//        assertEquals(expected, parser.parse(literal))
//    }

//    @Test
//    fun parseDefaultFileLiteral() {
//        val literal = listOf(
//                KeyElement.fromLiteral("ROOT.Key2.Key3")
//        )
//        val parser = ExpressionKeyParser()
//        val expected = FullKey(
//            listOf(),
//            listOf(
//                KeyElement("ROOT", "ROOT", KeyElementType.LITERAL),
//                KeyElement("Key2", "Key2", KeyElementType.LITERAL),
//                KeyElement("Key3", "Key3", KeyElementType.LITERAL)
//            )
//        )
//        val parse = parser.parse(literal)
//        assertEquals(expected, parse)
//    }

//    @Test
//    fun tooShortToBeKey() {
//        val literal = listOf(
//            KeyElement.fromLiteral("probably_not_a_reference_to_i18n")
//        )
//        val parser = ExpressionKeyParser()
//        assertNull(parser.parse(literal))
//    }

//    @Test
//    fun parseInvalidExpression() {
//        val invalidExpression = listOf(
//            KeyElement("\${fileExpr}", "sample:file", KeyElementType.TEMPLATE),
//            KeyElement(":ROOT.Key1.Key31", ":ROOT.Key1.Key31", KeyElementType.LITERAL)
//        )
//        val parser = ExpressionKeyParser()
//        assertNull(parser.parse(invalidExpression))
//    }
//
//    @Test
//    fun parseExpressionWithFilePartInTemplate() {
//        val elements = listOf(
//            KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
//            KeyElement(":ROOT.Key1.Key31", ":ROOT.Key1.Key31", KeyElementType.LITERAL)
//        )
//        val parser = ExpressionKeyParser()
//        val expected = FullKey(
//            listOf(KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE)),
//            listOf(
//                KeyElement("ROOT", "ROOT", KeyElementType.LITERAL),
//                KeyElement("Key1", "Key1", KeyElementType.LITERAL),
//                KeyElement("Key31", "Key31", KeyElementType.LITERAL)
//            )
//        )
//        val parsed = parser.parse(elements)
//        assertEquals(expected, parsed)
//    }
//
//    @Test
//    fun parseExpressionWithKeyInTemplate() {
//        val elements = listOf(
//            KeyElement("filename:", "filename:", KeyElementType.LITERAL),
//            KeyElement("\${key}", "Key0.Key2.Key21", KeyElementType.TEMPLATE)
//        )
//        val parser = ExpressionKeyParser()
//        val expected = FullKey(
//            listOf(KeyElement("filename", "filename", KeyElementType.LITERAL)),
//            listOf(
//                KeyElement("\${key}", "Key0", KeyElementType.TEMPLATE),
//                KeyElement("", "Key2", KeyElementType.TEMPLATE),
//                KeyElement("", "Key21", KeyElementType.TEMPLATE)
//            )
//        )
//        val parsed = parser.parse(elements)
//        assertEquals(expected, parsed)
//    }
//
//    @Test
//    fun partOfKeyIsExpression() {
//        val elements = listOf(
//            KeyElement("filename:root.", "filename:root.", KeyElementType.LITERAL),
//            KeyElement("\${key}", "Key0.Key2.Key21", KeyElementType.TEMPLATE)
//        )
//        val parser = ExpressionKeyParser()
//        val expected = FullKey(
//            listOf(KeyElement("filename", "filename", KeyElementType.LITERAL)),
//            listOf(
//                KeyElement("root", "root", KeyElementType.LITERAL),
//                KeyElement("\${key}", "Key0", KeyElementType.TEMPLATE),
//                KeyElement("", "Key2", KeyElementType.TEMPLATE),
//                KeyElement("", "Key21", KeyElementType.TEMPLATE)
//            )
//        )
//        val parsed = parser.parse(elements)
//        assertEquals(expected.fileName, parsed?.fileName)
//        assertEquals(expected.compositeKey, parsed?.compositeKey)
//    }
}
//<[KeyElement(text=root, resolvedTo=root, type=LITERAL), KeyElement(text=${key}, resolvedTo=Key0, type=TEMPLATE), KeyElement(text=, resolvedTo=Key2, type=TEMPLATE), KeyElement(text=, resolvedTo=Key21, type=TEMPLATE)]>,
//<[KeyElement(text=root, resolvedTo=root, type=LITERAL), KeyElement(text=, resolvedTo=, type=LITERAL), KeyElement(text=${key}, resolvedTo=Key0, type=TEMPLATE), KeyElement(text=, resolvedTo=Key2, type=TEMPLATE), KeyElement(text=, resolvedTo=Key21, type=TEMPLATE)]>.