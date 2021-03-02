package com.eny.i18n.plugin.ide.hint

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.CodeTranslationGenerators
import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.intellij.codeInsight.documentation.DocumentationManager
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class HintTestVue: PlatformBaseTest() {

    @Test
    fun testSingleHint() = myFixture.runVue {
        val cg = VueCodeGenerator()
        val tg = JsonTranslationGenerator()
        val translation = "translation here"
        myFixture.addFileToProject("assets/en-US.${tg.ext()}", tg.generateContent("root", "first", "second", translation))
        myFixture.configureByText("content.${cg.ext()}", cg.generate("\"test.root.first.<caret>second\""))
        read {
            val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
            assertEquals(
                translation,
                DocumentationManager.getProviderFromElement(codeElement).getQuickNavigateInfo(myFixture.elementAtCaret, codeElement)
            )
        }
    }

    @Test
    fun testSingleHintFirstComponentNs() = myFixture.runVueConfig(Config(firstComponentNs = true)) {
        val cg = VueCodeGenerator()
        val tg = JsonTranslationGenerator()
        val translation = "translation here"
        myFixture.addFileToProject("test.${tg.ext()}", tg.generateContent("root", "first", "second", translation))
        myFixture.configureByText("content.${cg.ext()}", cg.generate("\"test.root.first.<caret>second\""))
        read {
            val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
            assertEquals(
                translation,
                DocumentationManager.getProviderFromElement(codeElement).getQuickNavigateInfo(myFixture.elementAtCaret, codeElement)
            )
        }
    }
}
