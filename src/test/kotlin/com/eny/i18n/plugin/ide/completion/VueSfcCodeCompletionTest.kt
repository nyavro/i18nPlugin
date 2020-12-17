package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueSfcCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.completion.CompletionType
import org.junit.jupiter.api.Test

class VueSfcCodeCompletionTest: PlatformBaseTest() {

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
                VueSfcCodeGenerator(tg.generateNamedBlocks(Pair("en", tg.generateContent("tstw", "fstt", "leu", "value")))).generateBlock(
                    "message: {{ \$t(\"<caret>\") }}"
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
                VueSfcCodeGenerator(tg.generateNamedBlocks(Pair("en", tg.generateContent("tst1", "base", "single", "only one value")))).generateBlock(
                    "message: {{ \$t(\"tst<caret>\") }}"
                )
            )
            assertTrue(myFixture.completeBasic().find {it.lookupString == "tst1"} != null)
        }
    }

    //No completion happens
    @Test
    fun testNoCompletion() {
        val cg = VueSfcCodeGenerator(tg.generateNamedBlocks(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))))
        check(
            "none.vue",
            cg.generateBlock("message: {{ \$t(\"none.base.<caret>\") }}"),
            cg.generateBlock("message: {{ \$t(\"none.base.\") }}")
        )
    }

    //Simple case - one possible completion of key: 'test:tst1.base.<caret>'
    @Test
    fun testSingle() {
        val cg = VueSfcCodeGenerator(tg.generateNamedBlocks(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))))
        check(
            "single.vue",
            cg.generateBlock("message: {{ \$t(\"tst1.base.<caret>\") }}"),
            cg.generateBlock("message: {{ \$t(\"tst1.base.single\") }}")
        )
    }

    //Completion of plural key: 'test:tst2.plurals.<caret>'
    @Test
    fun testPlural() {
        val cg = VueSfcCodeGenerator(tg.generateNamedBlocks(Pair("en", tg.generatePlural("tst2", "plurals", "value", "tt", "qq", "vv"))))
        check(
            "plural.vue",
            cg.generateBlock("message: {{ \$t(\"tst2.plurals.<caret>\") }}"),
            cg.generateBlock("message: {{ \$t(\"tst2.plurals.value\") }}")
        )
    }

    //Completion of partially typed key: 'test:tst1.base.si<caret>'
    @Test
    fun testPartial() {
        val cg = VueSfcCodeGenerator(tg.generateNamedBlocks(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))))
        check(
            "partial.vue",
            cg.generateBlock("message: {{ \$t(\"tst1.base.si<caret>\") }}"),
            cg.generateBlock("message: {{ \$t(\"tst1.base.single\") }}")
        )
    }

    @Test
    fun testInvalidCompletion() {
        val cg = VueSfcCodeGenerator(tg.generateNamedBlocks(Pair("en", tg.generateContent("tst1", "base", "single", "only one value"))))
        return check(
            "invalid.vue",
            cg.generateBlock("message: {{ \$t(\"tst1.base.si<caret>ng\") }}"),
            cg.generateBlock("message: {{ \$t(\"tst1.base.sing\") }}")
        )
    }
}
