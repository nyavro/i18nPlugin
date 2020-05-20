package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal abstract class CreateKeyTestBase(private val base: String): BasePlatformTestCase() {
    override fun getTestDataPath(): String = "src/test/resources/quickfix"

    private val testConfig = Config(jsonContentGenerationEnabled = base == "json", yamlContentGenerationEnabled = base == "yml")

    fun testTsxCreateKey() = myFixture.runWithConfig(testConfig) {
        val hint = "Create i18n key"
        myFixture.configureByFiles("tsx/simple.tsx", "assets/test.$base")
        val action = myFixture.filterAvailableIntentions(hint).find {ac -> ac.text == hint}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("assets/test.$base", "assets/testExpected.$base", false)
    }

    fun testTsxCreateKeyMultipleTranslations() = myFixture.runWithConfig(testConfig) {
        myFixture.configureByFiles("tsx/simple.tsx", "assets/ru/test.$base", "assets/en/test.$base")
        val action = myFixture.findSingleIntention("Create i18n key in all translation files")
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("assets/en/test.$base", "assets/testExpected.$base", false)
    }
}

internal class CreateKeyInJsonTest: CreateKeyTestBase("json")

internal class CreateKeyInYamlTest: CreateKeyTestBase("yml")