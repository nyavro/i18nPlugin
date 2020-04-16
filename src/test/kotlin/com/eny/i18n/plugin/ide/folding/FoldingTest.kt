package com.eny.i18n.plugin.ide.folding

import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class FoldingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/resources/folding"
    }

    fun testFolding() {
        myFixture.configureByFiles("ts/simple.ts", "assets/en/test.json")
        myFixture.testFolding(getTestDataPath() + "/ts/simpleTest.ts")
    }
}
