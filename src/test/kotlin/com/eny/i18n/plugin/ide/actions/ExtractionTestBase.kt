package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.PlatformBaseTest
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.TestDialog
import com.intellij.openapi.ui.TestInputDialog

abstract class ExtractionTestBase: PlatformBaseTest() {
    private val hint = "Extract i18n key"

    override fun getTestDataPath(): String = "src/test/resources/keyExtraction"

    protected fun doRun(
            src: String,
            patched: String,
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
        myFixture.checkResultByFile(patched)
        myFixture.checkResultByFile(origTranslation, patchedTranslation, false)
    }

    protected fun doUnavailable(src: String) {
        myFixture.configureByFile(src)
        assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
    }

    protected fun doUnavailable(fileName: String, code: String) {
        myFixture.configureByText(fileName, code)
        assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
    }

    protected fun doRun(src: String, patched: String, translation: String, patchedTranslation: String, newKey: String) {
        doRun(src, patched, translation, patchedTranslation, predefinedTextInputDialog(newKey))
    }

    protected fun doCancel(src: String, translation: String) {
        doRun(src, src, translation, translation,
            object : TestInputDialog {
                override fun show(message: String): String? = null
                override fun show(message: String, validator: InputValidator?) = null
            }
        )
    }

    protected fun doCancelInvalid(src: String, translation: String) {
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

    protected fun doRunUnknownNs(
            src: String,
            patched: String,
            translationCreated: String,
            translationExpected: String,
            newKey: String,
            extraFile: String? = null) {
        if (extraFile != null) {
            myFixture.configureByFiles(src, extraFile)
        } else {
            myFixture.configureByFiles(src)
        }
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(predefinedTextInputDialog(newKey))
        myFixture.launchAction(action)
        myFixture.checkResultByFile(patched)
        myFixture.checkResultByFile(translationCreated, translationExpected, false)
    }

    private fun predefinedTextInputDialog(newKey: String): TestInputDialog {
        return object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?): String {
                return newKey
            }
        }
    }
}