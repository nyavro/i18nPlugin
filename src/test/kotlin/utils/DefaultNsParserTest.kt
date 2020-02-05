package utils

import com.eny.i18n.plugin.utils.KeyElement
import org.junit.Test
import kotlin.test.assertEquals

//@Ignore
internal class DefaultNsParserTest : TestBase {

//ROOT.Key2.Key3                            /                   / ROOT{4}.Key2{4}.Key3{4}
    @Test
    fun parseDefaultFileLiteral() {
        val elements = listOf(
            KeyElement.literal("ROOT.Key2.Key3")
        )
        val parsed = parse(elements)
        assertEquals("ROOT{4}.Key2{4}.Key3{4}", toTestString(parsed))
    }

//${fileExpr}.ROOT.Key1.Key31               / sample            / sample{11}.ROOT{4}.Key1{4}.Key31{5}
    @Test
    fun parseExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.resolvedTemplate("\${fileExpr}", "sample"),
            KeyElement.literal(".ROOT.Key1.Key31")
        )
        val parsed = parse(elements)
        assertEquals("sample{11}.ROOT{4}.Key1{4}.Key31{5}", toTestString(parsed))
    }

//prefix${fileExpr}.ROOT.Key4.Key5          / sample            / prefixsample{17}.ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parsePrefixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
                KeyElement.literal("prefix"),
                KeyElement.resolvedTemplate("\${fileExpr}", "sample"),
                KeyElement.literal(".ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefixsample{17}.ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//${fileExpr}postfix.ROOT.Key4.Key5         / sample            / samplepostfix{18}.ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parsePostfixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
                KeyElement.resolvedTemplate("\${fileExpr}", "sample"),
                KeyElement.literal("postfix"),
                KeyElement.literal(".ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("samplepostfix{18}.ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//prefix${fileExpr}postfix.ROOT.Key4.Key5   / sample            / prefixsamplepostfix{24}.ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parseMixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.literal("prefix"),
            KeyElement.resolvedTemplate("\${fileExpr}", "sample"),
            KeyElement.literal("postfix.ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefixsamplepostfix{24}.ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//prefix${fileExpr}postfix.ROOT.Key4.Key5   / partFile.partKey  / prefixpartFile{17}.partKeypostfix{7}.ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parseNsSeparatorInExpression() {
        val elements = listOf(
            KeyElement.literal("prefix"),
            KeyElement.resolvedTemplate("\${fileExpr}", "partFile.partKey"),
            KeyElement.literal("postfix.ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefixpartFile{17}.partKeypostfix{7}.ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//root.start${key}                          / *                     / root{4}.start*{11}
    @Test
    fun parseKeyInExpression() {
        val elements = listOf(
            KeyElement.literal("root.start"),
            KeyElement.unresolvedTemplate("\${key}")
        )
        val parsed = parse(elements)
        assertEquals("root{4}.start*{11}", toTestString(parsed))
    }

//root.start${key}                          / .Key0.Key2.Key21      / root{4}.start{5}.Key0{6}.Key2{0}.Key21{0}
    @Test
    fun parseKeyInExpression2() {
        val elements = listOf(
            KeyElement.literal("root.start"),
            KeyElement.resolvedTemplate("\${key}", ".Key0.Key2.Key21")
        )
        val parsed = parse(elements)
        assertEquals("root{4}.start{5}.Key0{6}.Key2{0}.Key21{0}", toTestString(parsed))
    }

//root.start${key}                          / Key0.Key2.Key21       / root{4}.startKey0{11}.Key2{0}.Key21{0}
    @Test
    fun parseKeyInExpression3() {
        val elements = listOf(
            KeyElement.literal("root.start"),
            KeyElement.resolvedTemplate("\${key}", "Key0.Key2.Key21")
        )
        val parsed = parse(elements)
        assertEquals("root{4}.startKey0{11}.Key2{0}.Key21{0}", toTestString(parsed))
    }

//filename:${key}                           / *         filename{8}.*{6}
    @Test
    fun parseExpressionWithKeyInTemplate() {
        val elements = listOf(
                KeyElement.literal("filename."),
                KeyElement.unresolvedTemplate("\${key}")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}.*{6}", toTestString(parsed))
    }

//filename.root.${key}                      / *         filename{8}.root{4}.*{6}
    @Test
    fun partOfKeyIsExpression() {
        val elements = listOf(
                KeyElement.literal("filename.root."),
                KeyElement.unresolvedTemplate("\${key}")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}.root{4}.*{6}", toTestString(parsed))
    }

//filename.${key}item                       / *         filename{8}.*item{10}
    @Test
    fun parseExpressionWithKeyInTemplate2() {
        val elements = listOf(
                KeyElement.literal("filename."),
                KeyElement.unresolvedTemplate("\${key}"),
                KeyElement.literal("item")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}.*item{10}", toTestString(parsed))
    }

//filename.${key}.item                      / *         filename{8}.*{6}.item{4}
    @Test
    fun parseExpressionWithKeyInTemplate3() {
        val elements = listOf(
                KeyElement.literal("filename."),
                KeyElement.unresolvedTemplate("\${key}"),
                KeyElement.literal(".item")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}.*{6}.item{4}", toTestString(parsed))
    }

//filename.${key}item                       / Key0.Key2.Key21       / filename{8}.Key0{6}.Key2{0}.Key21item{4}
    @Test
    fun parseExpressionWithKeyInTemplate4() {
        val elements = listOf(
                KeyElement.literal("filename."),
                KeyElement.resolvedTemplate("\${key}", "Key0.Key2.Key21"),
                KeyElement.literal("item")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}.Key0{6}.Key2{0}.Key21item{4}", toTestString(parsed))
    }
}



