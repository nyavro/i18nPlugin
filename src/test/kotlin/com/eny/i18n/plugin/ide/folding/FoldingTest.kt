package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class FoldingTest : BasePlatformTestCase() {

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
        myFixture.configureByFiles("assets/ru/test.json", "assets/en/test.json")
        myFixture.testFolding("$testDataPath/ts/simpleTest.ts")
    }

    fun testPreferredLanguage() {
        Settings.getInstance(myFixture.project).let {
            it.foldingPreferredLanguage = "ru"
            it.foldingMaxLength = 26
        }
        myFixture.configureByFiles("assets/ru/test.json", "assets/en/test.json")
        myFixture.testFolding("$testDataPath/ts/preferredLanguageTest.ts")
    }

    fun testPreferredInvalidLanguage() {
        Settings.getInstance(myFixture.project).foldingPreferredLanguage = "fr"
        myFixture.configureByFiles("assets/ru/test.json", "assets/en/test.json")
        myFixture.testFolding("$testDataPath/ts/noFolding.ts")
    }

//    fun testFoldingDisabled() {
//        Settings.getInstance(myFixture.project).foldingEnabled = false
//        myFixture.configureByFiles("assets/ru/test.json", "assets/en/test.json")
//        myFixture.testFolding("$testDataPath/ts/noFolding.ts")
//    }
}
