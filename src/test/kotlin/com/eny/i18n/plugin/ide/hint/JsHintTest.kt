package com.eny.i18n.plugin.ide.hint

import com.eny.i18n.plugin.utils.generator.code.JsCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.documentation.DocumentationManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class JsHintTest: BasePlatformTestCase() {

    val cg = JsCodeGenerator();
    val tg = JsonTranslationGenerator();

    fun testSingleHint() {
        val translation = "translation here"
        val content = cg.generate("\"test:root.first.<caret>second\"")
        val json = tg.generateContent("root", "first", "second", translation)
        myFixture.addFileToProject("test.json", json)
        myFixture.configureByText("content.js", content)
        val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
        val translationElement = myFixture.elementAtCaret
        val ctrlHover = DocumentationManager.getProviderFromElement(codeElement).getQuickNavigateInfo(translationElement, codeElement)
        assertEquals(translation, ctrlHover)
    }
}
