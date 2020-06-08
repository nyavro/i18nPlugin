package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

internal class VueChecker(private val fixture: CodeInsightTestFixture): Checker {
    private val checker = BasicChecker(fixture)
    override fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, ext: String, translationContent: String) = fixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {
        checker.doCheck(
            sourceName, sourceCode, expectedCode, "assets/en-US.$ext", translationContent
        )
    }
}

internal abstract class CodeCompletionTestBaseVue(translationGenerator: TranslationGenerator) :
    CodeCompletionTestBase(VueCodeGenerator(), translationGenerator, DefaultNsKeyGenerator(), ::VueChecker) {

    fun testEmptyKeyCompletion() = myFixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {
        myFixture.addFileToProject("assets/en-US.${translationGenerator.ext()}", translationGenerator.generateContent("tstw", "fstt", "leu", "value"))
        myFixture.configureByText("empty.${codeGenerator.ext()}", codeGenerator.generate( "\"<caret>\""))
        val vars = myFixture.completeBasic()
        assertTrue(vars.find {it.lookupString == "tstw"} != null)
    }

    fun testRootKeyCompletion() = myFixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {
        myFixture.addFileToProject("assets/en-US.${translationGenerator.ext()}", translationGenerator.generateContent("tst1", "base", "single", "only one value"))
        myFixture.configureByText("empty.${codeGenerator.ext()}", codeGenerator.generate("\"tst<caret>\""))
        val vars = myFixture.completeBasic()
        assertTrue(vars.find {it.lookupString == "tst1"} != null)
    }
}

internal class CodeCompletionVueJsonTest: CodeCompletionTestBaseVue(JsonTranslationGenerator())
internal class CodeCompletionVueYamlTest: CodeCompletionTestBaseVue(YamlTranslationGenerator())
