package utils

import com.eny.i18n.plugin.utils.KeyElement
import org.junit.Test
import kotlin.test.assertEquals

//@Ignore
internal class AsteriskKeyParserTest : TestBase {

//${fileExpa}:ROOT.Key1.Key31               / *         *{11}:ROOT{4}.Key1{4}.Key31{5}
    @Test
    fun parseExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.unresolvedTemplate("\${fileExpa}"),
            KeyElement.literal(":ROOT.Key1.Key31")
        )
        val parsed = parse(elements)
        assertEquals("*{11}:ROOT{4}.Key1{4}.Key31{5}", toTestString(parsed))
    }

//prefia${fileExpb}:ROOT.Key4.Key5          / *         prefia*{17}:ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parsePrefixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.literal("prefia"),
            KeyElement.unresolvedTemplate("\${fileExpb}"),
            KeyElement.literal(":ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefia*{17}:ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//${fileExpc}postfix:ROOT.Key4.Key5         / *         *postfix{18}:ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parsePostfixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.unresolvedTemplate("\${fileExpc}"),
            KeyElement.literal("postfix"),
            KeyElement.literal(":ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("*postfix{18}:ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//prefib${fileExpd}postfix:ROOT.Key4.Key5   / *         prefib*postfix{24}:ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parseMixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.literal("prefib"),
            KeyElement.unresolvedTemplate("\${fileExpd}"),
            KeyElement.literal("postfix"),
            KeyElement.literal(":ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefib*postfix{24}:ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//prefic${fileExpr}postfix.ROOT.Key4.Key5   / *         prefic*postfix{24}.ROOT{4}.Key4{4}.Key5{4} || prefix*:*postfix{24}.ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parseNsSeparatorInExpression() {
        val elements = listOf(
            KeyElement.literal("prefic"),
            KeyElement.unresolvedTemplate("\${fileExpr}"),
            KeyElement.literal("postfix"),
            KeyElement.literal(".ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefic*postfix{24}.ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//filename:${key}                           / *         filename{8}:*{6}
    @Test
    fun parseExpressionWithKeyInTemplate() {
        val elements = listOf(
            KeyElement.literal("filename:"),
            KeyElement.unresolvedTemplate("\${key}")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:*{6}", toTestString(parsed))
    }

//filename:${key}item                       / *         filename{8}:*item{10}
    @Test
    fun parseExpressionWithKeyInTemplate2() {
        val elements = listOf(
            KeyElement.literal("filename:"),
            KeyElement.unresolvedTemplate("\${key}"),
            KeyElement.literal("item")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:*item{10}", toTestString(parsed))
    }

//filename:${key}.item                      / *         filename{8}:*{6}.item{4}
    @Test
    fun parseExpressionWithKeyInTemplate3() {
        val elements = listOf(
            KeyElement.literal("filename:"),
            KeyElement.unresolvedTemplate("\${key}"),
            KeyElement.literal(".item")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:*{6}.item{4}", toTestString(parsed))
    }

//filename:root.${key}                      / *         filename{8}:root{4}.*{6}
    @Test
    fun partOfKeyIsExpression() {
        val elements = listOf(
            KeyElement.literal("filename:root."),
            KeyElement.unresolvedTemplate("\${key}")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:root{4}.*{6}", toTestString(parsed))
    }

//filename:root${key}                       / *         filename{8}:root*{10}
    @Test
    fun partOfKeyIsExpression2() {
        val elements = listOf(
            KeyElement.literal("filename:root"),
            KeyElement.unresolvedTemplate("\${key}")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}:root*{10}", toTestString(parsed))
    }
}