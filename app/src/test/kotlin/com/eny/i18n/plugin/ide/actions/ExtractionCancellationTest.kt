package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.TestDialog
import com.intellij.openapi.ui.TestInputDialog
import org.junit.Ignore
import org.junit.Test

@Ignore
class ExtractionCancellationTest: PlatformBaseTest() {

    private val hint = "Extract i18n key"

    private val simpleJs = """
        export const test = () => {
            const key = "<caret>I want to move it to translation";
        };
    """

    private val testJson = JsonTranslationGenerator().generateContent("ref", "section", "key", "Reference in Json")

    @Test
    fun testCancel() {
        myFixture.configureByText("simple.js", simpleJs)
        myFixture.addFileToProject("assets/test.json", testJson)
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?) = null
        })
        myFixture.launchAction(action)
        myFixture.checkResult(simpleJs)
        myFixture.checkResult("assets/test.json", testJson, false)
    }

    @Test
    fun testCancelInvalid() {
        myFixture.configureByText("simple.js", simpleJs)
        myFixture.addFileToProject("assets/test.json", testJson)
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?) = "not:a:key{here}"
        })
        Messages.setTestDialog(object : TestDialog {
            override fun show(message: String): Int {
                assertEquals("Invalid i18n key", message)
                return 1
            }
        })
        myFixture.launchAction(action)
        myFixture.checkResult(simpleJs)
        myFixture.checkResult("assets/test.json", testJson, false)
    }

    @Test
    fun testExtractionUnavailable() {
        myFixture.configureByText(
            "unavailable.tsx",
            """
                export const test = () => {
                    return (
                        <div>
                            <div>text</div>
                            <caret>
                            <div>footer</div>
                        </div>);
                };
            """
        )
        assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
    }
}