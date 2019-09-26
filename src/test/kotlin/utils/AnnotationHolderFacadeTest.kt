package utils

import com.eny.i18n.plugin.utils.AnnotationHolderFacade
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.Literal
import com.intellij.openapi.util.TextRange
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

//@Ignore
class AnnotationHolderFacadeTest : TestBase {

    //console.log('sample:ROOT.Key1.key31');
    @Test
    fun measureTotallyResolvedKey() {
        val facade = AnnotationHolderFacade(null, TextRange(19, 43))
        val range = facade.compositeKeyFullBounds(FullKey(Literal("sample"), literalsList("ROOT", "Key1", "key31")))
        assertEquals(TextRange(27, 42), range)
    }

    //console.log('ROOT.Key1.key31');
    @Test
    fun measureTotallyResolvedKeyWithDefaultNS() {
        val facade = AnnotationHolderFacade(null, TextRange(19, 36))
        val range = facade.compositeKeyFullBounds(FullKey(null, literalsList("ROOT", "Key1", "key31")))
        assertEquals(TextRange(20, 35), range)
    }

    //console.log('sample:missing.whole.composite.key');
    @Test
    fun measureTotallyUnresolvedKey() {
        val facade = AnnotationHolderFacade(null, TextRange(19, 55))
        val range = facade.unresolvedKey(
            FullKey(Literal("sample"), literalsList("ROOT", "Key1", "key31")),
            listOf()
        )
        assertEquals(TextRange(27, 54), range)
    }

    //console.log(i18n.t('sample:ROOT.Key1.missingKey'));
    @Test
    fun measureUnresolvedKey() {
        val facade = AnnotationHolderFacade(null, TextRange(26, 55))
        val range = facade.unresolvedKey(
            FullKey(Literal("sample"), literalsList("ROOT", "Key1", "missingKey")),
            literalsList("ROOT", "Key1")
        )
        assertEquals(TextRange(44, 54), range)
    }

    //console.log(i18n.t('sample:ROOT.Key1.missing.Key'));
    @Test
    fun measureUnresolvedKey2() {
        val facade = AnnotationHolderFacade(null, TextRange(26, 56))
        val range = facade.unresolvedKey(
            FullKey(Literal("sample"), literalsList("ROOT", "Key1", "missing", "Key")),
            literalsList("ROOT", "Key1")
        )
        assertEquals(TextRange(44, 55), range)
    }

    //console.log(i18n.t('sample:ROOT.Key1.Key2.missing.Key'));
    @Test
    fun measureUnresolvedKey3() {
        val facade = AnnotationHolderFacade(null, TextRange(26, 61))
        val range = facade.unresolvedKey(
            FullKey(Literal("sample"), literalsList("ROOT", "Key1", "Key2", "missing", "Key")),
            literalsList("ROOT", "Key1", "Key2")
        )
        assertEquals(TextRange(49, 60), range)
    }

    //console.log('MissingFile:Ex.Sub.Val');
    @Test
    fun measureUnresolvedFile() {
        val facade = AnnotationHolderFacade(null, TextRange(19, 43))
        val range = facade.unresolvedNs(
            FullKey(Literal("MissingFile"), literalsList("Ex", "Sub", "Val"))
        )
        assertEquals(TextRange(20, 31), range)
    }

    //console.log(`${fileExpr}:ROOT.Key1.Key31`)                //"sample"
    @Test
    fun measureUnresolvedNsResolvedTemplate() {
        val facade = AnnotationHolderFacade(null, TextRange(19, 43))
        val range = facade.unresolvedNs(
            FullKey(Literal("sample", 11), literalsList("ROOT", "Key1", "Key31"))
        )
        assertEquals(TextRange(20, 31), range)
    }

    //console.log(`${fileExpr}:ROOT.Key1.Key31`)                //*
    @Test
    fun measureUnresolvedNsUnresolvedTemplate() {
        val facade = AnnotationHolderFacade(null, TextRange(19, 43))
        val range = facade.unresolvedNs(
            FullKey(Literal("*", 11), literalsList("ROOT", "Key1", "Key31"))
        )
        assertEquals(TextRange(20, 31), range)
    }

    //console.log('Ex.Sub.Val');
    @Test
    fun measureUnresolvedFileDefault() {
        val facade = AnnotationHolderFacade(null, TextRange(19, 43))
        val range = facade.unresolvedNs(
            FullKey(null, literalsList("Ex", "Sub", "Val"))
        )
        assertEquals(TextRange(20, 20), range)
    }

    //console.log(i18n.t(`sample:ROOT.${sub}.key31`));                           //'Key1.key31'
    @Test
    fun measureResolvedTemplateKey() {
        val facade = AnnotationHolderFacade(null, TextRange(26, 52))
        val range = facade.unresolvedKey(
            FullKey(
                Literal("sample"),
                listOf(
                    Literal("ROOT"),
                    Literal("Key1", 6),
                    Literal("key31", 0),
                    Literal("key31")
                )
            ),
            listOf(
                Literal("ROOT"),
                Literal("Key1", 6),
                Literal("key31", 0)
            )
        )
        assertEquals(TextRange(46, 51), range)
    }

    //console.log(i18n.t(`sample:ROOT.Key1.${unknownInCompileTime}`));
    @Test
    fun measureUnresolvedTemplateKey() {
        val facade = AnnotationHolderFacade(null, TextRange(26, 68))
        val range = facade.unresolvedKey(
            FullKey(
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
        val facade = AnnotationHolderFacade(null, TextRange(26, 52))
        val range = facade.unresolvedKey(
            FullKey(
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