package com.eny.i18n.plugin.addons.technology.php

import com.eny.i18n.plugin.ide.actions.ExtractionTestBase
import com.eny.i18n.plugin.utils.generator.code.PhpGetTextCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.PoTranslationGenerator
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

//import org.junit.jupiter.api.Disabled
//import org.junit.jupiter.api.Test

@Ignore
class ExtractI18nPhpGettextTest: ExtractionTestBase() {
// @TODO 5

    private val tg = PoTranslationGenerator()
    private val cg = PhpGetTextCodeGenerator("gettext")

    @Test
    fun testStub1() {
        Assert.assertNotNull(tg.generateInvalidKey("test", "test"))
    }

//
//    @Test
//    fun testKeyExtraction() = myFixture.runWithConfig(config(tg.ext())) {
//        runTestCase(
//            "simple.${cg.ext()}",
//            cg.generateBlock("<caret>I want to move it to translation"),
//            cg.generate("'ref.avalue3'"),
//            "en-US/LC_MESSAGES/test.${tg.ext()}",
//            tg.generate(arrayOf("ref.section", "key", "Reference in json")),
//            tg.generate(arrayOf("ref.section", "key", "Reference in json"), arrayOf("avalue3", "I want to move it to translation")),
//            predefinedTextInputDialog("ref.avalue3")
//        )
//    }
//
//    @Test
//    fun testKeyExtractionSortedFirst() = myFixture.runWithConfig(config(tg.ext(), true)) {
//        runTestCase(
//            "simple.${cg.ext()}",
//            cg.generateBlock("<caret>I want to move it to translation"),
//            cg.generate("'ref.dvalue3'"),
//            "en-US/LC_MESSAGES/test.${tg.ext()}",
//            tg.generate(arrayOf("ref.section", "key", "Reference in json")),
//            tg.generate(arrayOf("ref.dvalue3", "I want to move it to translation"), arrayOf("section", "key", "Reference in json")),
//            predefinedTextInputDialog("ref.dvalue3")
//        )
//    }
//
//    @Test
//    fun testKeyExtractionSortedMiddle() = myFixture.runWithConfig(config(tg.ext(), true)) {
//        runTestCase(
//            "simple.${cg.ext()}",
//            cg.generateBlock("Mid<caret>dle!!!"),
//            cg.generate("'ref.mkey'"),
//            "en-US/LC_MESSAGES/test.${tg.ext()}",
//            tg.generate(arrayOf("ref.akey", "The first one"), arrayOf("zkey", "The last one")),
//            tg.generate(arrayOf("ref.akey", "The first one"), arrayOf("mkey", "Middle!!!"), arrayOf("zkey", "The last one")),
//            predefinedTextInputDialog("ref.mkey")
//        )
//    }
//
//    @Test
//    fun testDefNsKeyExtraction() = myFixture.runWithConfig(config(tg.ext())) {
//        runTestCase(
//            "simple.${cg.ext()}",
//            cg.generateBlock("<caret>I want to move it to translation"),
//            cg.generate("'ref.value3'"),
//            "en-US/LC_MESSAGES/translation.${tg.ext()}",
//            tg.generate(arrayOf("ref.section", "key", "Reference in json")),
//            tg.generate(arrayOf("ref.section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
//            predefinedTextInputDialog("ref.value3")
//        )
//    }
//
//    @Test
//    fun testRightBorderKeyExtraction() = myFixture.runWithConfig(config(tg.ext())) {
//        runTestCase(
//            "simple.${cg.ext()}",
//            cg.generateBlock("I want to move it to translation<caret>"),
//            cg.generate("'ref.value3'"),
//            "en-US/LC_MESSAGES/test.${tg.ext()}",
//            tg.generate(arrayOf("ref.section", "key", "Reference in json")),
//            tg.generate(arrayOf("ref.section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
//            predefinedTextInputDialog("ref.value3")
//        )
//    }
//
//    @Test
//    fun testRootSource() {
//        myFixture.runWithConfig(config(tg.ext())) {
//            runTestCase(
//                "simple.${cg.ext()}",
//                "I want to <caret>move it to translation",
//                "i18n.t<caret>('ref.value3')",
//                "en-US/LC_MESSAGES/test.${tg.ext()}",
//                tg.generate(arrayOf("ref.section", "key", "Reference in json")),
//                tg.generate(arrayOf("ref.section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
//                predefinedTextInputDialog("ref.value3")
//            )
//        }
//    }
}