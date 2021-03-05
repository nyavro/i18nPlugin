package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import org.junit.jupiter.api.Test

class CodeCompletionVueNamespace : PlatformBaseTest() {

    val tg = JsonTranslationGenerator()
    val cg = VueCodeGenerator()

        //TODO
//    fun testEmptyKeyCompletion() = myFixture.runVueConfig(
//        Config(firstComponentNs = true)
//    ) {
//        myFixture.addFileToProject("locales/en-US/first.${tg.ext()}", tg.generateContent("tst1", "base", "single", "only one value"))
//        myFixture.configureByText("empty.${cg.ext()}", cg.generate("\"fi<caret>\""))
//        val vars = myFixture.completeBasic()
//        assertTrue(vars.find {it.lookupString == "first"} != null)
//    }

    @Test
    fun doTestNamespaceCompletion() = myFixture.runVueConfig(
            Config(firstComponentNs = true)
    ) {
        myFixture.addFileToProject("locales/en-US/first.${tg.ext()}", tg.generateContent("root", "base", "single", "only one value"))
        myFixture.configureByText("empty.${cg.ext()}", cg.generate("\"first.root.ba<caret>\""))
        val vars = myFixture.completeBasic()
        assertTrue(vars?.find {it.lookupString == "first.root.base"} != null)
    }
}