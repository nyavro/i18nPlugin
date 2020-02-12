package utils

import com.eny.i18n.plugin.utils.KeyElement
import org.junit.Test
import kotlin.test.assertEquals

internal class ExpressionKeyParserTest : TestBase {

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

//${fileExpr}:ROOT.Key1.Key31               / sampla                / sampla{11}:ROOT{4}.Key1{4}.Key31{5}
    @Test
    fun parseExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.resolvedTemplate("\${fileExpr}", "sampla"),
            KeyElement.literal(":ROOT.Key1.Key31")
        )
        val parsed = parse(elements)
        assertEquals("sampla{11}:ROOT{4}.Key1{4}.Key31{5}", toTestString(parsed))
        assertEquals("\${fileExpr}:ROOT.Key1.Key31", parsed?.source)
    }

//prefix${fileExpr}:ROOT.Key4.Key5          / sample                / prefixsample{17}:ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parsePrefixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
                KeyElement.literal("prefix"),
                KeyElement.resolvedTemplate("\${fileExpr}", "sample"),
                KeyElement.literal(":ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefixsample{17}:ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
        assertEquals("prefix\${fileExpr}:ROOT.Key4.Key5", parsed?.source)
    }

//${fileExpr}postfix:ROOT.Key4.Key5         / sample                / samplepostfix{18}:ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parsePostfixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.resolvedTemplate("\${fileExpr}", "sample"),
            KeyElement.literal("postfix:ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("samplepostfix{18}:ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
        assertEquals("\${fileExpr}postfix:ROOT.Key4.Key5", parsed?.source)
    }

//prefix${fileExpr}postfix:ROOT.Key4.Key5   / sample                / prefixsamplepostfix{24}:ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parseMixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.literal("prefix"),
            KeyElement.resolvedTemplate("\${fileExpr}", "sample"),
            KeyElement.literal("postfix:ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefixsamplepostfix{24}:ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
        assertEquals("prefix\${fileExpr}postfix:ROOT.Key4.Key5", parsed?.source)
    }

//prefix${fileExpr}postfix.ROOT.Key4.Key5   / partFile:partKey      / prefixpartFile{17}:partKeypostfix{7}.ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parseNsSeparatorInExpression() {
        val elements = listOf(
            KeyElement.literal("prefix"),
            KeyElement.resolvedTemplate("\${fileExpr}", "partFile:partKey"),
            KeyElement.literal("postfix.ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefixpartFile{17}:partKeypostfix{7}.ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
        assertEquals("prefix\${fileExpr}postfix.ROOT.Key4.Key5", parsed?.source)
    }

//filename:${key}                           / Key0.Key2.Key21       / filename{8}:Key0{6}.Key2{0}.Key21{0}
    @Test
    fun parseExpressionWithKeyInTemplate() {
        val elements = listOf(
            KeyElement.literal("filename:"),
            KeyElement.resolvedTemplate("\${key}", "Key0.Key2.Key21")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:Key0{6}.Key2{0}.Key21{0}", toTestString(parsed))
    }

//filename:${key}item                       / Key0.Key2.Key21.      / filename{8}:Key0{6}.Key2{0}.Key21{0}.item{4}
    @Test
    fun parseExpressionWithKeyInTemplate2() {
        val elements = listOf(
            KeyElement.literal("filename:"),
            KeyElement.resolvedTemplate("\${key}", "Key0.Key2.Key21."),
            KeyElement.literal("item")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:Key0{6}.Key2{0}.Key21{0}.item{4}", toTestString(parsed))
}

//filename:${key}.item                      / Key0.Key2.Key21       / filename{8}:Key0{6}.Key2{0}.Key21{0}.item{4}
    @Test
    fun parseExpressionWithKeyInTemplate3() {
        val elements = listOf(
            KeyElement.literal("filename:"),
            KeyElement.resolvedTemplate("\${key}", "Key0.Key2.Key21"),
            KeyElement.literal(".item")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:Key0{6}.Key2{0}.Key21{0}.item{4}", toTestString(parsed))
    }

//filename:${key}item                       / Key0.Key2.Key21       / filename{8}:Key0{6}.Key2{0}.Key21item{4}
    @Test
    fun parseExpressionWithKeyInTemplate4() {
        val elements = listOf(
            KeyElement.literal("filename:"),
            KeyElement.resolvedTemplate("\${key}", "Key0.Key2.Key21"),
            KeyElement.literal("item")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:Key0{6}.Key2{0}.Key21item{4}", toTestString(parsed))
    }

//filename:root.${key}                      / Key0.Key2.Key21       / filename{8}:root{4}.Key0{6}.Key2{0}.Key21{0}
    @Test
    fun partOfKeyIsExpression() {
        val elements = listOf(
            KeyElement.literal("filename:root."),
            KeyElement.resolvedTemplate("\${key}", "Key0.Key2.Key21")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:root{4}.Key0{6}.Key2{0}.Key21{0}", toTestString(parsed))
    }

//filename:root${key}                       / .Key0.Key2.Key21      / filename{8}:root{4}.Key0{6}.Key2{0}.Key21{0}
    @Test
    fun partOfKeyIsExpression2() {
        val elements = listOf(
            KeyElement.literal("filename:root"),
            KeyElement.resolvedTemplate("\${key}", ".Key0.Key2.Key21")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:root{4}.Key0{6}.Key2{0}.Key21{0}", toTestString(parsed))
    }

//filename:root${key}                       / Key0.Key2.Key21       / filename{8}:rootKey0{10}.Key2{0}.Key21{0}
    @Test
    fun partOfKeyIsExpression3() {
        val elements = listOf(
            KeyElement.literal("filename:root"),
            KeyElement.resolvedTemplate("\${key}", "Key0.Key2.Key21")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:rootKey0{10}.Key2{0}.Key21{0}", toTestString(parsed))
    }

//filename:root${key}Postfix                / .Key0                 / filename{8}:root{4}.Key0Postfix{13}
    @Test
    fun partOfKeyIsExpression4() {
        val elements = listOf(
                KeyElement.literal("filename:root"),
                KeyElement.resolvedTemplate("\${key}", ".Key0"),
                KeyElement.literal("Postfix")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:root{4}.Key0Postfix{13}", toTestString(parsed))
    }

//filename:root${key}.Postfix                / .Key0                / filename{8}:root{4}.Key0{6}.Postfix{7}
    @Test
    fun partOfKeyIsExpression5() {
        val elements = listOf(
                KeyElement.literal("filename:root"),
                KeyElement.resolvedTemplate("\${key}", ".Key0"),
                KeyElement.literal(".Postfix")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:root{4}.Key0{6}.Postfix{7}", toTestString(parsed))
    }

//filename:root${key}.Postfix.               / .Key0                / filename{8}:root{4}.Key0{6}.Postfix{7}.{0}
    @Test
    fun partOfKeyIsExpression6() {
        val elements = listOf(
                KeyElement.literal("filename:root"),
                KeyElement.resolvedTemplate("\${key}", ".Key0"),
                KeyElement.literal(".Postfix.")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:root{4}.Key0{6}.Postfix{7}.{0}", toTestString(parsed))
    }
}
