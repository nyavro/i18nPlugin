package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.CodeGeneratorsWithNs
import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueScriptCodeGenerator
import com.intellij.codeInsight.intention.IntentionAction
import org.junit.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class KeyContextTest: PlatformBaseTest() {

    private val hint = "Extract i18n key"

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratorsWithNs::class)
    fun testKeyContext(codeGenerator: CodeGenerator, defaultNs: Boolean) {
        val key = if (defaultNs) "\"ref<caret>.value.sub1\"" else "\"test:ref<caret>.value.sub1\""
        myFixture.configureByText("keyContext.${codeGenerator.ext()}", codeGenerator.generate(key))
        assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
    }
}

class KeyContextVueTest: PlatformBaseTest() {

    private val hint = "Extract i18n key"

    private val codeGenerator = VueCodeGenerator()

    @Test
    fun testKeyContextScript() {
        val cg = VueScriptCodeGenerator()
        myFixture.runVue {
            myFixture.configureByText("keyContextScript.${cg.ext()}", cg.generateBlock("this.\$t('ref.<caret>value3')"))
            assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
        }
    }

    @Test
    fun testKeyContext() {
        myFixture.runVue {
            myFixture.configureByText("keyContext.${codeGenerator.ext()}", codeGenerator.generate("\"ref<caret>.value.sub1\""))
            assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
        }
    }
}