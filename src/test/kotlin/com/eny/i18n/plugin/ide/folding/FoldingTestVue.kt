package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.TranslationGenerators
import com.eny.i18n.plugin.ide.runCommonConfig
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.CommonSettings
import com.eny.i18n.plugin.ide.settings.VueSettings
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.ValueSource

class FoldingTestVue: PlatformBaseTest() {

    override fun getTestDataPath(): String {
        return "src/test/resources/folding"
    }

    private val testConfig = Pair(CommonSettings::foldingEnabled, true)

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testFolding(translationLang:String) = myFixture.runVueConfig(Pair(VueSettings::vue, true)) {
        myFixture.runCommonConfig(testConfig) {
            myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
            myFixture.testFolding("$testDataPath/vue/simpleTestVue.vue")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testPreferredLanguage(translationLang:String) = myFixture.runVueConfig(Pair(VueSettings::vue, true)) {
        myFixture.runCommonConfig(
            Pair(CommonSettings::foldingEnabled, true),
            Pair(CommonSettings::foldingPreferredLanguage, "ru-RU"),
            Pair(CommonSettings::foldingMaxLength, 26)
    ) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/preferredLanguageTestVue.vue")
    }
    }

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testPreferredLanguageInvalidConfiguration(translationLang:String) = myFixture.runVueConfig(Pair(VueSettings::vue, true)) {
        myFixture.runCommonConfig(
                Pair(CommonSettings::foldingEnabled, true),
                Pair(CommonSettings::foldingPreferredLanguage, "fr")
        ) {
            myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
            myFixture.testFolding("$testDataPath/vue/noFoldingVue.vue")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testIncompleteKey(translationLang:String) = myFixture.runVueConfig(Pair(VueSettings::vue, true)) {
        myFixture.runCommonConfig(testConfig) {
            myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
            myFixture.testFolding("$testDataPath/vue/incompleteKeysVue.vue")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testFoldingDisabled(translationLang:String) = myFixture.runVueConfig(Pair(VueSettings::vue, true)) {
        myFixture.runCommonConfig(testConfig) {
            myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
            myFixture.testFolding("$testDataPath/vue/noFoldingVue.vue")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["json", "yml"])
    fun testFoldingParametrizedTranslation(translationLang: String) = myFixture.runVueConfig(Pair(VueSettings::vue, true)) {
        myFixture.runCommonConfig(testConfig) {
            myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
            myFixture.testFolding("$testDataPath/vue/parametersTestVue.vue")
        }
    }
}

