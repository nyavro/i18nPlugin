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
    private val keyGenerator: KeyGenerator,
    private val checkerProducer: (fixture: CodeInsightTestFixture) -> Checker = ::NsChecker) : BasePlatformTestCase() {

    protected lateinit var checker: Checker

    override fun getTestDataPath(): String = "src/test/resources/codeCompletion"

    override fun setUp() {
        super.setUp()
        checker = checkerProducer(myFixture)
    }

    protected fun check(filePath: String) = checker.doCheck(filePath, lang, ext)

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

internal abstract class CodeCompletionTestBasePhp(
    lang: String,
    ext: String,
    private val codeGenerator: CodeGenerator,
    private val translationGenerator: TranslationGenerator,
    private val keyGenerator: KeyGenerator,
    checkerProducer: (fixture: CodeInsightTestFixture) -> Checker = ::NsChecker) :
        CodeCompletionTestBase(lang, ext, codeGenerator, translationGenerator, keyGenerator, checkerProducer) {

    fun testDQuote() = checker.doCheck(
        "dQuote.${codeGenerator.ext()}",
        codeGenerator.generate(keyGenerator.generate("test", "tst1.base.<caret>", "\"")),
        codeGenerator.generate(keyGenerator.generate("test","tst1.base.single", "\"")),
        translationGenerator.ext(),
        translationGenerator.generateContent("tst1", "base", "single", "only one value")
    )
//            = check("dQuote")
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

internal class CodeCompletionTsJsonTest: CodeCompletionTestBase("ts","json", TsCodeGenerator(), JsonTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionJsJsonTest: CodeCompletionTestBase("js","json", JsCodeGenerator(), JsonTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionTsxJsonTest: CodeCompletionTestBase("tsx","json", TsxCodeGenerator(), JsonTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionJsxJsonTest: CodeCompletionTestBase("jsx","json", JsxCodeGenerator(), JsonTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionPhpJsonTest: CodeCompletionTestBasePhp("php","json", PhpCodeGenerator(), JsonTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionTsYamlTest: CodeCompletionTestBase("ts", "yml", TsCodeGenerator(), YamlTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionJsYamlTest: CodeCompletionTestBase("js", "yml", JsCodeGenerator(), YamlTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionTsxYamlTest: CodeCompletionTestBase("tsx", "yml", TsxCodeGenerator(), YamlTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionJsxYamlTest: CodeCompletionTestBase("jsx", "yml", JsxCodeGenerator(), YamlTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionPhpYamlTest: CodeCompletionTestBase("php", "yml", PhpCodeGenerator(), YamlTranslationGenerator(), NsKeyGenerator())
internal class CodeCompletionTsJsonDefNsTest: CodeCompletionTestBase("ts","json", TsCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsJsonDefNsTest: CodeCompletionTestBase("js","json", JsCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionTsxJsonDefNsTest: CodeCompletionTestBase("tsx","json", TsxCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsxJsonDefNsTest: CodeCompletionTestBase("jsx","json", JsxCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionPhpJsonDefNsTest: CodeCompletionTestBase("php","json", PhpCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionTsYamlDefNsTest: CodeCompletionTestBase("ts", "yml", TsCodeGenerator(), YamlTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsYamlDefNsTest: CodeCompletionTestBase("js", "yml", JsCodeGenerator(), YamlTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionTsxYamlDefNsTest: CodeCompletionTestBase("tsx", "yml", TsxCodeGenerator(), YamlTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionJsxYamlDefNsTest: CodeCompletionTestBase("jsx", "yml", JsxCodeGenerator(), YamlTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionPhpYamlDefNsTest: CodeCompletionTestBasePhp("php", "yml", PhpCodeGenerator(), YamlTranslationGenerator(), DefaultNsKeyGenerator(), ::DefaultNsChecker)
internal class CodeCompletionVueJsonTest: CodeCompletionTestBase("vue", "json", VueCodeGenerator(), JsonTranslationGenerator(), DefaultNsKeyGenerator(), ::VueChecker)
internal class CodeCompletionVueYamlTest: CodeCompletionTestBase("vue", "yml", VueCodeGenerator(), YamlTranslationGenerator(), DefaultNsKeyGenerator(), ::VueChecker)