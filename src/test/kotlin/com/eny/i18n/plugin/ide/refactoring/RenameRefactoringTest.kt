package com.eny.i18n.plugin.ide.refactoring

import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class RenameRefactoringTest: BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/resources/refactoring"

    fun testRenameLeaf() {
        myFixture.configureByFiles("ts/usage1.ts", "assets/simple.json")
        myFixture.renameElementAtCaret("leaf2")
        myFixture.checkResultByFile("assets/simple.json", "assets/simpleRenamed.json", false)
//        myFixture.checkResultByFile("ts/usage1.ts", "ts/usage1Renamed.ts", false)
    }
}