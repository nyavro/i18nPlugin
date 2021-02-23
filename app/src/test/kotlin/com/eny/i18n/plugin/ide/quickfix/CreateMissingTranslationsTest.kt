package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runCommonConfig
import com.eny.i18n.plugin.ide.annotator.CommonSettings
import com.eny.i18n.plugin.ide.annotator.PluginBundle
import com.eny.i18n.plugin.utils.generator.code.TsxCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import org.junit.Test

class CreateMissingTranslationsTest: PlatformBaseTest() {

    @Test
    fun testUnavailableFix() = myFixture.runCommonConfig(Pair(CommonSettings::partialTranslationInspectionEnabled, true)){
        val hint = PluginBundle.getMessage("quickfix.create.missing.keys")
        val cg = TsxCodeGenerator()
        val tg = JsonTranslationGenerator()
        myFixture.addFileToProject("en/test.json",
            tg.generateNamedBlock(
                "root",
                tg.generateNamedBlock(
                    "sub",
                    tg.generateNamedBlock("base", "\"Partially defined translation\""),
                    1
                )
            )
        )
        myFixture.configureByText("sample.tsx", cg.generate("'test:root.sub.ba<caret>se'"))
        val action = myFixture.getAllQuickFixes().find {it.text == hint}
        assertNull(action)
    }

    @Test
    fun testCreateMissingTranslations() = myFixture.runCommonConfig(Pair(CommonSettings::partialTranslationInspectionEnabled, true)){
        val hint = PluginBundle.getMessage("quickfix.create.missing.keys")
        val cg = TsxCodeGenerator()
        val tg = JsonTranslationGenerator()
        myFixture.addFileToProject("en/test.json",
            tg.generateNamedBlock(
                "root",
                tg.generateNamedBlock(
                    "sub",
                    tg.generateNamedBlock("base", "\"Partially defined translation\""),
                    1
                )
            )
        )
        myFixture.addFileToProject("ru/test.json",
            """
                {
                  "root": {
                    "another": "value"
                  }
                }
            """.trimIndent()
        )
        myFixture.configureByText("sample.tsx", cg.generate("'test:root.sub.ba<caret>se'"))
        val action = myFixture.getAllQuickFixes().find {it.text == hint}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResult(
            "ru/test.json",
            """
                {
                  "root": {
                    "another": "value",
                    "sub": {
                      "base": "test:root.sub.base"
                    }
                  }
                }
            """.trimIndent(),
            false
        )
    }
}