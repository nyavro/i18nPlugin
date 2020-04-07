package com.eny.i18n.plugin.ide.quickfix

import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class CreateKeyTest: BasePlatformTestCase() {

    private val hint = "Create i18n key"

    override fun getTestDataPath(): String = "src/test/resources/quickfix"

    fun testTsxCreateKey() {
        myFixture.configureByFiles("tsx/simple.tsx", "assets/test.json")
        val action = myFixture.filterAvailableIntentions(hint).find {ac -> ac.text == hint}!!
        assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("assets/test.json", "assets/testExpected.json", false)
    }
}