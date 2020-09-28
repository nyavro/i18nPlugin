package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.settings.Config
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.TestDialog
import com.intellij.openapi.ui.TestInputDialog

abstract class ExtractionTestBase: PlatformBaseTest() {

    protected val hint = "Extract i18n key"

    override fun getTestDataPath(): String = "src/test/resources/keyExtraction"

    protected fun config(ext: String, extractSorted: Boolean = false) =
            Config(yamlContentGenerationEnabled = ext == "yml",
                    jsonContentGenerationEnabled = ext == "json",
                    preferYamlFilesGeneration = ext == "yml",
                    extractSorted = extractSorted
            )

    protected fun runTestCase(
            srcName: String,
            src: String,
            patched: String,
            translationName: String,
            origTranslation: String,
            patchedTranslation: String,
            inputDialog: TestInputDialog,
            message: TestDialog? = null) {
        myFixture.configureByText(srcName, src)
        myFixture.addFileToProject(translationName, origTranslation)
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(inputDialog)
        if (message != null) Messages.setTestDialog(message)
        myFixture.launchAction(action)
        myFixture.checkResult(patched)
        myFixture.checkResult(translationName, patchedTranslation, false)
    }

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

    protected fun doRun(src: String, patched: String, translation: String, patchedTranslation: String, newKey: String) {
        doRun(src, patched, translation, patchedTranslation, predefinedTextInputDialog(newKey))
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

    protected fun predefinedTextInputDialog(newKey: String): TestInputDialog {
        return object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?): String {
                return newKey
            }
        }
    }
}