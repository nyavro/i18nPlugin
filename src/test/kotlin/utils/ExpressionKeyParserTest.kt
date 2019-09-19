package utils

import com.eny.i18n.plugin.utils.*
import org.junit.Test
import kotlin.test.assertEquals

class ExpressionKeyParserTest : TestBase {

//fileName:ROOT.Key2.Key3                   /                       / fileName{8}:ROOT{4}.Key2{4}.Key3{4}
    @Test
    fun parseSimpleLiteral() {
        val elements = listOf(
            KeyElement.fromLiteral("fileName:ROOT.Key2.Key3")
        )
        val parsed = ExpressionKeyParser().parse(elements)
        assertEquals("fileName{8}:ROOT{4}.Key2{4}.Key3{4}", toTestString(parsed))
    }

//${fileExpr}:ROOT.Key1.Key31               / sample                / sample{11}:ROOT{4}.Key1{4}.Key31{5}
    @Test
    fun parseExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
            KeyElement(":ROOT.Key1.Key31", ":ROOT.Key1.Key31", KeyElementType.LITERAL)
        )
        val parsed = ExpressionKeyParser().parse(elements)
        assertEquals("sample{11}:ROOT{4}.Key1{4}.Key31{5}", toTestString(parsed))
    }

//prefix${fileExpr}:ROOT.Key4.Key5          / sample                / prefixsample{11}:ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parsePrefixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
                KeyElement("prefix", "prefix", KeyElementType.LITERAL),
                KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
                KeyElement(":ROOT.Key4.Key5", ":ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parsed = ExpressionKeyParser().parse(elements)
        assertEquals("sample{11}:ROOT{4}.Key1{4}.Key31{5}", toTestString(parsed))
    }

//${fileExpr}postfix:ROOT.Key4.Key5         / sample                / samplepostfix{18}:ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parsePostfixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
            KeyElement("postfix", "postfix", KeyElementType.LITERAL),
            KeyElement(":ROOT.Key4.Key5", ":ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parsed = ExpressionKeyParser().parse(elements)
        assertEquals("sample{11}:ROOT{4}.Key1{4}.Key31{5}", toTestString(parsed))
    }

//prefix${fileExpr}postfix:ROOT.Key4.Key5   / sample                / prefixsamplepostfix{24}:ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parseMixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
                KeyElement("prefix", "prefix", KeyElementType.LITERAL),
                KeyElement("\${fileExpr}", "sample", KeyElementType.TEMPLATE),
                KeyElement("postfix", "postfix", KeyElementType.LITERAL),
                KeyElement(":ROOT.Key4.Key5", ":ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parsed = ExpressionKeyParser().parse(elements)
        assertEquals("sample{11}:ROOT{4}.Key1{4}.Key31{5}", toTestString(parsed))
    }

//prefix${fileExpr}postfix.ROOT.Key4.Key5   / partFile:partKey      / prefixpartFile{17}:partKeypostfix{7}.ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parseNsSeparatorInExpression() {
        val elements = listOf(
                KeyElement("prefix", "prefix", KeyElementType.LITERAL),
                KeyElement("\${fileExpr}", "partFile:partKey", KeyElementType.TEMPLATE),
                KeyElement("postfix.ROOT.Key4.Key5", "postfix.ROOT.Key4.Key5", KeyElementType.LITERAL)
        )
        val parsed = ExpressionKeyParser().parse(elements)
        assertEquals("sample{11}:ROOT{4}.Key1{4}.Key31{5}", toTestString(parsed))
    }

//filename:${key}                           / Key0.Key2.Key21       / filename{8}:Key0{6}.Key2{0}.Key21{0}
    @Test
    fun parseExpressionWithKeyInTemplate() {
        val elements = listOf(
            KeyElement("filename:", "filename:", KeyElementType.LITERAL),
            KeyElement("\${key}", "Key0.Key2.Key21", KeyElementType.TEMPLATE)
        )
        val parsed = ExpressionKeyParser().parse(elements)
        assertEquals("filename{8}:Key0{6}.Key2{0}.Key21{0}", toTestString(parsed))
    }

//filename:${key}item                       / Key0.Key2.Key21.      / filename{8}:Key0{6}.Key2{0}.Key21{0}.item{4}
    @Test
    fun parseExpressionWithKeyInTemplate2() {
        val elements = listOf(
                KeyElement("filename:", "filename:", KeyElementType.LITERAL),
                KeyElement("\${key}", "Key0.Key2.Key21.", KeyElementType.TEMPLATE),
                KeyElement("item", "item", KeyElementType.LITERAL)
        )
        val parsed = ExpressionKeyParser().parse(elements)
        assertEquals("sample{11}:ROOT{4}.Key1{4}.Key31{5}", toTestString(parsed))
}

