package com.eny.i18n.plugin.ide.hint

import com.eny.i18n.plugin.utils.generator.code.JsCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.testFramework.fixtures.BasePlatformTestCase

internal class JsHintTest: BasePlatformTestCase() {

    val cg = JsCodeGenerator();
    val tg = JsonTranslationGenerator();

    fun testSingleHint() {
        cg.generate("")
    }
}
