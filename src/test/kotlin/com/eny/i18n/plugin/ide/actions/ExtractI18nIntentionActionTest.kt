package com.eny.i18n.plugin.ide.actions

//import com.eny.i18n.plugin.ide.JsonYamlCodeGenerators
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import org.junit.Assert
import org.junit.Test

//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.ArgumentsSource

class ExtractI18nIntentionActionTest: ExtractionTestBase() {

    private val cgs = listOf(JsCodeGenerator(), TsCodeGenerator(), JsxCodeGenerator(), TsxCodeGenerator(), PhpSingleQuoteCodeGenerator(), PhpDoubleQuoteCodeGenerator())

    @Test
    fun testStub1() {
        Assert.assertTrue(true)
    }

//    @Test
//    fun testKeyExtraction(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext())) {
//        runTestCase(
//            "simple.${cg.ext()}",
//            cg.generateBlock("<caret>I want to move it to translation"),
//            cg.generate("'test:ref.avalue3'"),
//            "assets/test.${tg.ext()}",
//            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
//            tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("avalue3", "I want to move it to translation")),
//            predefinedTextInputDialog("test:ref.avalue3")
//        )
//    }

//    @ParameterizedTest
//    @ArgumentsSource(JsonYamlCodeGenerators::class)
//    fun testKeyExtractionSortedFirst(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext(), true)) {
//        runTestCase(
//            "simple.${cg.ext()}",
//            cg.generateBlock("<caret>I want to move it to translation"),
//            cg.generate("'test:ref.dvalue3'"),
//            "assets/test.${tg.ext()}",
//            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
//            tg.generate("ref", arrayOf("dvalue3", "I want to move it to translation"), arrayOf("section", "key", "Reference in json")),
//            predefinedTextInputDialog("test:ref.dvalue3")
//        )
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(JsonYamlCodeGenerators::class)
//    fun testKeyExtractionSortedMiddle(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext(), true)) {
//        runTestCase(
//            "simple.${cg.ext()}",
//            cg.generateBlock("Mid<caret>dle!!!"),
//            cg.generate("'test:ref.mkey'"),
//            "assets/test.${tg.ext()}",
//            tg.generate("ref", arrayOf("akey", "The first one"), arrayOf("zkey", "The last one")),
//            tg.generate("ref", arrayOf("akey", "The first one"), arrayOf("mkey", "Middle!!!"), arrayOf("zkey", "The last one")),
//            predefinedTextInputDialog("test:ref.mkey")
//        )
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(JsonYamlCodeGenerators::class)
//    fun testDefNsKeyExtraction(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext())) {
//        runTestCase(
//            "simple.${cg.ext()}",
//            cg.generateBlock("<caret>I want to move it to translation"),
//            cg.generate("'ref.value3'"),
//            "assets/translation.${tg.ext()}",
//            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
//            tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
//            predefinedTextInputDialog("ref.value3")
//        )
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(JsonYamlCodeGenerators::class)
//    fun testRightBorderKeyExtraction(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext())) {
//        runTestCase(
//            "simple.${cg.ext()}",
//            cg.generateBlock("I want to move it to translation<caret>"),
//            cg.generate("'test:ref.value3'"),
//            "assets/test.${tg.ext()}",
//            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
//            tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
//            predefinedTextInputDialog("test:ref.value3")
//        )
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(JsonYamlCodeGenerators::class)
//    fun testRootSource(cg: CodeGenerator, tg: TranslationGenerator) {
//        myFixture.runWithConfig(config(tg.ext())) {
//            runTestCase(
//                "simple.${cg.ext()}",
//                "I want to <caret>move it to translation",
//                "i18n.t<caret>('test:ref.value3')",
//                "assets/test.${tg.ext()}",
//                tg.generate("ref", arrayOf("section", "key", "Reference in json")),
//                tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
//                predefinedTextInputDialog("test:ref.value3")
//            )
//        }
//    }
}