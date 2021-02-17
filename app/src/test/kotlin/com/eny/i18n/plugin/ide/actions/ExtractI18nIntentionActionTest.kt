package com.eny.i18n.plugin.ide.actions

//import com.eny.i18n.plugin.ide.JsonYamlCodeGenerators
import com.eny.i18n.plugin.ide.runCommonConfig
import com.eny.i18n.plugin.ide.settings.CommonSettings
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.junit.Test

fun noop (fixture: CodeInsightTestFixture, block: () -> Unit) = block()

class ExtractI18nIntentionActionJsTest: ExtractI18nIntentionActionTest (JsCodeGenerator(), JsonTranslationGenerator(), ::noop)
class ExtractI18nIntentionActionTsTest: ExtractI18nIntentionActionTest (TsCodeGenerator(), JsonTranslationGenerator(), ::noop)
class ExtractI18nIntentionActionJsxTest: ExtractI18nIntentionActionTest (JsxCodeGenerator(), JsonTranslationGenerator(), ::noop)
class ExtractI18nIntentionActionTsxTest: ExtractI18nIntentionActionTest (TsxCodeGenerator(), JsonTranslationGenerator(), ::noop)

abstract class ExtractI18nIntentionActionTest(
        private val cg: CodeGenerator,
        private val tg: TranslationGenerator,
        private val wrapper: (CodeInsightTestFixture, () -> Unit) -> Unit
): ExtractionTestBase() {

    @Test
    fun testKeyExtraction() = wrapper(myFixture) {
        runTestCase(
            "simple.${cg.ext()}",
            cg.generateBlock("<caret>I want to move it to translation"),
            cg.generate("'test:ref.avalue3'"),
            "assets/test.${tg.ext()}",
            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
            tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("avalue3", "I want to move it to translation")),
            predefinedTextInputDialog("test:ref.avalue3")
        )
    }

    @Test
    fun testKeyExtractionSortedFirst() = wrapper(myFixture) {
        myFixture.runCommonConfig(Pair(CommonSettings::extractSorted, true)) {
            runTestCase(
                "simple.${cg.ext()}",
                cg.generateBlock("<caret>I want to move it to translation"),
                cg.generate("'test:ref.dvalue3'"),
                "assets/test.${tg.ext()}",
                tg.generate("ref", arrayOf("section", "key", "Reference in json")),
                tg.generate("ref", arrayOf("dvalue3", "I want to move it to translation"), arrayOf("section", "key", "Reference in json")),
                predefinedTextInputDialog("test:ref.dvalue3")
            )
        }
    }

    @Test
    fun testKeyExtractionSortedMiddle() = wrapper(myFixture) {
        myFixture.runCommonConfig(Pair(CommonSettings::extractSorted, true)) {
            runTestCase(
                "simple.${cg.ext()}",
                cg.generateBlock("Mid<caret>dle!!!"),
                cg.generate("'test:ref.mkey'"),
                "assets/test.${tg.ext()}",
                tg.generate("ref", arrayOf("akey", "The first one"), arrayOf("zkey", "The last one")),
                tg.generate("ref", arrayOf("akey", "The first one"), arrayOf("mkey", "Middle!!!"), arrayOf("zkey", "The last one")),
                predefinedTextInputDialog("test:ref.mkey")
            )
        }
    }

    @Test
    fun testDefNsKeyExtraction() = wrapper(myFixture) {
        runTestCase(
            "simple.${cg.ext()}",
            cg.generateBlock("<caret>I want to move it to translation"),
            cg.generate("'ref.value3'"),
            "assets/translation.${tg.ext()}",
            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
            tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
            predefinedTextInputDialog("ref.value3")
        )
    }

    @Test
    fun testRightBorderKeyExtraction() = wrapper(myFixture) {
        runTestCase(
            "simple.${cg.ext()}",
            cg.generateBlock("I want to move it to translation<caret>"),
            cg.generate("'test:ref.value3'"),
            "assets/test.${tg.ext()}",
            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
            tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
            predefinedTextInputDialog("test:ref.value3")
        )
    }

    //TODO: fix broken test
    fun ignoreTestRootSource() = wrapper(myFixture) {
        runTestCase(
            "simple.${cg.ext()}",
            "I want to <caret>move it to translation",
            "i18n.t<caret>('test:ref.value3')",
            "assets/test.${tg.ext()}",
            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
            tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
            predefinedTextInputDialog("test:ref.value3")
        )
    }
}