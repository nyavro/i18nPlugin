package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import org.junit.Test

class LocalizationSourceSearchTest: PlatformBaseTest() {

    override fun getTestDataPath(): String {
        return "src/test/resources/utils"
    }

    private val testConfig = Config(vueDirectory = "assets", defaultNs = "translation")

    @Test
    fun testFailWhenFolderInsideTranslations() {
        val cg = VueCodeGenerator()
        val tg = JsonTranslationGenerator()
        myFixture.runVueConfig(testConfig) {
            myFixture.copyDirectoryToProject("test", "assets/test")
            myFixture.addFileToProject("assets/en-US.${tg.ext()}", tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5"))
            myFixture.configureByText("refToObject.${cg.ext()}", cg.generate("\"<warning descr=\"Reference to object\">tst2.plurals</warning>\""))
            myFixture.checkHighlighting(true, true, true, true)
        }
    }

    @Test
    fun testNonTranslationFilesInTranslationFolder() {
        val cg = VueCodeGenerator()
        val tg = JsonTranslationGenerator()
        myFixture.runVueConfig(testConfig) {
            myFixture.addFileToProject("assets/en-US.${tg.ext()}", tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5"))
            myFixture.addFileToProject("assets/file.txt", "not a translation file")
            myFixture.configureByText("refToObject.${cg.ext()}", cg.generate("\"<warning descr=\"Reference to object\">tst2.plurals</warning>\""))
            myFixture.checkHighlighting(true, true, true, true)
        }
    }
}