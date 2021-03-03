package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.junit.jupiter.api.Test

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

    @Test
    fun testEmptyKeyCompletion() = myFixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {
        myFixture.addFileToProject("assets/en-US.${translationGenerator.ext()}", translationGenerator.generateContent("tstw", "fstt", "leu", "value"))
        myFixture.configureByText("empty.${codeGenerator.ext()}", codeGenerator.generate( "\"<caret>\""))
        val vars = myFixture.completeBasic()
        assertTrue(vars.find {it.lookupString == "tstw"} != null)
    }

    @Test
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


class CodeCompletionVueNamespace : PlatformBaseTest() {

    val tg = JsonTranslationGenerator()
    val cg = VueCodeGenerator()

    fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, translationName: String, translationContent: String) {
        myFixture.addFileToProject(translationName, translationContent)
        myFixture.configureByText(sourceName, sourceCode)
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResult(expectedCode)
    }

    @Test
    fun testNoCompletion() = myFixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {doCheck(
        "none.${cg.ext()}",
        cg.generate("test.none.base.<caret>"),
        cg.generate("test.none.base."),
        tg.ext(),
        tg.generateContent("tst1", "base", "single", "only one value")
    )}

    fun testSingle() = myFixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {
        doCheck(
            "single.${cg.ext()}",
            cg.generate("test.tst1.base.<caret>"),
            cg.generate("test.tst1.base.single"),
            tg.ext(),
            tg.generateContent("tst1", "base", "single", "only one value")
        )
    }

    fun testPlural() = myFixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {
        doCheck(
            "plural.${cg.ext()}",
            cg.generate("test.tst2.plurals.<caret>"),
            cg.generate("test.tst2.plurals.value"),
            tg.ext(),
            tg.generatePlural("tst2", "plurals", "value", "tt", "qq", "vv")
        )
    }

    fun testPartial() = myFixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {
        doCheck(
            "partial.${cg.ext()}",
            cg.generate("test.tst1.base.si<caret>"),
            cg.generate("test.tst1.base.single"),
            tg.ext(),
            tg.generateContent("tst1", "base", "single", "only one value")
        )
    }

    fun testInvalidCompletion() = myFixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {doCheck(
        "partial.${cg.ext()}",
        cg.generate("test.tst1.base.si<caret>ng"),
        cg.generate("test.tst1.base.sing"),
        tg.ext(),
        tg.generateContent("tst1", "base", "single", "only one value")
    )}

    fun testEmptyKeyCompletion() = myFixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {
        myFixture.addFileToProject("assets/en-US.${tg.ext()}", tg.generateContent("tstw", "fstt", "leu", "value"))
        myFixture.configureByText("empty.${cg.ext()}", cg.generate( "\"<caret>\""))
        val vars = myFixture.completeBasic()
        assertTrue(vars.find {it.lookupString == "tstw"} != null)
    }

    fun testRootKeyCompletion() = myFixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {
        myFixture.addFileToProject("assets/en-US.${tg.ext()}", tg.generateContent("tst1", "base", "single", "only one value"))
        myFixture.configureByText("empty.${cg.ext()}", cg.generate("\"tst<caret>\""))
        val vars = myFixture.completeBasic()
        assertTrue(vars.find {it.lookupString == "tst1"} != null)
    }
}
