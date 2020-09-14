package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import org.junit.jupiter.api.Test

internal abstract class CreateKeyTestBase(private val base: String): PlatformBaseTest() {
    override fun getTestDataPath(): String = "src/test/resources/quickfix"

    private val testConfig = Config(jsonContentGenerationEnabled = base == "json", yamlContentGenerationEnabled = base == "yml")

    @Test
    fun testTsxCreateKey() = myFixture.runWithConfig(testConfig) {
        val hint = "Create i18n key"
        myFixture.configureByFiles("tsx/simple.tsx", "assets/test.$base")
        val action = myFixture.filterAvailableIntentions(hint).find {ac -> ac.text == hint}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("assets/test.$base", "assets/testExpected.$base", false)
    }

    @Test
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