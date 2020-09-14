package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.utils.generator.code.*
import org.junit.jupiter.api.Test

abstract class KeyContextBase(private val codeGenerator: CodeGenerator): ExtractionTestBase() {

    @Test
    fun testKeyContext() {
        doUnavailable("keyContextDefNs.${codeGenerator.ext()}", codeGenerator.generate("\"test:ref<caret>.value.sub1\""))
    }

    @Test
    fun testKeyContextDefaultNs() {
        doUnavailable("keyContextDefNs.${codeGenerator.ext()}", codeGenerator.generate("\"ref<caret>.value.sub1\""))
    }
}

class KeyContextJsTest: KeyContextBase(JsCodeGenerator())
class KeyContextTsTest: KeyContextBase(TsCodeGenerator())
class KeyContextJsxTest: KeyContextBase(JsxCodeGenerator())
class KeyContextTsxTest: KeyContextBase(TsxCodeGenerator())
class KeyContextPhpTest: KeyContextBase(PhpCodeGenerator())

class KeyContextVueTest: ExtractionTestBase() {

    private val codeGenerator = VueCodeGenerator()

    //@Test: TODO test
    fun testKeyContext() = myFixture.runVue {
        doUnavailable("keyContext.${codeGenerator.ext()}", codeGenerator.generate("\"ref<caret>.value.sub1\""))
    }

    @Test
    fun testKeyContextScript() = myFixture.runVue {
        myFixture.tempDirPath
        doUnavailable("vue/keyContextScript.vue")
    }
}