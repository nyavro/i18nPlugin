package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.intellij.openapi.ui.Messages
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

class TranslationFileGenerationJsTest : TranslationFileGenerationTestBase(JsCodeGenerator(), JsonTranslationGenerator(), ::noop)
class TranslationFileGenerationJsxTest : TranslationFileGenerationTestBase(JsxCodeGenerator(), JsonTranslationGenerator(), ::noop)
class TranslationFileGenerationTsTest : TranslationFileGenerationTestBase(TsCodeGenerator(), JsonTranslationGenerator(), ::noop)
class TranslationFileGenerationTsxTest : TranslationFileGenerationTestBase(TsxCodeGenerator(), JsonTranslationGenerator(), ::noop)

abstract class TranslationFileGenerationTestBase(
        private val cg: CodeGenerator,
        private val tg: TranslationGenerator,
        private val wrapper: (CodeInsightTestFixture, () -> Unit) -> Unit
): ExtractionTestBase() {

    //TODO: Default ns test
    fun testTranslationFileGeneration() = wrapper(myFixture) {
        myFixture.configureByText("simple.${cg.ext()}", cg.generateBlock("<caret>I want to move it to translation"))
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