//filename:${key}.item                      / Key0.Key2.Key21       / filename{8}:Key0{6}.Key2{0}.Key21{0}.item{4}
    @Test
    fun parseExpressionWithKeyInTemplate3() {
        val elements = listOf(
                KeyElement("filename:", "filename:", KeyElementType.LITERAL),
                KeyElement("\${key}", "Key0.Key2.Key21", KeyElementType.TEMPLATE),
                KeyElement(".item", ".item", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("Key0", "Key2", "Key21", "item")
        val parsed = parser.parse(elements)
        val textLengths = extractLengths(parsed?.compositeKey)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
        assertEquals(8, parsed?.nsLength)
        assertEquals(11, parsed?.keyLength)
        assertEquals(20, parsed?.length)
        assertEquals(listOf(6, 0, 0, 4), textLengths)
    }

//filename:${key}item                       / Key0.Key2.Key21       / filename{8}:Key0{6}.Key2{0}.Key21item{3}
    @Test
    fun parseExpressionWithKeyInTemplate4() {
        val elements = listOf(
                KeyElement("filename:", "filename:", KeyElementType.LITERAL),
                KeyElement("\${key}", "Key0.Key2.Key21", KeyElementType.TEMPLATE),
                KeyElement("item", "item", KeyElementType.LITERAL)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("Key0", "Key2", "Key21item")
        val parsed = parser.parse(elements)
        val textLengths = extractLengths(parsed?.compositeKey)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
        assertEquals(8, parsed?.nsLength)
        assertEquals(10, parsed?.keyLength)
        assertEquals(19, parsed?.length)
        assertEquals(listOf(6, 0, 4), textLengths)
    }

//filename:root.${key}                      / Key0.Key2.Key21       / filename{8}:root{4}.Key0{6}.Key2{0}.Key21{0}
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
        val textLengths = extractLengths(parsed?.compositeKey)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
        assertEquals(8, parsed?.nsLength)
        assertEquals(11, parsed?.keyLength)
        assertEquals(20, parsed?.length)
        assertEquals(listOf(4, 6, 0, 0), textLengths)
    }

//filename:root${key}                       / .Key0.Key2.Key21      / filename{8}:root{4}.Key0{6}.Key2{0}.Key21{0}
    @Test
    fun partOfKeyIsExpression2() {
        val elements = listOf(
                KeyElement("filename:root", "filename:root", KeyElementType.LITERAL),
                KeyElement("\${key}", ".Key0.Key2.Key21", KeyElementType.TEMPLATE)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("root", "Key0", "Key2", "Key21")
        val parsed = parser.parse(elements)
        val textLengths = extractLengths(parsed?.compositeKey)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
        assertEquals(8, parsed?.nsLength)
        assertEquals(10, parsed?.keyLength)
        assertEquals(19, parsed?.length)
        assertEquals(listOf(4, 6, 0, 0), textLengths)
    }

//filename:root${key}                       / Key0.Key2.Key21       / filename{8}:rootKey0{9}.Key2{0}.Key21{0}
    @Test
    fun partOfKeyIsExpression3() {
        val elements = listOf(
                KeyElement("filename:root", "filename:root", KeyElementType.LITERAL),
                KeyElement("\${key}", "Key0.Key2.Key21", KeyElementType.TEMPLATE)
        )
        val parser = ExpressionKeyParser()
        val expectedFileName = listOf("filename")
        val expectedKey = listOf("rootKey0", "Key2", "Key21")
        val parsed = parser.parse(elements)
        val textLengths = extractLengths(parsed?.compositeKey)
        assertEquals(expectedFileName, extractTexts(parsed?.fileName ?: listOf()))
        assertEquals(expectedKey, extractTexts(parsed?.compositeKey ?: listOf()))
        assertEquals(8, parsed?.nsLength)
        assertEquals(10, parsed?.keyLength)
        assertEquals(19, parsed?.length)
        assertEquals(listOf(10, 0, 0), textLengths)
    }
}

//fileName:ROOT.Key2.Key3                   /                       / fileName{8}:ROOT{4}.Key2{4}.Key3{4}
//${fileExpr}:ROOT.Key1.Key31               / sample                / sample{11}:ROOT{4}.Key1{4}.Key31{5}
//prefix${fileExpr}:ROOT.Key4.Key5          / sample                / prefixsample{11}:ROOT{4}.Key4{4}.Key5{4}
//${fileExpr}postfix:ROOT.Key4.Key5         / sample                / samplepostfix{18}:ROOT{4}.Key4{4}.Key5{4}
//prefix${fileExpr}postfix:ROOT.Key4.Key5   / sample                / prefixsamplepostfix{24}:ROOT{4}.Key4{4}.Key5{4}
//prefix${fileExpr}postfix.ROOT.Key4.Key5   / partFile:partKey      / prefixpartFile{17}:partKeypostfix{6}.ROOT{4}.Key4{4}.Key5{4}
//filename:${key}                           / Key0.Key2.Key21       / filename{8}:Key0{5}.Key2{0}.Key21{0}
//filename:${key}item                       / Key0.Key2.Key21.      / filename{8}:Key0{5}.Key2{0}.Key21{0}.item{3}
//filename:${key}.item                      / Key0.Key2.Key21       / filename{8}:Key0{5}.Key2{0}.Key21{0}.item{4}
//filename:${key}item                       / Key0.Key2.Key21       / filename{8}:Key0{5}.Key2{0}.Key21item{3}
//filename:root.${key}                      / Key0.Key2.Key21       / filename{8}:root{4}.Key0{5}.Key2{0}.Key21{0}
//filename:root${key}                       / .Key0.Key2.Key21      / filename{8}:root{4}.Key0{4}.Key2{0}.Key21{0}
//filename:root${key}                       / Key0.Key2.Key21       / filename{8}:rootKey0{9}.Key2{0}.Key21{0}
