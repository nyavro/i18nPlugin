package utils

import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.Literal
import com.intellij.openapi.util.TextRange
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertTrue

internal class Check(
    val elementRange: TextRange,
    val key: FullKey,
    val expected: TextRange,
    val resolved: List<Literal> = listOf(),
    val isQuoted: Boolean = true
)

//@Ignore
internal class AnnotationHolderFacadeTest : TestBase {

    @Test
    fun measureTotallyResolvedKey() {
        val resolvedChecks = listOf(
            Check(
                TextRange(19, 43),
                FullKey("sampla:ROOA.Kea1.kea31", Literal("sampla"), literalsList("ROOA", "Kea1", "kea31")),
                TextRange(27, 42)
            ),
            Check(
                TextRange(19, 36),
                FullKey("ROOB.Keb1.keb31",null, literalsList("ROOB", "Keb1", "keb31")),
                TextRange(20, 35)
            ),
            Check(
                TextRange(19, 55),
                FullKey(
                    "samplc:missinс.wholс.compositс.keс", Literal("samplc"), literalsList("missinс", "wholс", "compositс", "keс")
                ),
                TextRange(27, 54)
            ),
            Check(
                TextRange(19, 36),
                FullKey("ROOL.Kel1.kel31",null, literalsList("ROOL", "Kel1", "kel31")),
                TextRange(19, 34),
                listOf(),
                false
            )
        )
        resolvedChecks.forEach {
            check -> assertEquals(check.expected, facade(check.elementRange, check.isQuoted).compositeKeyFullBounds(check.key))
        }
    }

    @Test
    fun measureUnresolvedKey() {
        listOf(
            Check(
                TextRange(26, 55),
                FullKey("sampld:ROOD.Ked1.missingKed", Literal("sampld"), literalsList("ROOD", "Ked1", "missingKed")),
                TextRange(44, 54),
                literalsList("ROOD", "Ked1")
            ),
            Check(
                TextRange(26, 56),
                FullKey("samplf:ROOF.Kef1.missinf.Kef", Literal("samplf"), literalsList("ROOF", "Kef1", "missinf", "Kef")),
                TextRange(44, 55),
                literalsList("ROOF", "Kef1")
            ),
            Check(
                TextRange(26, 61),
                FullKey("samplt:ROTT.Ket1.Ket2.missint.Ket", Literal("samplt"), literalsList("ROTT", "Ket1", "Ket2", "missint", "Ket")),
                TextRange(49, 60),
                literalsList("ROTT", "Ket1", "Ket2")
            ),
            Check(
                TextRange(26, 52),
                FullKey(
                    "samplg:ROOG.\${sub}.keg31",
                    Literal("samplg"),
                    listOf(Literal("ROOG"), Literal("Keg1", 6), Literal("keg31", 0), Literal("keg41"))
                ),
                TextRange(46, 51),
                listOf(Literal("ROOG"), Literal("Keg1", 6), Literal("keg31", 0))
            ),
            Check(
                TextRange(26, 68),
                FullKey(
                    "sample:ROOT.Key1.\${unknownInCompileTime}",
                    Literal("sample"),
                    listOf(Literal("ROOT"), Literal("Key1"), Literal("*", 23))
                ),
                TextRange(44, 67),
                listOf(Literal("ROOT"), Literal("Key1"))
            ),
            Check(
                TextRange(26, 56),
                FullKey("samplm:ROOM.Kem1.missinm.Kem", Literal("samplm"), literalsList("ROOM", "Kem1", "missinm", "Kem")),
                TextRange(43, 54),
                literalsList("ROOM", "Kem1"),
                false
            )
        ).forEach {
            check -> assertEquals(check.expected, facade(check.elementRange, check.isQuoted).unresolvedKey(check.key, check.resolved))
        }
    }

    @Test
    fun measureUnresolvedTemplateKey() {
        listOf(
            Check(
                TextRange(26, 52),
                FullKey(
                    "sample:ROOT.\${sub}.key31",
                    Literal("sample"),
                    listOf(
                        Literal("ROOT"), Literal("Key1", 6), Literal("key3", 0), Literal("key31")
                    )
                ),
                TextRange.EMPTY_RANGE,
                listOf(Literal("ROOT"), Literal("Key1", 6), Literal("key31"))
            )
        ).forEach {
            check -> assertTrue(facade(check.elementRange).unresolvedKey(check.key, check.resolved).isEmpty)
        }
    }

    @Test
    fun measureUnresolvedFile() {
        listOf(
            Check(
                TextRange(19, 43),
                FullKey("MissingFile:Ex.Sub.Val", Literal("MissingFile"), literalsList("Ex", "Sub", "Val")),
                TextRange(20, 31)
            ),
            Check(
                TextRange(19, 43),
                FullKey("\${fileExpr}:ROOT.Key1.Key31", Literal("sample", 11), literalsList("ROOT", "Key1", "Key31")),
                TextRange(20, 31)
            ),
            Check(
                TextRange(19, 43),
                FullKey("\${fileExpr}:ROOT.Key1.Key31", Literal("*", 11), literalsList("ROOT", "Key1", "Key31")),
                TextRange(20, 31)
            ),
            Check(
                TextRange(19, 43),
                FullKey("Ex.Sub.Val",null, literalsList("Ex", "Sub", "Val")),
                TextRange(20, 20)
            ),
            Check(
                TextRange(19, 43),
                FullKey("MissingFilt:Ez.Suz.Vaz", Literal("MissingFilt"), literalsList("Ez", "Suz", "Vaz")),
                TextRange(19, 30),
                listOf(),
                false
            )
        ).forEach {
            check -> assertEquals(check.expected, facade(check.elementRange, check.isQuoted).unresolvedNs(check.key))
        }
    }
}