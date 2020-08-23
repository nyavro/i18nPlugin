package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.utils.generator.code.JsCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.TestDialog
import com.intellij.openapi.ui.TestInputDialog

class ExtractionCancellationTest: ExtractionTestBase() {

    private val hint = "Extract i18n key"

    private val cg = listOf(::JsCodeGenerator).random()()
    private val tg = listOf(::JsonTranslationGenerator).random()()
    private val code = cg.generateCodeForExtraction("<caret>I want to move it to translation")
    private val translation = tg.generateContent("ref", "key", "translation value")

    fun testTsCancel() {
        val inputDialog = object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?) = null
        }
        myFixture.configureByText("simple.${cg.ext()}", code)
        myFixture.addFileToProject("assets/test.${tg.ext()}", translation)
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(inputDialog)
        myFixture.launchAction(action)
        myFixture.checkResult(code)
        myFixture.checkResult("assets/test.json", translation, false)
    }

    fun testCancelInvalid() {
        val inputDialog = object : TestInputDialog {
            override fun show(message: String): String? = null
            override fun show(message: String, validator: InputValidator?) = "not:a:key{here}"
        }
        val message = object : TestDialog {
            override fun show(message: String): Int {
                assertEquals("Invalid i18n key", message)
                return 1
            }
        }
        myFixture.configureByText("simple.${cg.ext()}", code)
        myFixture.addFileToProject("assets/test.${tg.ext()}", translation)
        val action = myFixture.findSingleIntention(hint)
        assertNotNull(action)
        Messages.setTestInputDialog(inputDialog)
        Messages.setTestDialog(message)
        myFixture.launchAction(action)
        myFixture.checkResult(code)
        myFixture.checkResult("assets/test.${tg.ext()}", translation, false)
    }

    fun testExtractionUnavailable() {
        myFixture.configureByFile("tsx/unavailable.tsx")
        assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
    }

    fun testInvalidSource() {
        doRun("jsx/strange.jsx",
            "jsx/strangeKeyExtracted.jsx",
            "assets/test.json",
            "assets/testKeyExtracted.json",
            "test:ref.value3"
        )
    }
}

