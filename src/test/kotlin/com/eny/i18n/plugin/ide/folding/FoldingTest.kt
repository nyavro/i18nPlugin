package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal abstract class FoldingTestBase(private val lang:String, private val translationLang:String): BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/folding"
    }

    private val testConfig = Config(foldingPreferredLanguage = "en", foldingMaxLength = 20, foldingEnabled = true)
    
    fun testFolding() = myFixture.runWithConfig(testConfig) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/simpleTest.$lang")
    }

    fun testPreferredLanguage() = myFixture.runWithConfig(Config(foldingPreferredLanguage = "ru" ,foldingMaxLength = 26, foldingEnabled = true)) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/preferredLanguageTest.$lang")
    }

    fun testIncompleteKey() = myFixture.runWithConfig(testConfig) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/incompleteKeys.$lang")
    }

    fun testPreferredLanguageInvalidConfiguration() = myFixture.runWithConfig(Config(foldingPreferredLanguage = "fr")) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/noFolding.$lang")
    }

    fun testFoldingDisabled() = myFixture.runWithConfig(Config(foldingEnabled = false)) {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/noFolding.$lang")
    }

    fun testDefaultNs() = myFixture.runWithConfig(testConfig) {
        myFixture.configureByFiles("assets/ru/translation.$translationLang", "assets/en/translation.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/defaultTest.$lang")
    }

    fun testPreferredLanguageDefaultNs() = myFixture.runWithConfig(
            Config(foldingPreferredLanguage = "ru", foldingMaxLength = 28, foldingEnabled = true)
    ) {
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

internal abstract class FoldingTestI18nVueBase(private val translationLang:String): BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/folding"
    }

    private val testConfig = Config(foldingEnabled = true)

    fun testFolding() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/simpleTestVue.vue")
    }

    fun testPreferredLanguage() = myFixture.runVueConfig(
            Config(foldingPreferredLanguage = "ru-RU", foldingMaxLength = 26, foldingEnabled = true)
    ) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/preferredLanguageTestVue.vue")
    }

    fun testPreferredLanguageInvalidConfiguration() = myFixture.runVueConfig(
        Config(foldingPreferredLanguage = "fr", foldingEnabled = true)
    ) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/noFoldingVue.vue")
    }

    fun testIncompleteKey() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/incompleteKeysVue.vue")
    }

    fun testFoldingDisabled() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/noFoldingVue.vue")
    }

    fun testFoldingParametrizedTranslation() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/parametersTestVue.vue")
    }
}


internal class FoldingTestI18nVueJson : FoldingTestI18nVueBase("json")
internal class FoldingTestI18nVueYaml : FoldingTestI18nVueBase("yml")