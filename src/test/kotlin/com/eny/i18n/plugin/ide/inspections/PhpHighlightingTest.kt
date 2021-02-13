package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.utils.generator.code.PhpDoubleQuoteCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.PhpSingleQuoteCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import org.junit.Test

class PhpHighlightingTest: PlatformBaseTest() {

    private val cgs = listOf(PhpSingleQuoteCodeGenerator(), PhpDoubleQuoteCodeGenerator())
    private val tg = JsonTranslationGenerator()
    @Test
    fun testDefNsUnresolved() {
        cgs.forEachIndexed { index, cg ->
            myFixture.customCheck(
                    "defNsUnresolved.${cg.ext()}",
                    cg.multiGenerate(
                            "\"<warning descr=\"Missing default translation file\">missing.default.translation</warning>\"",
                            "'<warning descr=\"Missing default translation file\">missing.default.in.translation</warning>'"
                    ),
                    "assets/test${index}.${tg.ext()}",
                    tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
            )
        }
    }

    @Test
    fun testUnresolvedKey() {
        cgs.forEachIndexed { i, cg ->
            myFixture.customCheck(
                    "unresolvedKey.${cg.ext()}",
                    cg.multiGenerate(
                            "\"test${i}:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key</warning>\"",
                            "\"test${i}:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\"",
                            "'test${i}:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key</warning>'",
                            "'test${i}:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>'"
                    ),
                    "test${i}.${tg.ext()}",
                    tg.generateContent("tst1", "base", "single", "only one value")
            )
        }
    }

    @Test
    fun testUnresolvedNs() {
        cgs.forEachIndexed { i, cg ->
            myFixture.customCheck(
                    "unresolvdNs.${cg.ext()}",
                    cg.multiGenerate(
                            "\"<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base\"",
                            "'<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base'"
                    ),
                    "test${i}.${tg.ext()}",
                    tg.generateContent("root", "first", "key", "value")
            )
        }
    }
}