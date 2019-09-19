package utils

import com.eny.i18n.plugin.utils.*
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

//@Ignore
class DefaultNsParserTest : TestBase{

    //    ROOT.Key2.Key3
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

    //${fileExpr}.ROOT.Key1.Key31       / sample
    @Test
    fun parseExpressionWithFilePartInTemplate() {
        val elements = listOf(
                KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
                KeyElement(".ROOT.Key1.Key31", ".ROOT.Key1.Key31", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedKey = listOf("sample", "ROOT", "Key1", "Key31")
        val parsed = parser.parse(elements)
        assertEquals(listOf(), extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //prefix${fileExpr}.ROOT.Key4.Key5  / sample
    @Test
    fun parsePrefixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
                KeyElement("prefix", "prefix", KeyElementType.LITERAL),
                KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
                KeyElement(".ROOT.Key4.Key5", ".ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedKey = listOf("prefixsample", "ROOT", "Key4", "Key5")
        val parsed = parser.parse(elements)
        assertEquals(listOf(), extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //${fileExpr}postfix.ROOT.Key4.Key5  / sample
    @Test
    fun parsePostfixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
                KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
                KeyElement("postfix", "postfix", KeyElementType.LITERAL),
                KeyElement(".ROOT.Key4.Key5", ".ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedKey = listOf("samplepostfix", "ROOT", "Key4", "Key5")
        val parsed = parser.parse(elements)
        assertEquals(listOf(), extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //prefix${fileExpr}postfix.ROOT.Key4.Key5  / sample
    @Test
    fun parseMixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
                KeyElement("prefix", "prefix", KeyElementType.LITERAL),
                KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
                KeyElement("postfix", "postfix", KeyElementType.LITERAL),
                KeyElement("ROOT.Key4.Key5", "ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedKey = listOf("prefixsamplepostfixROOT", "Key4", "Key5")
        val parsed = parser.parse(elements)
        assertEquals(listOf(), extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }

    //prefix${fileExpr}postfix.ROOT.Key4.Key5   / partFile.partKey
    @Test
    fun parseNsSeparatorInExpression() {
        val elements = listOf(
            KeyElement("prefix", "prefix", KeyElementType.LITERAL),
            KeyElement("\${fileExpr}", "partFile.partKey", KeyElementType.TEMPLATE),
            KeyElement("postfix", "postfix", KeyElementType.LITERAL),
            KeyElement(".ROOT.Key4.Key5", ".ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedKey = listOf("prefixpartFile", "partKeypostfix", "ROOT", "Key4", "Key5")
        val parsed = parser.parse(elements)
        assertEquals(listOf(), extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
    }
}