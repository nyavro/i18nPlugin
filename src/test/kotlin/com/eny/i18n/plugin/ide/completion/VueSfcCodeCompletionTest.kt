package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.VueSettings
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.completion.CompletionType
import org.junit.jupiter.api.Test

class VueSfcCodeCompletionTest: PlatformBaseTest() {

    val cg = VueCodeGenerator()
    val tg = JsonTranslationGenerator()

    private fun check(sourceName: String, sourceCode: String, expectedCode: String) {
        myFixture.runVueConfig(Pair(VueSettings::vue, true)) {
            myFixture.configureByText(sourceName, sourceCode)
            myFixture.complete(CompletionType.BASIC, 1)
            myFixture.checkResult(expectedCode)
        }
    }

    @Test
    fun testEmptyKeyCompletion() {
        myFixture.runVueConfig(Pair(VueSettings::vue, true)) {
            myFixture.configureByText(
                "none.vue",
                cg.generateSfc(
                        mapOf(Pair("en", tg.generateContent("tstw", "fstt", "leu", "value"))),
                        "\"<caret>\""
                )
            )
            assertTrue(myFixture.completeBasic().find {it.lookupString == "tstw"} != null)
        }
    }

    @Test
    fun testRootKeyCompletion() {
        myFixture.runVueConfig(Pair(VueSettings::vue, true)) {
            myFixture.configureByText(
                "none.vue",
                cg.generateSfc(
                        mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))),
                        "\"tst<caret>\""
                )
            )
            assertTrue(myFixture.completeBasic().find {it.lookupString == "tst1"} != null)
        }
    }

    //No completion happens
    @Test
    fun testNoCompletion() {
        check(
            "none.vue",
            cg.generateSfc(
                    mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))),
                    "\"none.base.<caret>\""
            ),
            cg.generateSfc(
                    mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))),
                    "\"none.base.\""
            )
        )
    }

    //Simple case - one possible completion of key: 'test:tst1.base.<caret>'
    @Test
    fun testSingle() {
        check(
            "single.vue",
            cg.generateSfc(
                    mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))),
                    "\"tst1.base.<caret>\""
            ),
            cg.generateSfc(
                    mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))),
                    "\"tst1.base.single\""
            )
        )
    }

    //Completion of plural key: 'test:tst2.plurals.<caret>'
    @Test
    fun testPlural() {
        check(
            "plural.vue",
            cg.generateSfc(
                    mapOf(Pair("en", tg.generatePlural("tst2", "plurals", "value", "tt", "qq", "vv"))),
                    "\"tst2.plurals.<caret>\""
            ),
            cg.generateSfc(
                    mapOf(Pair("en", tg.generatePlural("tst2", "plurals", "value", "tt", "qq", "vv"))),
                    "\"tst2.plurals.value\""
            )
        )
    }

    //Completion of partially typed key: 'test:tst1.base.si<caret>'
    @Test
    fun testPartial() {
        check(
            "partial.vue",
            cg.generateSfc(
                    mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))),
                    "\"tst1.base.si<caret>\""
            ),
            cg.generateSfc(
                    mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))),
                    "\"tst1.base.single\""
            )
        )
    }

    @Test
    fun testInvalidCompletion() = check(
        "invalid.vue",
        cg.generateSfc(
                mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))),
                "\"tst1.base.si<caret>ng\""
        ),
        cg.generateSfc(
                mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))),
                "\"tst1.base.sing\""
        )
    )
}
