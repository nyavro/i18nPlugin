package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.utils.generator.code.*
import com.intellij.codeInsight.intention.IntentionAction
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import kotlin.concurrent.thread

class KeyContextTest: PlatformBaseTest() {

    private val hint = "Extract i18n key"

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratorsWithNs::class)
    fun testKeyContext(codeGenerator: CodeGenerator, defaultNs: Boolean) {
        thread {
            val key = if (defaultNs) "\"ref<caret>.value.sub1\"" else "\"test:ref<caret>.value.sub1\""
            myFixture.configureByText("keyContext.${codeGenerator.ext()}", codeGenerator.generate(key))
            assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
        }
    }
}

class KeyContextVueTest: PlatformBaseTest() {

    private val hint = "Extract i18n key"

    private val codeGenerator = VueCodeGenerator()

    @Test
    fun testKeyContextScript() {
        thread {
            myFixture.runVue {
                myFixture.configureByText("keyContextScript.${codeGenerator.ext()}", codeGenerator.generateScript("this.\$t('ref.<caret>value3')"))
                assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
            }
        }
    }
}