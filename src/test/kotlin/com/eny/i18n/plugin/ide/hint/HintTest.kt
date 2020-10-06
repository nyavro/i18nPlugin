package com.eny.i18n.plugin.ide.hint

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.CodeTranslationGenerators
import com.eny.i18n.plugin.ide.TranslationGenerators
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.Json5TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import com.intellij.codeInsight.documentation.DocumentationManager
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.ValueSource
import utils.randomOf
import kotlin.concurrent.thread

class JsHintTestBase: PlatformBaseTest() {

    @ParameterizedTest
    @ArgumentsSource(CodeTranslationGenerators::class)
    fun testSingleHint(cg: CodeGenerator, tg: TranslationGenerator) {
        val translation = "translation here"
        myFixture.addFileToProject("test.${tg.ext()}", tg.generateContent("root", "first", "second", translation))
        myFixture.configureByText("content.${cg.ext()}", cg.generate("\"test:root.first.<caret>second\""))
        read {
            val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
            assertEquals(
                translation,
                DocumentationManager.getProviderFromElement(codeElement).getQuickNavigateInfo(myFixture.elementAtCaret, codeElement)
            )
        }
    }
//
//    @Test
//    fun testVueSfcSingleHint() {
//        val cg = VueCodeGenerator()
//        val tg = JsonTranslationGenerator()
//        val translation = "translation here"
//        val translationBlock = tg.generateNamedBlock("en", tg.generateContent("root", "first", "second", translation))
//        val generateSfcBlock = cg.generateSfcBlock(
//            "<p>{{ \$t('root.first.<caret>second')}}</p>",
//            translationBlock
//        )
//        myFixture.configureByText(
//            "App.vue",
//            generateSfcBlock
//        )
//        read {
//            val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
//            assertEquals(
//                translation,
//                DocumentationManager.getProviderFromElement(codeElement).getQuickNavigateInfo(codeElement, codeElement)
//            )
//        }
//    }
}