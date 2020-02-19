package utils

import com.eny.i18n.plugin.utils.KeyElement
import org.junit.Test
import kotlin.test.assertEquals

internal class CheckDefaultNs(val elements: List<KeyElement>, val expected: String)

//@Ignore
internal class DefaultNsParserTest : TestBase {

    @Test
    fun checkParse() {
        listOf(
            //ROOT.Key2.Key3                            /                   / ROOT{4}.Key2{4}.Key3{4}
            CheckDefaultNs(
                listOf(KeyElement.literal("ROOT.Key2.Key3")),
                "ROOT{4}.Key2{4}.Key3{4}"
            ),
            //${fileExpa}.ROOT.Key1.Key31               / sample            / sampla{11}.ROOT{4}.Key1{4}.Key31{5}
            CheckDefaultNs(
                listOf(
                    KeyElement.resolvedTemplate("\${fileExpa}", "sampla"),
                    KeyElement.literal(".ROOT.Key1.Key31")
                ),
                "sampla{11}.ROOT{4}.Key1{4}.Key31{5}"
            ),
            //prefin${fileExpb}.ROOT.Key4.Key5          / sample            / prefinsamplb{17}.ROOT{4}.Key4{4}.Key5{4}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("prefin"),
                    KeyElement.resolvedTemplate("\${fileExpb}", "samplb"),
                    KeyElement.literal(".ROOT.Key4.Key5")
                ),
                "prefinsamplb{17}.ROOT{4}.Key4{4}.Key5{4}"
            ),
            //${fileExpr}postfix.ROOT.Key4.Key5         / sample            / samplepostfix{18}.ROOT{4}.Key4{4}.Key5{4}
            CheckDefaultNs(
                listOf(
                    KeyElement.resolvedTemplate("\${fileExpt}", "sampld"),
                    KeyElement.literal("postfix"),
                    KeyElement.literal(".ROOT.Key4.Key5")
                ),
                "sampldpostfix{18}.ROOT{4}.Key4{4}.Key5{4}"
            ),
            //prefie${fileExpr}postfix.ROOT.Key4.Key5   / sampee            / prefiesampeepostfix{24}.ROOT{4}.Key4{4}.Key5{4}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("prefie"),
                    KeyElement.resolvedTemplate("\${fileExpe}", "sampee"),
                    KeyElement.literal("postfix.ROOT.Key4.Key5")
                ),
                "prefiesampeepostfix{24}.ROOT{4}.Key4{4}.Key5{4}"
            ),
            //prefif${fileExpr}postfix.ROOT.Key4.Key5   / partFile.partKey  / prefifpartFile{17}.partKeypostfix{7}.ROOT{4}.Key4{4}.Key5{4}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("prefif"),
                    KeyElement.resolvedTemplate("\${fileExpr}", "partFile.partKey"),
                    KeyElement.literal("postfix.ROOT.Key4.Key5")
                ),
                "prefifpartFile{17}.partKeypostfix{7}.ROOT{4}.Key4{4}.Key5{4}"
            ),
            //roor.starr${kej}                          / *                     / roor{4}.starr*{11}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("roor.starr"),
                    KeyElement.unresolvedTemplate("\${kej}")
                ),
                "roor{4}.starr*{11}"
            ),
            //rooq.starq${kek}                          / .Key0.Key2.Key21      / root{4}.start{5}.Key0{6}.Key2{0}.Key21{0}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("rooq.starq"),
                    KeyElement.resolvedTemplate("\${kek}", ".Key0.Key2.Key21")
                ),
                "rooq{4}.starq{5}.Key0{6}.Key2{0}.Key21{0}"
            ),
            //root.start${kes}                          / Key0.Key2.Key21       / root{4}.startKey0{11}.Key2{0}.Key21{0}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("root.start"),
                    KeyElement.resolvedTemplate("\${kes}", "Key0.Key2.Key21")
                ),
                "root{4}.startKey0{11}.Key2{0}.Key21{0}"
            ),
            //filename:${ker}                           / *         filename{8}.*{6}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("filename."),
                    KeyElement.unresolvedTemplate("\${ker}")
                ),
                "filename{8}.*{6}"
            ),
            //filename.root.${ket}                      / *         filename{8}.root{4}.*{6}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("filename.root."),
                    KeyElement.unresolvedTemplate("\${ket}")
                ),
                "filename{8}.root{4}.*{6}"
            ),
            //filename.${kep}item                       / *         filename{8}.*item{10}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("filenama."),
                    KeyElement.unresolvedTemplate("\${kep}"),
                    KeyElement.literal("item")
                ),
                "filenama{8}.*item{10}"
            ),
            //filename.${keb}.item                      / *         filename{8}.*{6}.item{4}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("filenamb."),
                    KeyElement.unresolvedTemplate("\${keb}"),
                    KeyElement.literal(".item")
                ),
                "filenamb{8}.*{6}.item{4}"
            ),
            //filename.${key}item                       / Key0.Key2.Key21       / filename{8}.Key0{6}.Key2{0}.Key21item{4}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("filenamc."),
                    KeyElement.resolvedTemplate("\${key}", "Key0.Key2.Key21"),
                    KeyElement.literal("item")
                ),
                "filenamc{8}.Key0{6}.Key2{0}.Key21item{4}"
            )
        ).forEach {
            check -> assertEquals(check.expected, toTestString(parse(check.elements)))
        }
    }
}



