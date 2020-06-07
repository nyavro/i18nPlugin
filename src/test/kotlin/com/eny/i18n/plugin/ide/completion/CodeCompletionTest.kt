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
    fun doCheck(fileName: String, lang: String, ext: String)
    fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, ext: String, translationContent: String)
}

internal class BasicChecker(private val fixture: CodeInsightTestFixture) {
    fun doCheck(fileName: String, expectedFilePath: String, translation: String) {
        fixture.configureByFiles(fileName, translation)
        fixture.complete(CompletionType.BASIC, 1)
        fixture.checkResultByFile(expectedFilePath)
    }

    fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, translationName: String, translationContent: String) {
        fixture.addFileToProject(translationName, translationContent)
        fixture.configureByText(sourceName, sourceCode)
        fixture.complete(CompletionType.BASIC, 1)
        fixture.checkResult(expectedCode)
    }
}

internal class VueChecker(private val fixture: CodeInsightTestFixture): Checker {
    private val checker = BasicChecker(fixture)
    override fun doCheck(fileName: String, lang: String, ext: String) = fixture.runVueConfig(
        Config(vueDirectory = "assets")
    ) {
        checker.doCheck("$lang/$fileName.$lang", "$lang/${fileName}Result.$lang", "assets/en-US.$ext")
    }
    override fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, ext: String, translationContent: String) {
        checker.doCheck(
            sourceName, sourceCode, expectedCode, "assets/en-US.$ext", translationContent
        )
    }
}

internal class DefaultNsChecker(fixture: CodeInsightTestFixture): Checker {
    private val checker = BasicChecker(fixture)
    override fun doCheck(fileName: String, lang: String, ext: String) {
        checker.doCheck("$lang/default/$fileName.$lang", "$lang/default/${fileName}Result.$lang", "assets/translation.$ext")
    }
    override fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, ext: String, translationContent: String) {
        checker.doCheck(
            sourceName, sourceCode, expectedCode, "assets/translation.$ext", translationContent
        )
    }
}

internal class NsChecker(fixture: CodeInsightTestFixture): Checker {
    private val checker = BasicChecker(fixture)
    override fun doCheck(fileName: String, lang: String, ext: String) {
        checker.doCheck("$lang/$fileName.$lang", "$lang/${fileName}Result.$lang", "assets/test.$ext")
    }

    override fun doCheck(sourceName: String, sourceCode: String, expectedCode: String, ext: String, translationContent: String) {
        checker.doCheck(
            sourceName, sourceCode, expectedCode, "assets/test.$ext", translationContent
        )
    }
}

internal abstract class CodeCompletionTestBase(
    private val lang: String,
    private val ext: String,
    private val codeGenerator: CodeGenerator,
    private val translationGenerator: TranslationGenerator,
    private val checkerProducer: (fixture: CodeInsightTestFixture) -> Checker = ::NsChecker) : BasePlatformTestCase() {

    private lateinit var checker: Checker

    override fun getTestDataPath(): String = "src/test/resources/codeCompletion"

    override fun setUp() {
        super.setUp()
        checker = checkerProducer(myFixture)
    }

    protected fun check(filePath: String) = checker.doCheck(filePath, lang, ext)

    //No completion happens
    fun testNoCompletion() = checker.doCheck(
        "none.${codeGenerator.ext()}",
        codeGenerator.generate("\"test:none.base.<caret>\""),
        codeGenerator.generate("\"test:none.base.\""),
        translationGenerator.ext(),
        translationGenerator.generateContent("tst1", "base", "single", "olny one value")
    )

    //Simple case - one possible completion of key: 'test:tst1.base.<caret>'
    fun testSingle() = check("single")

    //Completion of plural key: 'test:tst2.plurals.<caret>'
    fun testPlural() = check("plural")

    //Completion of partially typed key: 'test:tst1.base.si<caret>'
    fun testPartial() = check("partial")

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

internal abstract class CodeCompletionTestBasePhp(
    lang: String,
    ext: String,
    private val codeGenerator: CodeGenerator,
    private val translationGenerator: TranslationGenerator,
    checkerProducer: (fixture: CodeInsightTestFixture) -> Checker = ::NsChecker) :
        CodeCompletionTestBase(lang, ext, codeGenerator, translationGenerator, checkerProducer) {

    fun testDQuote() = check("dQuote")
}

internal class CodeCompletionInvalidTest: BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/resources/codeCompletion"

    fun testInvalid() {
        myFixture.configureByFiles("js/invalid.js")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("js/invalidResult.js")
    }

    fun testInvalidDefNs() {
        myFixture.configureByFiles("js/default/invalid.js")
        myFixture.complete(CompletionType.BASIC, 1)
        myFixture.checkResultByFile("js/default/invalidResult.js")
    }
}

