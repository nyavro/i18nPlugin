package com.eny.i18n.plugin.addons.technology.vue

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.intellij.codeInsight.intention.IntentionAction
//@TODO disabled test 6
class KeyContextVueTest: PlatformBaseTest() {

    private val hint = "Extract i18n key"

    private val codeGenerator = VueCodeGenerator()

    fun disableTestKeyContextScript() {
        myFixture.runVueConfig {
            myFixture.configureByText("keyContextScript.${codeGenerator.ext()}", codeGenerator.generateScript("this.\$t('ref.<caret>value3')"))
            assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
        }
    }

    fun disabledTestKeyContext() {
        myFixture.runVueConfig {
            myFixture.configureByText("keyContext.${codeGenerator.ext()}", codeGenerator.generate("\"ref<caret>.value.sub1\""))
            assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
        }
    }
}

