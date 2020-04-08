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
            //roor.starr${kej}                          / *                     / roor{4}.starr*{11}
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("roor.starr"),
                    KeyElement.unresolvedTemplate("\${kej}")
                ),
                "roor{4}.starr*{11}"
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
            )
        ).forEach {
            check -> assertEquals(check.expected, toTestString(parse(check.elements)))
        }
    }
}



