package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.utils.generator.code.*
import com.intellij.codeInsight.intention.IntentionAction

class KeyContextJsDs : KeyContextDefaultNsTestBase(JsCodeGenerator())
class KeyContextTsDs : KeyContextDefaultNsTestBase(TsCodeGenerator())
class KeyContextTsxDs : KeyContextDefaultNsTestBase(TsxCodeGenerator())
class KeyContextJsxDs : KeyContextDefaultNsTestBase(JsxCodeGenerator())
class KeyContextJs : KeyContextTestBase(JsCodeGenerator())
class KeyContextTs : KeyContextTestBase(TsCodeGenerator())
class KeyContextTsx : KeyContextTestBase(TsxCodeGenerator())
class KeyContextJsx : KeyContextTestBase(JsxCodeGenerator())

/**
 * Should not suggest extractions inside key
 */
abstract class KeyContextDefaultNsTestBase(private val cg: CodeGenerator): PlatformBaseTest() {

    private val hint = "Extract i18n key"

    fun testKeyContext() {
        val key = "\"ref<caret>.value.sub1\""
        myFixture.configureByText("keyContext.${cg.ext()}", cg.generate(key))
        assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
    }
}

abstract class KeyContextTestBase(private val cg: CodeGenerator): PlatformBaseTest() {

    private val hint = "Extract i18n key"

    fun testKeyContext() {
        val key = "\"test:ref<caret>.value.sub1\""
        myFixture.configureByText("keyContext.${cg.ext()}", cg.generate(key))
        assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
    }
}