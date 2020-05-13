package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.ide.SettingsPack
import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.ide.runVuePack
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal abstract class FoldingTestBase(private val lang:String, private val translationLang:String): BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/folding"
    }

    override fun setUp() {
        super.setUp()
        val settings = Settings.getInstance(myFixture.project)
        settings.foldingPreferredLanguage = "en"
        settings.foldingMaxLength = 20
        settings.foldingEnabled = true
    }

    fun testFolding() {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/simpleTest.$lang")
    }

    fun testPreferredLanguage() {
        Settings.getInstance(myFixture.project).let {
            it.foldingPreferredLanguage = "ru"
            it.foldingMaxLength = 26
        }
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/preferredLanguageTest.$lang")
    }

    fun testIncompleteKey() {
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/incompleteKeys.$lang")
    }

    fun testPreferredLanguageInvalidConfiguration() {
        Settings.getInstance(myFixture.project).foldingPreferredLanguage = "fr"
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/noFolding.$lang")
    }

    fun testFoldingDisabled() {
        Settings.getInstance(myFixture.project).foldingEnabled = false
        myFixture.configureByFiles("assets/ru/test.$translationLang", "assets/en/test.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/noFolding.$lang")
    }

    fun testDefaultNs() {
        myFixture.configureByFiles("assets/ru/translation.$translationLang", "assets/en/translation.$translationLang")
        myFixture.testFolding("$testDataPath/$lang/defaultTest.$lang")
    }

    fun testPreferredLanguageDefaultNs() {
        Settings.getInstance(myFixture.project).let {
            it.foldingPreferredLanguage = "ru"
            it.foldingMaxLength = 28
        }
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

    private val settingsPack = SettingsPack()
        .with(Settings::foldingPreferredLanguage, "en")
        .with(Settings::foldingMaxLength, 20)
        .with(Settings::foldingEnabled, true)

    override fun setUp() {
        super.setUp()
        val settings = Settings.getInstance(myFixture.project)
        settings.foldingPreferredLanguage = "en"
        settings.foldingMaxLength = 20
        settings.foldingEnabled = true
    }

    override fun tearDown() {
        val instance = Settings.getInstance(myFixture.project)
        instance.vue = false
        super.tearDown()
    }

    fun testFolding() = myFixture.runVuePack(settingsPack) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/simpleTestVue.vue")
    }

    fun testPreferredLanguage() = myFixture.runVuePack(
        SettingsPack()
            .with(Settings::foldingPreferredLanguage, "ru-RU")
            .with(Settings::foldingMaxLength, 26)
            .with(Settings::foldingEnabled, true)
    ) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/preferredLanguageTestVue.vue")
    }

    fun testPreferredLanguageInvalidConfiguration() = myFixture.runVuePack(
        SettingsPack().with(Settings::foldingPreferredLanguage, "fr")
    ) {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/noFoldingVue.vue")
    }

    fun testIncompleteKey() = myFixture.runVue {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/incompleteKeysVue.vue")
    }

    fun testFoldingDisabled() = myFixture.runVue {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/noFoldingVue.vue")
    }

    fun testFoldingParametrizedTranslation() = myFixture.runVue {
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/parametersTestVue.vue")
    }
}


internal class FoldingTestI18nVueJson : FoldingTestI18nVueBase("json")
internal class FoldingTestI18nVueYaml : FoldingTestI18nVueBase("yml")