package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.JsonYamlCodeGenerators
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.intellij.openapi.ui.TestDialogManager.setTestInputDialog
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class TranslationFileGenerationTest: ExtractionTestBase() {

    @ParameterizedTest
    @ArgumentsSource(JsonYamlCodeGenerators::class)
    fun testTranslationFileGeneration(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext())) {
        val ext = cg.ext()
        val text = cg.generateBlock("<caret>I want to move it to translation")
        myFixture.configureByText("simple.${ext}", text)
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        setTestInputDialog(predefinedTextInputDialog("main:component.header.title"))
        myFixture.launchAction(action)
        val res = cg.generate("'main:component.header.title'")
        val txt = tg.generateContent("component", "header", "title", "I want to move it to translation")
        myFixture.checkResult(res)
        myFixture.checkResult(
            "main.${tg.ext()}",
            txt,
            false
        )
    }

    @Test
    fun testTranslation() {}
}