package com.eny.i18n.plugin.addons.technology.vue

import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.completion.BasicChecker
import com.eny.i18n.plugin.ide.completion.Checker
import com.eny.i18n.plugin.ide.completion.CodeCompletionTestBase
import com.eny.i18n.plugin.ide.completion.DefaultNsKeyGenerator
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.junit.Test

internal class VueChecker(private val fixture: CodeInsightTestFixture): Checker {
    private val checker = BasicChecker(fixture)
    override fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, ext: String, translationContent: String) = fixture.runVueConfig(
        Pair(VueSettings::vueDirectory, "assets")
    ) {
        checker.doCheck(
            sourceName, sourceCode, expectedCode, "assets/en-US.$ext", translationContent
        )
    }
}

internal abstract class CodeCompletionTestBaseVue(translationGenerator: TranslationGenerator) :
    CodeCompletionTestBase(VueCodeGenerator(), translationGenerator, DefaultNsKeyGenerator(), ::VueChecker) {

    @Test
    fun testEmptyKeyCompletion() = myFixture.runVueConfig(
        Pair(VueSettings::vueDirectory, "assets")
    ) {
        myFixture.addFileToProject("assets/en-US.${translationGenerator.ext()}", translationGenerator.generateContent("tstw", "fstt", "leu", "value"))
        myFixture.configureByText("empty.${codeGenerator.ext()}", codeGenerator.generate( "\"<caret>\""))
        val vars = myFixture.completeBasic()
        assertTrue(vars.find {it.lookupString == "tstw"} != null)
    }

    @Test
    fun testRootKeyCompletion() = myFixture.runVueConfig(
        Pair(VueSettings::vueDirectory, "assets")
    ) {
        myFixture.addFileToProject("assets/en-US.${translationGenerator.ext()}", translationGenerator.generateContent("tst1", "base", "single", "only one value"))
        myFixture.configureByText("empty.${codeGenerator.ext()}", codeGenerator.generate("\"tst<caret>\""))
        val vars = myFixture.completeBasic()
        assertTrue(vars.find {it.lookupString == "tst1"} != null)
    }
}

internal class CodeCompletionVueJsonTest: CodeCompletionTestBaseVue(JsonTranslationGenerator())
