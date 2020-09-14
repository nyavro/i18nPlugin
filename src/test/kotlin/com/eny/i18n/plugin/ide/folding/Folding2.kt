package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class Folding2: BasePlatformTestCase() {

    private val testConfig = Config(foldingPreferredLanguage = "en", foldingMaxLength = 20, foldingEnabled = true)

    override fun getTestDataPath(): String {
        return "src/test/resources/folding"
    }

    @BeforeEach
    fun setFixtureUp() {
        super.setUp()
    }

    @AfterEach
    fun tearFixtureDown() {
        super.tearDown()
    }

    @ParameterizedTest
    @ValueSource(strings = ["js", "ts"])
    fun testFolding2(ext: String) = myFixture.runWithConfig(testConfig) {
        myFixture.configureByFiles("assets/ru/test.json", "assets/en/test.json")
        myFixture.testFolding("$testDataPath/$ext/simpleTest.$ext")
    }
}