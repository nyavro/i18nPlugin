package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class FoldingTestVue: PlatformBaseTest() {

    override fun getTestDataPath(): String {
        return "src/test/resources/folding"
    }

    private val testConfig = Config(foldingEnabled = true)

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testFolding(translationLang:String) = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/simpleTestVue.vue")
    }

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testPreferredLanguage(translationLang:String) = myFixture.runVueConfig(
            Config(foldingPreferredLanguage = "ru-RU", foldingMaxLength = 26, foldingEnabled = true)
    ) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/preferredLanguageTestVue.vue")
    }

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testPreferredLanguageInvalidConfiguration(translationLang:String) = myFixture.runVueConfig(
            Config(foldingPreferredLanguage = "fr", foldingEnabled = true)
    ) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/noFoldingVue.vue")
    }

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testIncompleteKey(translationLang:String) = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/incompleteKeysVue.vue")
    }

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testFoldingDisabled(translationLang:String) = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/noFoldingVue.vue")
    }

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testFoldingParametrizedTranslation(translationLang: String) = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/parametersTestVue.vue")
    }
}

