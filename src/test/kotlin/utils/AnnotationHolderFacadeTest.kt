package utils

import com.eny.i18n.plugin.utils.AnnotationHolderFacade
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.Literal
import com.intellij.openapi.util.TextRange
import org.junit.Assert.assertEquals
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
        val range = facade.compositeKeyFullBounds(FullKey(Literal("sample"), literalsList("ROOT", "Key1", "key31")))
        assertEquals(TextRange(27, 54), range)
    }

    //console.log(i18n.t('sample:ROOT.Key1.missingKey'));
    @Test
    fun measureUnresolvedKey() {
        val facade = AnnotationHolderFacade(null, TextRange(26, 55))
        val range = facade.unresolved(
            FullKey(Literal("sample"), literalsList("ROOT", "Key1", "missingKey")),
            literalsList("ROOT", "Key1")
        )
        assertEquals(TextRange(44, 54), range)
    }

    //console.log(i18n.t('sample:ROOT.Key1.missing.Key'));
    @Test
    fun measureUnresolvedKey2() {
        val facade = AnnotationHolderFacade(null, TextRange(26, 56))
        val range = facade.unresolved(
            FullKey(Literal("sample"), literalsList("ROOT", "Key1", "missing", "Key")),
            literalsList("ROOT", "Key1")
        )
        assertEquals(TextRange(44, 55), range)
    }

    //console.log(i18n.t('sample:ROOT.Key1.Key2.missing.Key'));
    @Test
    fun measureUnresolvedKey3() {
        val facade = AnnotationHolderFacade(null, TextRange(26, 61))
        val range = facade.unresolved(
            FullKey(Literal("sample"), literalsList("ROOT", "Key1", "Key2", "missing", "Key")),
            literalsList("ROOT", "Key1", "Key2")
        )
        assertEquals(TextRange(49, 60), range)
    }
}




//console.log('MissingFile:Ex.Sub.Val');                                          //Unresolved File
//console.log(i18n.t('sample:ROOT.Key1'));                                   //Reference to Json object
//console.log(i18n.t('sample:ROOT.Key1.plurals.value', {count: 2})); //Reference to plural key
//const sub0 = 'Key1.key31';
//const sub = sub0;
//console.log(i18n.t(`sample:ROOT.${sub}.key31`));                           //Resolved simple expression
//console.log(i18n.t(`sample:ROOT.${sub}.key314`));                          //Unresolved simple expression
//console.log(i18n.t(`sample:ROOT.Key1.${unknownInCompileTime}`));           //Goto to json object
//const fileExpr = "sample";
//console.log(`${fileExpr}:ROOT.Key1.Key31`)