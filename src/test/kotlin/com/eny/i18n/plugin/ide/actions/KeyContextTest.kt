package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.CodeGeneratorsWithNs
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.intellij.codeInsight.intention.IntentionAction
import org.junit.jupiter.api.Test
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

    @Test
    fun testKeyContext1() {}
}