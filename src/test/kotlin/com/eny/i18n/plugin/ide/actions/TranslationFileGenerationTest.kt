package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.code.JsCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.intellij.openapi.ui.Messages
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.ValueSource

class TranslationFileGenerationTest: ExtractionTestBase() {

    @ParameterizedTest
    @ArgumentsSource(CodeGenerators::class)
    @ValueSource(strings = ["yml", "json"])
    fun testTranslationFileGeneration(cg: CodeGenerator, ext: String) = myFixture.runWithConfig(
        Config(yamlContentGenerationEnabled = ext == "yml", jsonContentGenerationEnabled = ext == "json")
    ) {
        val tg = translationGenerator(ext)!!
        myFixture.configureByText("simple.${cg.ext()}", cg.generateNotExtracted("\"<caret>I want to move it to translation\""))
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
    fun testTranslationFileGenerationVue(ext: String) = myFixture.runVueConfig(
        Config(yamlContentGenerationEnabled = ext == "yml", jsonContentGenerationEnabled = ext == "json", preferYamlFilesGeneration = ext == "yml")
    ) {
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