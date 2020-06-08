package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

internal interface Checker {
    fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, ext: String, translationContent: String)
}

interface KeyGenerator {
    fun generate(ns: String, compositeKey: String, quote: String = "'"): String
}

class DefaultNsKeyGenerator: KeyGenerator {
    override fun generate(ns: String, compositeKey: String, quote: String): String = "$quote$compositeKey$quote"
}

class NsKeyGenerator: KeyGenerator {
    override fun generate(ns: String, compositeKey: String, quote: String): String = "$quote$ns:$compositeKey$quote"
}

internal class BasicChecker(private val fixture: CodeInsightTestFixture) {
    fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, translationName: String, translationContent: String) {
        fixture.addFileToProject(translationName, translationContent)
        fixture.configureByText(sourceName, sourceCode)
        fixture.complete(CompletionType.BASIC, 1)
        fixture.checkResult(expectedCode)
    }
}

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

internal class DefaultNsChecker(fixture: CodeInsightTestFixture): Checker {
    private val checker = BasicChecker(fixture)
    override fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, ext: String, translationContent: String) {
        checker.doCheck(
            sourceName, sourceCode, expectedCode, "assets/translation.$ext", translationContent
        )
    }
}

internal class NsChecker(fixture: CodeInsightTestFixture): Checker {
    private val checker = BasicChecker(fixture)
    override fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, ext: String, translationContent: String) {
        checker.doCheck(
            sourceName, sourceCode, expectedCode, "assets/test.$ext", translationContent
        )
    }
}

internal abstract class CodeCompletionTestBase(
    private val codeGenerator: CodeGenerator,
    private val translationGenerator: TranslationGenerator,
    private val keyGenerator: KeyGenerator,
    private val checkerProducer: (fixture: CodeInsightTestFixture) -> Checker = ::NsChecker) : BasePlatformTestCase() {

    protected lateinit var checker: Checker

    override fun setUp() {
        super.setUp()
        checker = checkerProducer(myFixture)
    }

    //No completion happens
    fun testNoCompletion() = checker.doCheck(
        "none.${codeGenerator.ext()}",
        codeGenerator.generate(keyGenerator.generate("test", "none.base.<caret>")),
        codeGenerator.generate(keyGenerator.generate("test", "none.base.")),
        translationGenerator.ext(),
        translationGenerator.generateContent("tst1", "base", "single", "only one value")
    )

    //Simple case - one possible completion of key: 'test:tst1.base.<caret>'
    fun testSingle() = checker.doCheck(
        "single.${codeGenerator.ext()}",
        codeGenerator.generate(keyGenerator.generate("test", "tst1.base.<caret>")),
        codeGenerator.generate(keyGenerator.generate("test","tst1.base.single")),
        translationGenerator.ext(),
        translationGenerator.generateContent("tst1", "base", "single", "only one value")
    )

    //Completion of plural key: 'test:tst2.plurals.<caret>'
    fun testPlural() = checker.doCheck(
        "plural.${codeGenerator.ext()}",
        codeGenerator.generate(keyGenerator.generate("test", "tst2.plurals.<caret>")),
        codeGenerator.generate(keyGenerator.generate("test","tst2.plurals.value")),
        translationGenerator.ext(),
        translationGenerator.generatePlural("tst2", "plurals", "value", "tt", "qq", "vv")
    )

    //Completion of partially typed key: 'test:tst1.base.si<caret>'
    fun testPartial() = checker.doCheck(
        "partial.${codeGenerator.ext()}",
        codeGenerator.generate(keyGenerator.generate("test", "tst1.base.si<caret>")),
        codeGenerator.generate(keyGenerator.generate("test","tst1.base.single")),
        translationGenerator.ext(),
        translationGenerator.generateContent("tst1", "base", "single", "only one value")
    )

//    fun testRename() {
//        myFixture.configureByFiles("RenameTestData.java", "RenameTestData.simple")
//        myFixture.renameElementAtCaret("websiteUrl")
//        myFixture.checkResultByFile("RenameTestData.simple", "RenameTestDataAfter.simple", false)
//    }
//
//    fun testFindUsages() {
//         val usageInfos = myFixture.testFindUsages("FindUsagesTestData.simple", "FindUsagesTestData.java")
//        assertEquals(1, usageInfos.size)
//    }
}

internal class CodeCompletionTsJsonTest: CodeCompletionTestBase(TsCodeGenerator(), JsonTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionJsJsonTest: CodeCompletionTestBase(JsCodeGenerator(), JsonTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionTsxJsonTest: CodeCompletionTestBase(TsxCodeGenerator(), JsonTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionJsxJsonTest: CodeCompletionTestBase(JsxCodeGenerator(), JsonTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionTsYamlTest: CodeCompletionTestBase(TsCodeGenerator(), YamlTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionJsYamlTest: CodeCompletionTestBase(JsCodeGenerator(), YamlTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionTsxYamlTest: CodeCompletionTestBase(TsxCodeGenerator(), YamlTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionJsxYamlTest: CodeCompletionTestBase(JsxCodeGenerator(), YamlTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionPhpYamlTest: CodeCompletionTestBase(PhpCodeGenerator(), YamlTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionTsJsonDefNsTest: CodeCompletionTestBase(TsCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsJsonDefNsTest: CodeCompletionTestBase(JsCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionTsxJsonDefNsTest: CodeCompletionTestBase(TsxCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsxJsonDefNsTest: CodeCompletionTestBase(JsxCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionPhpJsonDefNsTest: CodeCompletionTestBase(PhpCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionTsYamlDefNsTest: CodeCompletionTestBase(TsCodeGenerator(), YamlTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsYamlDefNsTest: CodeCompletionTestBase(JsCodeGenerator(), YamlTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionTsxYamlDefNsTest: CodeCompletionTestBase(TsxCodeGenerator(), YamlTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsxYamlDefNsTest: CodeCompletionTestBase(JsxCodeGenerator(), YamlTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionVueJsonTest: CodeCompletionTestBase(VueCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::VueChecker)
internal class CodeCompletionVueYamlTest: CodeCompletionTestBase(VueCodeGenerator(), YamlTranslationGenerator(), DefaultNsKeyGenerator(), ::VueChecker)