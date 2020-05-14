package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.ide.SettingsPack
import com.eny.i18n.plugin.ide.runVuePack
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

internal interface Checker {
    fun doCheck(fileName: String, lang: String, ext: String)
}

internal class BasicChecker(private val fixture: CodeInsightTestFixture) {
    fun doCheck(fileName: String, expectedFilePath: String, translation: String) {
        fixture.configureByFiles(fileName, translation)
        fixture.complete(CompletionType.BASIC, 1)
        fixture.checkResultByFile(expectedFilePath)
    }
}

internal class VueChecker(private val fixture: CodeInsightTestFixture): Checker {
    private val checker = BasicChecker(fixture)
    override fun doCheck(fileName: String, lang: String, ext: String) = fixture.runVuePack(
        SettingsPack().with(Settings::vueDirectory, "assets")
    ) {
        checker.doCheck("$lang/$fileName.$lang", "$lang/${fileName}Result.$lang", "assets/en-US.$ext")
    }
}

internal class DefaultNsChecker(fixture: CodeInsightTestFixture): Checker {
    private val checker = BasicChecker(fixture)
    override fun doCheck(fileName: String, lang: String, ext: String) {
        checker.doCheck("$lang/default/$fileName.$lang", "$lang/default/${fileName}Result.$lang", "assets/translation.$ext")
    }
}

internal class NsChecker(private val fixture: CodeInsightTestFixture): Checker {
    private val checker = BasicChecker(fixture)
    override fun doCheck(fileName: String, lang: String, ext: String) {
        checker.doCheck("$lang/$fileName.$lang", "$lang/${fileName}Result.$lang", "assets/test.$ext")
    }
}

internal abstract class CodeCompletionTestBase(
    private val lang: String,
    private val ext: String,
    private val checkerProducer: (fixture: CodeInsightTestFixture) -> Checker = ::NsChecker) : BasePlatformTestCase() {

    private lateinit var checker: Checker

    override fun getTestDataPath(): String = "src/test/resources/codeCompletion"

    override fun setUp() {
        super.setUp()
        checker = checkerProducer(myFixture)
    }

    protected fun check(filePath: String) = checker.doCheck(filePath, lang, ext)

    fun testNoCompletion() = check("none")
    fun testSingle() = check("single")
    fun testSingleNoDot() = check("singleNoDot")
    fun testPlural() = check("plural")
//
//    fun testRename() {
//        myFixture.configureByFiles("RenameTestData.java", "RenameTestData.simple")
//        myFixture.renameElementAtCaret("websiteUrl")
//        myFixture.checkResultByFile("RenameTestData.simple", "RenameTestDataAfter.simple", false)
//    }
//
//    fun testFindUsages() {
//         val usageInfos = myFixture.testFindUsages("FindUsagesTestData.simple", "FindUsagesTestData.java")
//        Assert.assertEquals(1, usageInfos.size)
//    }
}

internal abstract class CodeCompletionTestBasePhp(
    lang: String,
    ext: String,
    checkerProducer: (fixture: CodeInsightTestFixture) -> Checker = ::NsChecker) : CodeCompletionTestBase(lang, ext, checkerProducer) {

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

internal class CodeCompletionTsJsonTest: CodeCompletionTestBase("ts","json")
internal class CodeCompletionJsJsonTest: CodeCompletionTestBase("js","json")
internal class CodeCompletionTsxJsonTest: CodeCompletionTestBase("tsx","json")
//internal class CodeCompletionJsxJsonTest: CodeCompletionTestBase("jsx","json")
internal class CodeCompletionPhpJsonTest: CodeCompletionTestBasePhp("php","json")
internal class CodeCompletionTsYamlTest: CodeCompletionTestBase("ts", "yml")
internal class CodeCompletionJsYamlTest: CodeCompletionTestBase("js", "yml")
internal class CodeCompletionTsxYamlTest: CodeCompletionTestBase("tsx", "yml")
internal class CodeCompletionJsxYamlTest: CodeCompletionTestBase("jsx", "yml")
internal class CodeCompletionPhpYamlTest: CodeCompletionTestBase("php", "yml")
internal class CodeCompletionTsJsonDefNsTest: CodeCompletionTestBase("ts","json", ::DefaultNsChecker)
internal class CodeCompletionJsJsonDefNsTest: CodeCompletionTestBase("js","json", ::DefaultNsChecker)
internal class CodeCompletionTsxJsonDefNsTest: CodeCompletionTestBase("tsx","json", ::DefaultNsChecker)
internal class CodeCompletionJsxJsonDefNsTest: CodeCompletionTestBase("jsx","json", ::DefaultNsChecker)
internal class CodeCompletionPhpJsonDefNsTest: CodeCompletionTestBase("php","json", ::DefaultNsChecker)
internal class CodeCompletionTsYamlDefNsTest: CodeCompletionTestBase("ts", "yml", ::DefaultNsChecker)
internal class CodeCompletionJsYamlDefNsTest: CodeCompletionTestBase("js", "yml", ::DefaultNsChecker)
internal class CodeCompletionTsxYamlDefNsTest: CodeCompletionTestBase("tsx", "yml", ::DefaultNsChecker)
internal class CodeCompletionJsxYamlDefNsTest: CodeCompletionTestBase("jsx", "yml", ::DefaultNsChecker)
internal class CodeCompletionPhpYamlDefNsTest: CodeCompletionTestBasePhp("php", "yml", ::DefaultNsChecker)
internal class CodeCompletionVueJsonTest: CodeCompletionTestBase("vue", "json", ::VueChecker)
internal class CodeCompletionVueYamlTest: CodeCompletionTestBase("vue", "yml", ::VueChecker)