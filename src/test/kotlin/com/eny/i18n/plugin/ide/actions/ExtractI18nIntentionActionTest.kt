package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.TestDialog
import com.intellij.openapi.ui.TestInputDialog
import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class ExtractionTestBase: BasePlatformTestCase() {
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

    protected fun doRun(src: String, patched: String, translation: String, patchedTranslation: String, newKey: String) {
        doRun(src, patched, translation, patchedTranslation,
            object : TestInputDialog {
                override fun show(message: String): String? = null
                override fun show(message: String, validator: InputValidator?): String {
                    return newKey
                }
            }
        )
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
}

class ExtractionCancellation: ExtractionTestBase() {

    fun testTsCancel() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = false
        doCancel("js/simple.js", "assets/test.json")
    }

    fun testTsCancelInvalid() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = false
        doCancelInvalid("ts/simple.ts", "assets/test.json")
    }

    fun testExtractionUnavailable() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = false
        doUnavailable("tsx/unavailable.tsx")
    }

    fun testInvalidSource() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = false
        doRun("jsx/strange.jsx",
            "jsx/strangeKeyExtracted.jsx",
            "assets/test.json",
            "assets/testKeyExtracted.json",
            "test:ref.value3"
        )
    }
}

abstract class ExtractI18nIntentionActionBase(private val language: String, private val translationFormat: String): ExtractionTestBase() {

    fun testKeyExtraction() {
        doRun(
            "$language/simple.$language",
            "$language/simpleKeyExtracted.$language",
            "assets/test.$translationFormat",
            "assets/testKeyExtracted.$translationFormat",
            "test:ref.value3")
    }

    fun testDefNsKeyExtraction() {
        doRun(
            "$language/simple.$language",
            "$language/simpleDefNsKeyExtracted.$language",
            "assets/translation.$translationFormat",
            "assets/translationKeyExtracted.$translationFormat",
            "ref.value.sub1")
    }
}

class ExtractI18nIntentionActionJsJsonTest: ExtractI18nIntentionActionBase("js","json")
class ExtractI18nIntentionActionTsJsonTest: ExtractI18nIntentionActionBase("ts", "json")
class ExtractI18nIntentionActionTsxJsonTest: ExtractI18nIntentionActionBase("tsx", "json")
class ExtractI18nIntentionActionJsxJsonTest: ExtractI18nIntentionActionBase("jsx", "json")
class ExtractI18nIntentionActionJsYamlTest: ExtractI18nIntentionActionBase("js","yml")
class ExtractI18nIntentionActionTsYamlTest: ExtractI18nIntentionActionBase("ts", "yml")
class ExtractI18nIntentionActionTsxYamlTest: ExtractI18nIntentionActionBase("tsx", "yml")
class ExtractI18nIntentionActionJsxYamlTest: ExtractI18nIntentionActionBase("jsx", "yml")

abstract class ExtractI18nIntentionActionPhpBase(private val translationFormat: String): ExtractI18nIntentionActionBase("php", translationFormat) {

    fun testKeyExtractionSingleQuoted() {
        doRun(
            "php/simpleSingleQuoted.php",
            "php/simpleKeyExtracted.php",
            "assets/test.$translationFormat",
            "assets/testKeyExtracted.$translationFormat",
            "test:ref.value3")
    }

    fun testDefNsKeyExtractionSingleQuoted() {
        doRun(
            "php/simpleSingleQuoted.php",
            "php/simpleDefNsKeyExtracted.php",
            "assets/translation.$translationFormat",
            "assets/translationKeyExtracted.$translationFormat",
            "ref.value.sub1")
    }
}
class ExtractI18nIntentionActionPhpJsonTest: ExtractI18nIntentionActionPhpBase("json")
class ExtractI18nIntentionActionPhpYamlTest: ExtractI18nIntentionActionPhpBase("yml")

abstract class ExtractI18nIntentionActionVueI18nBase(private val translationFormat: String): ExtractionTestBase() {
//
//    override fun setUp() {
//        super.setUp()
//        val settings = Settings.getInstance(myFixture.project)
//        settings.vue = true
//    }

    fun testKeyExtraction() {
        val settings = Settings.getInstance(myFixture.project)
        settings.vue = true
        doRun(
            "vue/simpleVue.vue",
            "vue/simpleKeyExtractedVue.vue",
            "locales/en-US.$translationFormat",
            "locales/en-USKeyExtracted.$translationFormat",
            "ref.value3"
        )
        settings.vue = false
    }

//    fun testKeyExtraction2() {
//        doRun(
//            "vue/App.vue",
//            "vue/AppExtracted.vue",
//            "locales/en-US.$translationFormat",
//            "locales/en-USKeyExtracted.$translationFormat",
//            "ref.value3"
//        )
//        val settings = Settings.getInstance(myFixture.project)
//        settings.vue = false
//    }
//
//    fun testScriptKeyExtraction() {
//        doRun(
//            "vue/scriptVue.vue",
//            "vue/scriptKeyExtractedVue.vue",
//            "locales/en-US.$translationFormat",
//            "locales/en-USKeyExtracted.$translationFormat",
//            "ref.value3"
//        )
//        val settings = Settings.getInstance(myFixture.project)
//        settings.vue = false
//    }
}

class ExtractI18nIntentionActionVueI18nJsonTest: ExtractI18nIntentionActionVueI18nBase("json")
//class ExtractI18nIntentionActionVueI18nYamlTest: ExtractI18nIntentionActionVueI18nBase("yml")