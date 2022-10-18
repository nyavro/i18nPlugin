package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.settings.Config
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.TestDialog
import com.intellij.openapi.ui.TestDialogManager.setTestDialog
import com.intellij.openapi.ui.TestDialogManager.setTestInputDialog
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
        if (!ApplicationManager.getApplication().isReadAccessAllowed()) return
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        setTestInputDialog(inputDialog)
        if (message != null) setTestDialog(message)
        myFixture.launchAction(action)
        myFixture.checkResult(patched)
        myFixture.checkResult(translationName, patchedTranslation, false)
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