package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.utils.generator.code.JsCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.JsxCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.TsCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.TsxCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import org.junit.Test

class JsDialectCodeHighlightingTest: PlatformBaseTest() {
    val cgs = listOf(JsCodeGenerator(), TsCodeGenerator(), JsxCodeGenerator(), TsxCodeGenerator())
    val tg = JsonTranslationGenerator()

    @Test
    fun testDefNsUnresolved() {
        myFixture.addFileToProject(
            "assets/test.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
        cgs.forEachIndexed { i, cg ->
            myFixture.customCheck(
                "defNsUnresolved.${cg.ext()}",
                cg.multiGenerate(
                    "\"<warning descr=\"Missing default translation file\">missing.default.translation</warning>\"",
                    "`<warning descr=\"Missing default translation file\">missing.default.in.\${template}</warning>`"
                )
            )
        }
    }

    @Test
    fun testUnresolvedKey() {
        myFixture.addFileToProject(
            "test.${tg.ext()}",
            tg.generateContent("tst1", "base", "single", "only one value")
        )
        cgs.forEachIndexed { i, cg ->
            myFixture.customCheck(
                "unresolvedKey.${cg.ext()}",
                cg.multiGenerate(
                    "\"test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key</warning>\"",
                    "\"test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\"",
                    "`test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key.\${arg}</warning>`",
                    "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${arg}</warning>`",
                    "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${arg}</warning>`",
                    "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${b ? 'key' : 'key2'}</warning>`",
                    "`test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.\${b ? 'key' : 'key2'}</warning>`"
                )
            )
        }
    }

    @Test
    fun testUnresolvedNs() {
        myFixture.addFileToProject(
            "test.${tg.ext()}",
            tg.generateContent("root", "first", "key", "value")
        )
        cgs.forEachIndexed { i, cg ->
            myFixture.customCheck(
                "unresolvdNs.${cg.ext()}",
                cg.multiGenerate(
                    "\"<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base\"",
                    "`<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base.\${arg}`"
                )
            )
        }
    }

    @Test
    fun testResolvedTemplate() {
        myFixture.addFileToProject(
            "assets/translation.${tg.ext()}",
            tg.generateContent("tst1", "base", "value", "translation")
        )
        cgs.forEachIndexed { i, cg ->
            myFixture.customCheck(
                "resolvedTemplate.${cg.ext()}",
                cg.generate("`test:tst1.base.\${arg}`")
            )
        }
    }
}