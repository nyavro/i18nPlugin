package com.eny.i18n.plugin.ide.folding

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

    fun testFolding() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/simpleTestVue.vue")
        settings.vue = false
    }

    fun testPreferredLanguage() {
        val settings = Settings.getInstance(myFixture.project)
        settings.foldingPreferredLanguage = "ru-RU"
        settings.foldingMaxLength = 26
        settings.vue = true
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/preferredLanguageTestVue.vue")
        settings.vue = false
    }

    fun testPreferredLanguageInvalidConfiguration() {
        val settings = Settings.getInstance(myFixture.project)
        settings.foldingPreferredLanguage = "fr"
        settings.vue = true
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/noFoldingVue.vue")
        settings.vue = false
    }

    fun testIncompleteKey() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/incompleteKeysVue.vue")
        settings.vue = false
    }

    fun testFoldingDisabled() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/noFoldingVue.vue")
        settings.vue = false
    }

    fun testFoldingParametrizedTranslation() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        myFixture.configureByFiles("locales/ru-RU.$translationLang", "locales/en-US.$translationLang")
        myFixture.testFolding("$testDataPath/vue/parametersTestVue.vue")
        settings.vue = false
    }
}


internal class FoldingTestI18nVueJson : FoldingTestI18nVueBase("json")
internal class FoldingTestI18nVueYaml : FoldingTestI18nVueBase("yml")