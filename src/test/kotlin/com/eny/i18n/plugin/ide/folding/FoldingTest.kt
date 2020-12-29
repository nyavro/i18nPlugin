package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runCommonConfig
import com.eny.i18n.plugin.ide.settings.CommonSettings
import org.junit.jupiter.api.Test

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
    fun testFolding() = myFixture.runCommonConfig(*testConfig) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/simpleTest.$lang")
    }

    @Test
    fun testPreferredLanguage() = myFixture.runCommonConfig(
            Pair(CommonSettings::foldingPreferredLanguage, "ru"),
            Pair(CommonSettings::foldingMaxLength, 26),
            Pair(CommonSettings::foldingEnabled, true)) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/preferredLanguageTest.$lang")
    }

    @Test
    fun testIncompleteKey() = myFixture.runCommonConfig(*testConfig) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/incompleteKeys.$lang")
    }

    @Test
    fun testPreferredLanguageInvalidConfiguration() = myFixture.runCommonConfig(Pair(CommonSettings::foldingPreferredLanguage, "fr")) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/noFolding.$lang")
    }

    @Test
    fun testFoldingDisabled() = myFixture.runCommonConfig(Pair(CommonSettings::foldingEnabled, false)) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/noFolding.$lang")
    }

    @Test
    fun testDefaultNs() = myFixture.runCommonConfig(*testConfig) {
        myFixture.configureByFiles("assets/ru/translation.$translationLang", "assets/en/translation.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/defaultTest.$lang")
    }

    @Test
    fun testPreferredLanguageDefaultNs() = myFixture.runCommonConfig(
            Pair(CommonSettings::foldingPreferredLanguage, "ru"),
            Pair(CommonSettings::foldingMaxLength, 28),
            Pair(CommonSettings::foldingEnabled, true)) {
        myFixture.configureByFiles("assets/ru/translation.$translationLang", "assets/en/translation.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/prefferedLangDefTest.$lang")
    }
}

internal class FoldingTestTsJson : FoldingTestBase("ts", "json")
internal class FoldingTestTsYaml : FoldingTestBase("ts", "yml")
internal class FoldingTestTsxJson : FoldingTestBase("tsx", "json")
internal class FoldingTestTsxYaml : FoldingTestBase("tsx", "yml")
internal class FoldingTestJsJson : FoldingTestBase("js", "json")
internal class FoldingTestJsYaml : FoldingTestBase("js", "yml")
internal class FoldingTestJsxJson : FoldingTestBase("jsx", "json")
internal class FoldingTestJsxYaml : FoldingTestBase("jsx", "yml")
internal class FoldingTestPhpJson : FoldingTestBase("php", "json")
internal class FoldingTestPhpYaml : FoldingTestBase("php", "yml")
internal class FoldingTestVueJson : FoldingTestBase("vue", "json")
internal class FoldingTestVueYaml : FoldingTestBase("vue", "yml")

