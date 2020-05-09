package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class CheckDefaultNs(val elements: List<KeyElement>, val expected: String)

//@Ignore
internal class DefaultNsParserTest : ParserTestBase {

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
            assertEquals(it.expected, toTestString(parse(it.elements)))
        }
    }

    @Test
    fun checkParseSingleton() {
        listOf(
            CheckDefaultNs(
                listOf(
                    KeyElement.literal("we.are.sure."),
                    KeyElement.literal("that.current.key.has.no.namespace."),
                    KeyElement.literal("so.this.key.may.be.signleton")
                ),
                "we{2}.are{3}.sure{4}.that{4}.current{7}.key{3}.has{3}.no{2}.namespace{9}.so{2}.this{4}.key{3}.may{3}.be{2}.signleton{9}"
            ),
            CheckDefaultNs(
                listOf(KeyElement.literal("singleton")),
                "singleton{9}"
            )
        ).forEach {
            val fullKey = parse(it.elements, emptyNamespace = true)
            assertNotNull(fullKey)
            assertNull(fullKey.ns)
            assertEquals(it.expected, toTestString(fullKey))
        }
    }
}



