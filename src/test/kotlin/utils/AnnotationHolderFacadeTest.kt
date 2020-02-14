package utils

import com.eny.i18n.plugin.utils.AnnotationHolderFacade
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.Literal
import com.intellij.openapi.util.TextRange
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

//@Ignore
internal class AnnotationHolderFacadeTest : TestBase {

    //console.log('sampla:ROOA.Kea1.kea31');
    @Test
    fun measureTotallyResolvedKey() {
        val facade = AnnotationHolderFacade(TextRange(19, 43))
        val range = facade.compositeKeyFullBounds(
            FullKey("sampla:ROOA.Kea1.kea31", Literal("sampla"), literalsList("ROOA", "Kea1", "kea31"))
        )
        assertEquals(TextRange(27, 42), range)
    }

    //console.log('ROOB.Keb1.keb31');
    @Test
    fun measureTotallyResolvedKeyWithDefaultNS() {
        val facade = AnnotationHolderFacade(TextRange(19, 36))
        val range = facade.compositeKeyFullBounds(
            FullKey("ROOB.Keb1.keb31",null, literalsList("ROOB", "Keb1", "keb31"))
        )
        assertEquals(TextRange(20, 35), range)
    }

    //console.log('samplc:missinс.wholс.compositс.keс');
    @Test
    fun measureTotallyUnresolvedKey() {
        val facade = AnnotationHolderFacade(TextRange(19, 55))
        val range = facade.unresolvedKey(
            FullKey(
                "samplc:missinс.wholс.compositс.keс", Literal("samplc"), literalsList("missinс", "wholс", "compositс", "keс")
            ),
            listOf()
        )
        assertEquals(TextRange(27, 54), range)
    }

    //console.log(i18n.t('sampld:ROOD.Ked1.missingKed'));
    @Test
    fun measureUnresolvedKey() {
        val facade = AnnotationHolderFacade(TextRange(26, 55))
        val range = facade.unresolvedKey(
            FullKey("sampld:ROOD.Ked1.missingKed", Literal("sampld"), literalsList("ROOD", "Ked1", "missingKed")),
            literalsList("ROOD", "Ked1")
        )
        assertEquals(TextRange(44, 54), range)
    }

    //console.log(i18n.t('samplf:ROOT.Key1.missing.Key'));
    @Test
    fun measureUnresolvedKey2() {
        val facade = AnnotationHolderFacade(TextRange(26, 56))
        val range = facade.unresolvedKey(
            FullKey("samplf:ROOF.Kef1.missinf.Kef", Literal("samplf"), literalsList("ROOF", "Kef1", "missinf", "Kef")),
            literalsList("ROOF", "Kef1")
        )
        assertEquals(TextRange(44, 55), range)
    }

    //console.log(i18n.t('samplt:ROTT.Ket1.Ket2.missint.Ket'));
    @Test
    fun measureUnresolvedKey3() {
        val facade = AnnotationHolderFacade(TextRange(26, 61))
        val range = facade.unresolvedKey(
            FullKey("samplt:ROTT.Ket1.Ket2.missint.Ket", Literal("samplt"), literalsList("ROTT", "Ket1", "Ket2", "missint", "Ket")),
            literalsList("ROTT", "Ket1", "Ket2")
        )
        assertEquals(TextRange(49, 60), range)
    }

    //console.log('MissingFile:Ex.Sub.Val');
    @Test
    fun measureUnresolvedFile() {
        val facade = AnnotationHolderFacade(TextRange(19, 43))
        val range = facade.unresolvedNs(
            FullKey("MissingFile:Ex.Sub.Val", Literal("MissingFile"), literalsList("Ex", "Sub", "Val"))
        )
        assertEquals(TextRange(20, 31), range)
    }

    //console.log(`${fileExpr}:ROOT.Key1.Key31`)                //"sample"
    @Test
    fun measureUnresolvedNsResolvedTemplate() {
        val facade = AnnotationHolderFacade(TextRange(19, 43))
        val range = facade.unresolvedNs(
            FullKey("\${fileExpr}:ROOT.Key1.Key31", Literal("sample", 11), literalsList("ROOT", "Key1", "Key31"))
        )
        assertEquals(TextRange(20, 31), range)
    }

    //console.log(`${fileExpr}:ROOT.Key1.Key31`)                //*
    @Test
    fun measureUnresolvedNsUnresolvedTemplate() {
        val facade = AnnotationHolderFacade(TextRange(19, 43))
        val range = facade.unresolvedNs(
            FullKey("\${fileExpr}:ROOT.Key1.Key31", Literal("*", 11), literalsList("ROOT", "Key1", "Key31"))
        )
        assertEquals(TextRange(20, 31), range)
    }

    //console.log('Ex.Sub.Val');
    @Test
    fun measureUnresolvedFileDefault() {
        val facade = AnnotationHolderFacade(TextRange(19, 43))
        val range = facade.unresolvedNs(
            FullKey("Ex.Sub.Val",null, literalsList("Ex", "Sub", "Val"))
        )
        assertEquals(TextRange(20, 20), range)
    }

    //console.log(i18n.t(`samplg:ROOG.${sub}.keg31`));                           //'Keg1.keg31'
    @Test
    fun measureResolvedTemplateKey() {
        val facade = AnnotationHolderFacade(TextRange(26, 52))
        val range = facade.unresolvedKey(
            FullKey(
                "samplg:ROOG.\${sub}.keg31",
                Literal("samplg"),
                listOf(
                    Literal("ROOG"),
                    Literal("Keg1", 6),
                    Literal("keg31", 0),
                    Literal("keg41")
                )
            ),
            listOf(
                Literal("ROOG"),
                Literal("Keg1", 6),
                Literal("keg31", 0)
            )
        )
        assertEquals(TextRange(46, 51), range)
    }

    //console.log(i18n.t(`sample:ROOT.Key1.${unknownInCompileTime}`));
    @Test
    fun measureUnresolvedTemplateKey() {
        val facade = AnnotationHolderFacade(TextRange(26, 68))
        val range = facade.unresolvedKey(
            FullKey(
                "sample:ROOT.Key1.\${unknownInCompileTime}",
                Literal("sample"),
                listOf(
                    Literal("ROOT"),
                    Literal("Key1"),
                    Literal("*", 23)
                )
            ),
            listOf(
                Literal("ROOT"),
                Literal("Key1")
            )
        )
        assertEquals(TextRange(44, 67), range)
    }

    //console.log(i18n.t(`sample:ROOT.${sub}.key31`));  //'Key1.key3'
    @Test
    fun measureUnresolvedTemplateKey2() {
        val facade = AnnotationHolderFacade(TextRange(26, 52))
        val range = facade.unresolvedKey(
            FullKey(
                "sample:ROOT.\${sub}.key31",
                Literal("sample"),
                listOf(
                    Literal("ROOT"),
                    Literal("Key1", 6),
                    Literal("key3", 0),
                    Literal("key31")
                )
            ),
            listOf(
                Literal("ROOT"),
                Literal("Key1", 6),
                Literal("key31")
            )
        )
        assertTrue(range.isEmpty)
    }
}