internal class CodeCompletionTsJsonTest: CodeCompletionTestBase("ts","json", TsCodeGenerator(), JsonTranslationGenerator())
internal class CodeCompletionJsJsonTest: CodeCompletionTestBase("js","json", JsCodeGenerator(), JsonTranslationGenerator())
internal class CodeCompletionTsxJsonTest: CodeCompletionTestBase("tsx","json", TsxCodeGenerator(), JsonTranslationGenerator())
internal class CodeCompletionJsxJsonTest: CodeCompletionTestBase("jsx","json", JsxCodeGenerator(), JsonTranslationGenerator())
internal class CodeCompletionPhpJsonTest: CodeCompletionTestBasePhp("php","json", PhpCodeGenerator(), JsonTranslationGenerator())
internal class CodeCompletionTsYamlTest: CodeCompletionTestBase("ts", "yml", TsCodeGenerator(), YamlTranslationGenerator())
internal class CodeCompletionJsYamlTest: CodeCompletionTestBase("js", "yml", JsCodeGenerator(), YamlTranslationGenerator())
internal class CodeCompletionTsxYamlTest: CodeCompletionTestBase("tsx", "yml", TsxCodeGenerator(), YamlTranslationGenerator())
internal class CodeCompletionJsxYamlTest: CodeCompletionTestBase("jsx", "yml", JsxCodeGenerator(), YamlTranslationGenerator())
internal class CodeCompletionPhpYamlTest: CodeCompletionTestBase("php", "yml", PhpCodeGenerator(), YamlTranslationGenerator())
internal class CodeCompletionTsJsonDefNsTest: CodeCompletionTestBase("ts","json", TsCodeGenerator(), JsonTranslationGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsJsonDefNsTest: CodeCompletionTestBase("js","json", JsCodeGenerator(), JsonTranslationGenerator(), ::DefaultNsChecker)
internal class CodeCompletionTsxJsonDefNsTest: CodeCompletionTestBase("tsx","json", TsxCodeGenerator(), JsonTranslationGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsxJsonDefNsTest: CodeCompletionTestBase("jsx","json", JsxCodeGenerator(), JsonTranslationGenerator(), ::DefaultNsChecker)
internal class CodeCompletionPhpJsonDefNsTest: CodeCompletionTestBase("php","json", PhpCodeGenerator(), JsonTranslationGenerator(), ::DefaultNsChecker)
internal class CodeCompletionTsYamlDefNsTest: CodeCompletionTestBase("ts", "yml", TsCodeGenerator(), YamlTranslationGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsYamlDefNsTest: CodeCompletionTestBase("js", "yml", JsCodeGenerator(), YamlTranslationGenerator(), ::DefaultNsChecker)
internal class CodeCompletionTsxYamlDefNsTest: CodeCompletionTestBase("tsx", "yml", TsxCodeGenerator(), YamlTranslationGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsxYamlDefNsTest: CodeCompletionTestBase("jsx", "yml", JsxCodeGenerator(), YamlTranslationGenerator(), ::DefaultNsChecker)
internal class CodeCompletionPhpYamlDefNsTest: CodeCompletionTestBasePhp("php", "yml", PhpCodeGenerator(), YamlTranslationGenerator(), ::DefaultNsChecker)
internal class CodeCompletionVueJsonTest: CodeCompletionTestBase("vue", "json", VueCodeGenerator(), JsonTranslationGenerator(), ::VueChecker)
internal class CodeCompletionVueYamlTest: CodeCompletionTestBase("vue", "yml", VueCodeGenerator(), YamlTranslationGenerator(), ::VueChecker)