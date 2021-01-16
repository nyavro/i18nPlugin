package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.PlatformBaseTest
//import com.eny.i18n.plugin.ide.CodeGeneratorsWithNs
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.intellij.codeInsight.intention.IntentionAction
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

@Ignore
class KeyContextTest: PlatformBaseTest() {

    // @TODO 10

    private val hint = "Extract i18n key"

    @Test
    fun testStub1() {
        Assert.assertTrue(true)
    }

//    @ParameterizedTest
//    @ArgumentsSource(CodeGeneratorsWithNs::class)
//    fun testKeyContext(codeGenerator: CodeGenerator, defaultNs: Boolean) {
//        val key = if (defaultNs) "\"ref<caret>.value.sub1\"" else "\"test:ref<caret>.value.sub1\""
//        myFixture.configureByText("keyContext.${codeGenerator.ext()}", codeGenerator.generate(key))
//        assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
//    }
}

class KeyContextVueTest: PlatformBaseTest() {

    private val hint = "Extract i18n key"

    private val codeGenerator = VueCodeGenerator()

    @Test
    fun testKeyContextScript() {
        myFixture.runVueConfig {
            myFixture.configureByText("keyContextScript.${codeGenerator.ext()}", codeGenerator.generateScript("this.\$t('ref.<caret>value3')"))
            assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
        }
    }

    @Test
    fun testKeyContext() {
        myFixture.runVueConfig {
            myFixture.configureByText("keyContext.${codeGenerator.ext()}", codeGenerator.generate("\"ref<caret>.value.sub1\""))
            assertEquals(emptyList<IntentionAction>(), myFixture.filterAvailableIntentions(hint).toList())
        }
    }
}