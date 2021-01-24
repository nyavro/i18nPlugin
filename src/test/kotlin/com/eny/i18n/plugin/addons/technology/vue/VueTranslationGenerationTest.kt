package com.eny.i18n.plugin.addons.technology.vue

import com.eny.i18n.plugin.ide.actions.ExtractionTestBase
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.translationGenerator
import com.eny.i18n.plugin.utils.generator.code.VueScriptAttributeCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueScriptCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueTsCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.openapi.ui.Messages
import org.junit.Assert
import org.junit.Test

class VueTranslationGenerationTest: ExtractionTestBase() {

    private val tg = JsonTranslationGenerator()

    fun testTranslationFileGenerationVue() = myFixture.runVueConfig {
        val cg = VueScriptCodeGenerator()
        myFixture.tempDirFixture.findOrCreateDir("locales")
        myFixture.configureByText("simple.${cg.ext()}", cg.generateBlock("\"I want<caret> to move it to translation\""))
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(predefinedTextInputDialog("component.header.title"))
        myFixture.launchAction(action)
        myFixture.checkResult(cg.generateBlock("this.\$t('component.header.title')"))
        myFixture.checkResult(
            "locales/en.${tg.ext()}",
            tg.generateContent("component", "header", "title", "I want to move it to translation"),
            false
        )
    }

    fun testTranslationFileGenerationVueTs() = myFixture.runVueConfig {
        val cg = VueTsCodeGenerator()
        myFixture.tempDirFixture.findOrCreateDir("locales")
        myFixture.configureByText("simple.${cg.ext()}", cg.generateBlock("\"I want<caret> to move it to translation\""))
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(predefinedTextInputDialog("component.header.title"))
        myFixture.launchAction(action)
        myFixture.checkResult(cg.generateBlock("this.\$t('component.header.title').toString()"))
        myFixture.checkResult(
            "locales/en.${tg.ext()}",
            tg.generateContent("component", "header", "title", "I want to move it to translation"),
            false
        )
    }

    fun testTranslationFileGenerationVueAttributeFix() = myFixture.runVueConfig {
        val cg = VueScriptAttributeCodeGenerator("attr")
        myFixture.tempDirFixture.findOrCreateDir("locales")
        myFixture.configureByText("simple.${cg.ext()}", cg.generate("\"I want<caret> to move it to translation\""))
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(predefinedTextInputDialog("component.header.title"))
        myFixture.launchAction(action)
        myFixture.checkResult(VueScriptAttributeCodeGenerator(":attr").generate("\"\$t('component.header.title')\""))
        myFixture.checkResult(
                "locales/en.${tg.ext()}",
                tg.generateContent("component", "header", "title", "I want to move it to translation"),
                false
        )
    }
}