package com.eny.i18n.plugin.ide.hint

import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.Json5TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import com.intellij.codeInsight.documentation.DocumentationManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import utils.randomOf

abstract class JsHintTestBase(private val cg: CodeGenerator, private val tg: TranslationGenerator): BasePlatformTestCase() {

    fun testSingleHint() {
        val translation = "translation here"
        myFixture.addFileToProject("test.${tg.ext()}", tg.generateContent("root", "first", "second", translation))
        myFixture.configureByText("content.${cg.ext()}", cg.generate("\"test:root.first.<caret>second\""))
        val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
        assertEquals(
            translation,
            DocumentationManager.getProviderFromElement(codeElement).getQuickNavigateInfo(myFixture.elementAtCaret, codeElement)
        )
    }
}

class HintTest: JsHintTestBase(
    randomOf(
        ::JsCodeGenerator,
        ::TsCodeGenerator,
        ::JsxCodeGenerator,
        ::TsxCodeGenerator,
        ::PhpCodeGenerator,
        ::VueCodeGenerator
    )(),
    randomOf(::JsonTranslationGenerator, ::YamlTranslationGenerator, ::Json5TranslationGenerator)()
)
