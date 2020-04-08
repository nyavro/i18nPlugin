package com.eny.i18n.plugin.ide.quickfix

import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class CreateKeyTest: BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/resources/quickfix"

    fun testTsxCreateKey() {
        val hint = "Create i18n key"
        myFixture.configureByFiles("tsx/simple.tsx", "assets/test.json")
        val action = myFixture.filterAvailableIntentions(hint).find {ac -> ac.text == hint}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("assets/test.json", "assets/testExpected.json", false)
    }

    fun testTsxCreateKeyMultipleTranslations() {
        myFixture.configureByFiles("tsx/simple.tsx", "assets/ru/test.json", "assets/en/test.json")
        val action = myFixture.findSingleIntention("Create i18n key in all translation files")
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("assets/en/test.json", "assets/testExpected.json", false)
        myFixture.checkResultByFile("assets/ru/test.json", "assets/testExpected.json", false)
    }
}