package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runCommonConfig
import com.eny.i18n.plugin.ide.runCommonConfigStr
import com.eny.i18n.plugin.ide.settings.CommonSettings
import org.junit.Test

internal abstract class FoldingTestBase(private val lang:String, private val translationLang:String): PlatformBaseTest() {

    override fun getTestDataPath(): String {
        return "src/test/resources/folding"
    }

    private val testConfig = arrayOf(
            Pair(CommonSettings::foldingPreferredLanguage, "en"),
            Pair(CommonSettings::foldingMaxLength, 20),
            Pair(CommonSettings::foldingEnabled, true)
    )

    @Test
    fun testFolding() = myFixture.runCommonConfig(
            Pair(CommonSettings::foldingPreferredLanguage, "en"),
            Pair(CommonSettings::foldingEnabled, true),
            Pair(CommonSettings::foldingMaxLength, 20)) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/simpleTest.$lang")
    }

    @Test
    fun testPreferredLanguage() = myFixture.runCommonConfig(
            Pair(CommonSettings::foldingPreferredLanguage, "ru"),
            Pair(CommonSettings::foldingEnabled, true),
            Pair(CommonSettings::foldingMaxLength, 26)) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/preferredLanguageTest.$lang")
    }

    @Test
    fun testIncompleteKey() = myFixture.runCommonConfig(
            Pair(CommonSettings::foldingPreferredLanguage, "en"),
            Pair(CommonSettings::foldingEnabled, true),
            Pair(CommonSettings::foldingMaxLength, 20)) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/incompleteKeys.$lang")
    }

    @Test
    fun testPreferredLanguageInvalidConfiguration() = myFixture.runCommonConfigStr(Pair(CommonSettings::foldingPreferredLanguage, "fr")) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/noFolding.$lang")
    }

    @Test
    fun testFoldingDisabled() = myFixture.runCommonConfig(Pair(CommonSettings::foldingEnabled, false)) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/noFolding.$lang")
    }

    @Test
    fun testDefaultNs() = myFixture.runCommonConfig(
            Pair(CommonSettings::foldingPreferredLanguage, "en"),
            Pair(CommonSettings::foldingEnabled, true),
            Pair(CommonSettings::foldingMaxLength, 20)
    ) {
        myFixture.configureByFiles("assets/ru/translation.$translationLang", "assets/en/translation.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/defaultTest.$lang")
    }

    @Test
    fun testPreferredLanguageDefaultNs() = myFixture.runCommonConfig(
            Pair(CommonSettings::foldingPreferredLanguage, "ru"),
            Pair(CommonSettings::foldingEnabled, true),
            Pair(CommonSettings::foldingMaxLength, 28)) {
        myFixture.configureByFiles("assets/ru/translation.$translationLang", "assets/en/translation.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/prefferedLangDefTest.$lang")
    }
}

internal class FoldingTestTsJson : FoldingTestBase("ts", "json")
internal class FoldingTestTsxJson : FoldingTestBase("tsx", "json")
internal class FoldingTestJsJson : FoldingTestBase("js", "json")
internal class FoldingTestJsxJson : FoldingTestBase("jsx", "json")
internal class FoldingTestPhpJson : FoldingTestBase("php", "json")
internal class FoldingTestVueJson : FoldingTestBase("vue", "json")
//TODO
//internal class FoldingTestVueYaml : FoldingTestBase("vue", "yml")

