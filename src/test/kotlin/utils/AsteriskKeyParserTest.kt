package utils

import com.eny.i18n.plugin.utils.*
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

@Ignore
class AsteriskKeyParserTest : TestBase{

    //${fileExpr}:ROOT.Key1.Key31       / *
    @Test
    fun parseExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement("\${fileExpr}", null, KeyElementType.TEMPLATE),
            KeyElement(":ROOT.Key1.Key31", ":ROOT.Key1.Key31", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("*")
        val expectedKey = listOf("ROOT", "Key1", "Key31")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //prefix${fileExpr}:ROOT.Key4.Key5  / *
    @Test
    fun parsePrefixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement("prefix", "prefix", KeyElementType.LITERAL),
            KeyElement("\${fileExpr}", null, KeyElementType.TEMPLATE),
            KeyElement(":ROOT.Key4.Key5", ":ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("prefix", "*")
        val expectedKey = listOf("ROOT", "Key4", "Key5")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //${fileExpr}postfix:ROOT.Key4.Key5  / *
    @Test
    fun parsePostfixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement("\${fileExpr}", null, KeyElementType.TEMPLATE),
            KeyElement("postfix", "postfix", KeyElementType.LITERAL),
            KeyElement(":ROOT.Key4.Key5", ":ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("*", "postfix")
        val expectedKey = listOf("ROOT", "Key4", "Key5")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //prefix${fileExpr}postfix:ROOT.Key4.Key5  / *
    @Test
    fun parseMixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
                KeyElement("prefix", "prefix", KeyElementType.LITERAL),
                KeyElement("\${fileExpr}", null, KeyElementType.TEMPLATE),
                KeyElement("postfix", "postfix", KeyElementType.LITERAL),
                KeyElement(":ROOT.Key4.Key5", ":ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("prefix", "*", "postfix")
        val expectedKey = listOf("ROOT", "Key4", "Key5")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //prefix${fileExpr}postfix.ROOT.Key4.Key5   / *
    @Test
    fun parseNsSeparatorInExpression() {
        val elements = listOf(
                KeyElement("prefix", "prefix", KeyElementType.LITERAL),
                KeyElement("\${fileExpr}", null, KeyElementType.TEMPLATE),
                KeyElement("postfix", "postfix", KeyElementType.LITERAL),
                KeyElement(".ROOT.Key4.Key5", ".ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedKey = listOf("prefix", "*", "postfix", "ROOT", "Key4", "Key5")
        val parsed = parser.parse(elements)
        assertEquals(listOf(), extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

//    filename:${key}   / *
    @Test
    fun parseExpressionWithKeyInTemplate() {
        val elements = listOf(
            KeyElement("filename:", "filename:", KeyElementType.LITERAL),
            KeyElement("\${key}", null, KeyElementType.TEMPLATE)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("*")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

//    filename:${key}item   / *
    @Test
    fun parseExpressionWithKeyInTemplate2() {
        val elements = listOf(
                KeyElement("filename:", "filename:", KeyElementType.LITERAL),
                KeyElement("\${key}", null, KeyElementType.TEMPLATE),
                KeyElement("item", "item", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("*item")  // Todo: listOf("*", "item")     ???

        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //filename:${key}.item   / *
    @Test
    fun parseExpressionWithKeyInTemplate3() {
        val elements = listOf(
                KeyElement("filename:", "filename:", KeyElementType.LITERAL),
                KeyElement("\${key}", null, KeyElementType.TEMPLATE),
                KeyElement(".item", ".item", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("*", "item")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //filename:root.${key}  / *
    @Test
    fun partOfKeyIsExpression() {
        val elements = listOf(
            KeyElement("filename:root.", "filename:root.", KeyElementType.LITERAL),
            KeyElement("\${key}", null, KeyElementType.TEMPLATE)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("root", "*")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //filename:root${key}   / *
    @Test
    fun partOfKeyIsExpression2() {
        val elements = listOf(
                KeyElement("filename:root", "filename:root", KeyElementType.LITERAL),
                KeyElement("\${key}", null, KeyElementType.TEMPLATE)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("root", "*")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //filename:root${key}   / *
    @Test
    fun partOfKeyIsExpression3() {
        val elements = listOf(
                KeyElement("filename:root", "filename:root", KeyElementType.LITERAL),
                KeyElement("\${key}", null, KeyElementType.TEMPLATE)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("root", "*")
        val parsed = parser.parse(elements)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }
}