package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.VueSettings
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import org.junit.Test

class LocalizationSourceSearchTest: PlatformBaseTest() {

    override fun getTestDataPath(): String {
        return "src/test/resources/utils"
    }

    @Test
    fun testFailWhenFolderInsideTranslations() {
        val cg = VueCodeGenerator()
        val tg = JsonTranslationGenerator()
        myFixture.runVueConfig(Pair(VueSettings::vueDirectory, "assets"), Pair(VueSettings::vue, true)) {
            myFixture.copyDirectoryToProject("test", "assets/test")
            myFixture.addFileToProject("assets/en-US.${tg.ext()}", tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5"))
            myFixture.configureByText("refToObject.${cg.ext()}", cg.generate("\"<warning descr=\"Reference to object\">tst2.plurals</warning>\""))
            myFixture.checkHighlighting(true, true, true, true)
        }
    }
}