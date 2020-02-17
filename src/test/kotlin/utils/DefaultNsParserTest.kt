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

//${fileExpa}.ROOT.Key1.Key31               / sample            / sampla{11}.ROOT{4}.Key1{4}.Key31{5}
    @Test
    fun parseExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.resolvedTemplate("\${fileExpa}", "sampla"),
            KeyElement.literal(".ROOT.Key1.Key31")
        )
        val parsed = parse(elements)
        assertEquals("sampla{11}.ROOT{4}.Key1{4}.Key31{5}", toTestString(parsed))
    }

//prefin${fileExpb}.ROOT.Key4.Key5          / sample            / prefinsamplb{17}.ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parsePrefixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
                KeyElement.literal("prefin"),
                KeyElement.resolvedTemplate("\${fileExpb}", "samplb"),
                KeyElement.literal(".ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefinsamplb{17}.ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//${fileExpr}postfix.ROOT.Key4.Key5         / sample            / samplepostfix{18}.ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parsePostfixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.resolvedTemplate("\${fileExpt}", "sampld"),
            KeyElement.literal("postfix"),
            KeyElement.literal(".ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("sampldpostfix{18}.ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//prefie${fileExpr}postfix.ROOT.Key4.Key5   / sampee            / prefiesampeepostfix{24}.ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parseMixedExpressionWithFilePartInTemplate() {
        val elements = listOf(
            KeyElement.literal("prefie"),
            KeyElement.resolvedTemplate("\${fileExpe}", "sampee"),
            KeyElement.literal("postfix.ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefiesampeepostfix{24}.ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//prefif${fileExpr}postfix.ROOT.Key4.Key5   / partFile.partKey  / prefifpartFile{17}.partKeypostfix{7}.ROOT{4}.Key4{4}.Key5{4}
    @Test
    fun parseNsSeparatorInExpression() {
        val elements = listOf(
            KeyElement.literal("prefif"),
            KeyElement.resolvedTemplate("\${fileExpr}", "partFile.partKey"),
            KeyElement.literal("postfix.ROOT.Key4.Key5")
        )
        val parsed = parse(elements)
        assertEquals("prefifpartFile{17}.partKeypostfix{7}.ROOT{4}.Key4{4}.Key5{4}", toTestString(parsed))
    }

//roor.starr${kej}                          / *                     / roor{4}.starr*{11}
    @Test
    fun parseKeyInExpression() {
        val elements = listOf(
            KeyElement.literal("roor.starr"),
            KeyElement.unresolvedTemplate("\${kej}")
        )
        val parsed = parse(elements)
        assertEquals("roor{4}.starr*{11}", toTestString(parsed))
    }

//rooq.starq${kek}                          / .Key0.Key2.Key21      / root{4}.start{5}.Key0{6}.Key2{0}.Key21{0}
    @Test
    fun parseKeyInExpression2() {
        val elements = listOf(
            KeyElement.literal("rooq.starq"),
            KeyElement.resolvedTemplate("\${kek}", ".Key0.Key2.Key21")
        )
        val parsed = parse(elements)
        assertEquals("rooq{4}.starq{5}.Key0{6}.Key2{0}.Key21{0}", toTestString(parsed))
    }

//root.start${kes}                          / Key0.Key2.Key21       / root{4}.startKey0{11}.Key2{0}.Key21{0}
    @Test
    fun parseKeyInExpression3() {
        val elements = listOf(
            KeyElement.literal("root.start"),
            KeyElement.resolvedTemplate("\${kes}", "Key0.Key2.Key21")
        )
        val parsed = parse(elements)
        assertEquals("root{4}.startKey0{11}.Key2{0}.Key21{0}", toTestString(parsed))
    }

//filename:${ker}                           / *         filename{8}.*{6}
    @Test
    fun parseExpressionWithKeyInTemplate() {
        val elements = listOf(
                KeyElement.literal("filename."),
                KeyElement.unresolvedTemplate("\${ker}")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}.*{6}", toTestString(parsed))
    }

//filename.root.${ket}                      / *         filename{8}.root{4}.*{6}
    @Test
    fun partOfKeyIsExpression() {
        val elements = listOf(
                KeyElement.literal("filename.root."),
                KeyElement.unresolvedTemplate("\${ket}")
        )
        val parsed = parse(elements)
        assertEquals("filename{8}.root{4}.*{6}", toTestString(parsed))
    }

//filename.${kep}item                       / *         filename{8}.*item{10}
    @Test
    fun parseExpressionWithKeyInTemplate2() {
        val elements = listOf(
                KeyElement.literal("filenama."),
                KeyElement.unresolvedTemplate("\${kep}"),
                KeyElement.literal("item")
        )
        val parsed = parse(elements)
        assertEquals("filenama{8}.*item{10}", toTestString(parsed))
    }

//filename.${keb}.item                      / *         filename{8}.*{6}.item{4}
    @Test
    fun parseExpressionWithKeyInTemplate3() {
        val elements = listOf(
                KeyElement.literal("filenamb."),
                KeyElement.unresolvedTemplate("\${keb}"),
                KeyElement.literal(".item")
        )
        val parsed = parse(elements)
        assertEquals("filenamb{8}.*{6}.item{4}", toTestString(parsed))
    }

//filename.${key}item                       / Key0.Key2.Key21       / filename{8}.Key0{6}.Key2{0}.Key21item{4}
    @Test
    fun parseExpressionWithKeyInTemplate4() {
        val elements = listOf(
                KeyElement.literal("filenamc."),
                KeyElement.resolvedTemplate("\${key}", "Key0.Key2.Key21"),
                KeyElement.literal("item")
        )
        val parsed = parse(elements)
        assertEquals("filenamc{8}.Key0{6}.Key2{0}.Key21item{4}", toTestString(parsed))
    }
}



