package com.eny.i18n.plugin.ide.actions

import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.TestDialog
import com.intellij.openapi.ui.TestInputDialog
import com.intellij.testFramework.fixtures.BasePlatformTestCase


internal class ExtractI18nIntentionActionTest: BasePlatformTestCase() {

    private val hint = "Extract i18n key"

    override fun getTestDataPath(): String = "src/test/resources/keyExtraction"

    private fun doRun(
            src: String,
            trgt: String,
            origTranslation: String,
            patchedTranslation: String,
            inputDialog: TestInputDialog,
            message: TestDialog? = null) {
        myFixture.configureByFiles(src, origTranslation)
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(inputDialog)
        if (message != null) Messages.setTestDialog(message)
        myFixture.launchAction(action)
        myFixture.checkResultByFile(trgt)
        myFixture.checkResultByFile(origTranslation, patchedTranslation, false)
    }

    private fun doRun(src: String, trgt: String, translation: String, patchedTranslation: String, newKey: String) {
        doRun(src, trgt, translation, patchedTranslation,
            object : TestInputDialog {
                override fun show(message: String): String? = null
                override fun show(message: String, validator: InputValidator?): String {
                    return newKey
                }
            }
        )
    }

    private fun doCancel(src: String, translation: String) {
        doRun(src, src, translation, translation,
            object : TestInputDialog {
                override fun show(message: String): String? = null
                override fun show(message: String, validator: InputValidator?) = null
            }
        )
    }

    private fun doCancelInvalid(src: String, translation: String) {
        doRun(src, src, translation, translation,
            object : TestInputDialog {
                override fun show(message: String): String? = null
                override fun show(message: String, validator: InputValidator?) = "not:a:key{here}"
            },
            object: TestDialog {
                override fun show(message: String): Int {
                    assertEquals("Invalid i18n key", message)
                    return 1
                }
            }
        )
    }

    fun testTsKeyExtraction() {
        doRun(
            "ts/simple.ts",
            "ts/simpleKeyExtracted.ts",
            "assets/test.json",
            "assets/testKeyExtracted.json",
            "test:ref.value3")
    }

    fun testTsDefNsKeyExtraction() {
        doRun(
            "ts/simpleDefNs.ts",
            "ts/simpleDefNsKeyExtracted.ts",
            "assets/translation.json",
            "assets/translationKeyExtracted.json",
            "ref.value.sub1")
    }

    fun testJsxKeyExtraction() {
        doRun("jsx/simple.jsx", "jsx/simpleKeyExtracted.jsx", "assets/test.json", "assets/testKeyExtracted.json", "test:ref.value3")
    }

    fun testTsCancel() {
        doCancel("ts/simple.ts", "assets/test.json")
    }

    fun testJsKeyExtraction() {
        doRun("js/simple.js", "js/simpleKeyExtracted.js", "assets/test.json", "assets/testKeyExtracted.json", "test:ref.value3")
    }

    fun testTsCancelInvalid() {
        doCancelInvalid("ts/simple.ts", "assets/test.json")
    }
}