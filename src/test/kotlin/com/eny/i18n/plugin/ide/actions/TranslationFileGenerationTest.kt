package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.JsonYamlCodeGenerators
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.translationGenerator
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.intellij.openapi.ui.Messages
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.ValueSource

class TranslationFileGenerationTest: ExtractionTestBase() {

    @ParameterizedTest
    @ArgumentsSource(JsonYamlCodeGenerators::class)
    fun testTranslationFileGeneration(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext())) {
        myFixture.configureByText("simple.${cg.ext()}", cg.generateNotExtracted("<caret>I want to move it to translation"))
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(predefinedTextInputDialog("main:component.header.title"))
        myFixture.launchAction(action)
        myFixture.checkResult(cg.generate("'main:component.header.title'"))
        myFixture.checkResult(
            "main.${tg.ext()}",
            tg.generateContent("component", "header", "title", "I want to move it to translation"),
            false
        )
    }
}

class VueTranslationGenerationTest: ExtractionTestBase() {

    private val cg = VueCodeGenerator()

    @ParameterizedTest
    @ValueSource(strings = ["yml", "json"])
    fun testTranslationFileGenerationVue(ext: String) = myFixture.runVueConfig(config(ext)) {
        val tg = translationGenerator(ext)!!
        myFixture.tempDirFixture.findOrCreateDir("locales")
        myFixture.configureByText("simple.${cg.ext()}", cg.generateScript("\"I want<caret> to move it to translation\""))
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(predefinedTextInputDialog("component.header.title"))
        myFixture.launchAction(action)
        myFixture.checkResult(cg.generateScript("this.\$t('component.header.title')"))
        myFixture.checkResult(
            "locales/en.${tg.ext()}",
            tg.generateContent("component", "header", "title", "I want to move it to translation"),
            false
        )
    }
}