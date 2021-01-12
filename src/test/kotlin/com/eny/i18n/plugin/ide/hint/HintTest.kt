package com.eny.i18n.plugin.ide.hint

import com.eny.i18n.plugin.PlatformBaseTest
//import com.eny.i18n.plugin.ide.CodeTranslationGenerators
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.VueSettings
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.intellij.codeInsight.documentation.DocumentationManager
import org.junit.Test
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.ArgumentsSource

class HintTest: PlatformBaseTest() {

    // @TODO 8


    @Test
    fun testStub1() {
        assertTrue(true)
    }


//
//    @ParameterizedTest
//    @ArgumentsSource(CodeTranslationGenerators::class)
//    fun testSingleHint(cg: CodeGenerator, tg: TranslationGenerator) {
//        val translation = "translation here"
//        myFixture.addFileToProject("test.${tg.ext()}", tg.generateContent("root", "first", "second", translation))
//        myFixture.configureByText("content.${cg.ext()}", cg.generate("\"test:root.first.<caret>second\""))
//        read {
//            val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
//            assertEquals(
//                translation,
//                DocumentationManager.getProviderFromElement(codeElement).getQuickNavigateInfo(myFixture.elementAtCaret, codeElement)
//            )
//        }
//    }

    @Test
    fun vueSfcHint() = myFixture.runVueConfig {
        val cg = VueCodeGenerator()
        val tg = JsonTranslationGenerator()
        myFixture.configureByText(
            "App.vue",
            cg.generateSfcBlock(
                "<p>{{ \$t('root.first.<caret>second')}}</p>",
                tg.generateNamedBlock("en", tg.generateContent("root", "first", "second", "translation here"))
            )
        )
        val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
        //Not supported for Vue SFC:
        assertNull(
            DocumentationManager.getProviderFromElement(codeElement).getQuickNavigateInfo(myFixture.elementAtCaret, codeElement)
        )
    }
}