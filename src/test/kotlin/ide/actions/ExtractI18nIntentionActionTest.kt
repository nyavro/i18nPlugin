package ide.actions

import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.TestInputDialog
import com.intellij.testFramework.fixtures.BasePlatformTestCase


internal class ExtractI18nIntentionActionTest: BasePlatformTestCase() {

    private val translation = "assets/test.json"

    override fun getTestDataPath(): String = "src/test/resources/keyExtraction"

    fun doRun(src: String, trgt: String) {
        myFixture.configureByFiles(src, translation)
        val action = myFixture.findSingleIntention("Extract i18n key")
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

    fun testTsKeyExtraction() {
        doRun("ts/simple.ts", "ts/simpleKeyExtracted.ts")
    }

//    fun testJsKeyExtraction() {
//        doRun("js/simple.js", "js/simpleKeyExtracted.js")
//    }
}