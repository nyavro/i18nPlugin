package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config

class FoldingTestVue: PlatformBaseTest() {

    override fun getTestDataPath(): String {
        return "src/test/resources/folding"
    }

    private val testConfig = Config(foldingEnabled = true)

    private val translationLang = "json"

    fun testFolding() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/simpleTestVue.vue")
    }

    fun testFoldingVueNs() = myFixture.runVueConfig(testConfig.copy(firstComponentNs = true)) {
        myFixture.configureByFiles("locales/ru/common.$translationLang", "locales/en/common.$translationLang")
        myFixture.testFolding("$testDataPath/vue/vueNamespaces.vue")
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

