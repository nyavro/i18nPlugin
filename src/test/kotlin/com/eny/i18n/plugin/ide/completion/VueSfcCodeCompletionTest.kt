package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.completion.CompletionType
import org.junit.jupiter.api.Test

class VueSfcCodeCompletionTest: PlatformBaseTest() {

    val cg = VueCodeGenerator()
    val tg = JsonTranslationGenerator()

    private fun check(sourceName: String, sourceCode: String, expectedCode: String) {
        myFixture.runVue {
            myFixture.configureByText(sourceName, sourceCode)
            myFixture.complete(CompletionType.BASIC, 1)
            myFixture.checkResult(expectedCode)
        }
    }

    @Test
    fun testEmptyKeyCompletion() {
        myFixture.runVue {
            myFixture.configureByText(
                "none.vue",
                cg.generateSfc(
                    "\"<caret>\"",
                    mapOf(Pair("en", tg.generateContent("tstw", "fstt", "leu", "value")))
                )
            )
            assertTrue(myFixture.completeBasic().find {it.lookupString == "tstw"} != null)
        }
    }

    @Test
    fun testRootKeyCompletion() {
        myFixture.runVue {
            myFixture.configureByText(
                "none.vue",
                cg.generateSfc(
                    "\"tst<caret>\"",
                    mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value")))
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
                "\"none.base.<caret>\"",
                mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value")))
            ),
            cg.generateSfc(
                "\"none.base.\"",
                mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value")))
            )
        )
    }

    //Simple case - one possible completion of key: 'test:tst1.base.<caret>'
    @Test
    fun testSingle() {
        check(
            "single.vue",
            cg.generateSfc(
                "\"tst1.base.<caret>\"",
                mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value")))
            ),
            cg.generateSfc(
                "\"tst1.base.single\"",
                mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value")))
            )
        )
    }

    //Completion of plural key: 'test:tst2.plurals.<caret>'
    @Test
    fun testPlural() {
        check(
            "plural.vue",
            cg.generateSfc(
                "\"tst2.plurals.<caret>\"",
                mapOf(Pair("en", tg.generatePlural("tst2", "plurals", "value", "tt", "qq", "vv")))
            ),
            cg.generateSfc(
                "\"tst2.plurals.value\"",
                mapOf(Pair("en", tg.generatePlural("tst2", "plurals", "value", "tt", "qq", "vv")))
            )
        )
    }

    //Completion of partially typed key: 'test:tst1.base.si<caret>'
    @Test
    fun testPartial() {
        check(
            "partial.vue",
            cg.generateSfc(
                "\"tst1.base.si<caret>\"",
                mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value")))
            ),
            cg.generateSfc(
                "\"tst1.base.single\"",
                mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value")))
            )
        )
    }

    @Test
    fun testInvalidCompletion() = check(
        "invalid.vue",
        cg.generateSfc(
            "\"tst1.base.si<caret>ng\"",
            mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value")))
        ),
        cg.generateSfc(
            "\"tst1.base.sing\"",
            mapOf(Pair("en", tg.generateContent("tst1", "base", "single", "only one value")))
        )
    )
}
