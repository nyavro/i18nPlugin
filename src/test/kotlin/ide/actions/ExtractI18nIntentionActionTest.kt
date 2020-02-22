package ide.actions

import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.TestInputDialog
import com.intellij.testFramework.fixtures.BasePlatformTestCase


internal class ExtractI18nIntentionActionTest: BasePlatformTestCase() {

    private val translation = "assets/test.json"

    private val hint = "Extract i18n key"

    override fun getTestDataPath(): String = "src/test/resources/keyExtraction"

    fun doRun(src: String, trgt: String) {
        myFixture.configureByFiles(src, translation)
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?): String {
                return "test:ref.value3"
            }
        })
        myFixture.launchAction(action)
        myFixture.checkResultByFile(trgt)
        myFixture.checkResultByFile(translation, "assets/testKeyExtracted.json", false)
    }

    fun doCancel(src: String) {
        myFixture.configureByFiles(src, translation)
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?) = null
        })
        myFixture.launchAction(action)
        myFixture.checkResultByFile(src)
        myFixture.checkResultByFile(translation, translation, false)
    }

    fun cancelInvalid(src: String) {
        myFixture.configureByFiles(src, translation)
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?) = "not:a/key{here}"
        })
        myFixture.launchAction(action)
        myFixture.checkResultByFile(src)
        myFixture.checkResultByFile(translation, translation, false)
    }

    fun testTsKeyExtraction() {
        doRun("ts/simple.ts", "ts/simpleKeyExtracted.ts")
    }

    fun testTsCancel() {
        doCancel("ts/simple.ts")
    }

    fun testJsKeyExtraction() {
        doRun("js/simple.js", "js/simpleKeyExtracted.js")
    }

    fun testTsCancelInvalid() {
        doCancel("ts/simple.ts")
    }
}