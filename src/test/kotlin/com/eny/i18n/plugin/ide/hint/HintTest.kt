package com.eny.i18n.plugin.ide.hint

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.documentation.DocumentationManager
import org.junit.jupiter.api.Test

class HintTest: PlatformBaseTest() {

    @Test
    fun testSingleHint() {
        val cgs = listOf(JsCodeGenerator(), TsCodeGenerator(), JsxCodeGenerator(), TsxCodeGenerator(), PhpSingleQuoteCodeGenerator(), PhpDoubleQuoteCodeGenerator())
        val translation = "translation here"
        val tg = JsonTranslationGenerator()
        cgs.forEachIndexed {
            index, cg ->
                myFixture.addFileToProject("test${index}.${tg.ext()}", tg.generateContent("root", "first", "second", translation))
                myFixture.configureByText("content.${cg.ext()}", cg.generate("\"test${index}:root.first.<caret>second\""))
                read {
                    val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
                    assertEquals(
                        translation,
                        DocumentationManager.getProviderFromElement(codeElement)
                            .getQuickNavigateInfo(myFixture.elementAtCaret, codeElement)
                    )
                }
        }
    }
}
