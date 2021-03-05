package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator

class VueHighlightingTest : PlatformBaseTest() {

    private val cg = VueCodeGenerator()
    private val tg = JsonTranslationGenerator()

    private val testConfig = Config(vueDirectory = "assets", defaultNs = "translation")

    private fun check(fileName: String, code: String, translationName: String, translation: String) {
        myFixture.runVueConfig(testConfig) {
            myFixture.addFileToProject(translationName, translation)
            myFixture.configureByText(fileName, code)
            myFixture.checkHighlighting(true, true, true, true)
        }
    }

    fun testReferenceToObjectVue() {
        check(
            "refToObject.${cg.ext()}",
            cg.generate("\"<warning descr=\"Reference to object\">tst2.plurals</warning>\""),
            "assets/en-US.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    fun testDefaultNsUnresolvedVue() {
        check(
            "defNsUnresolved.${cg.ext()}",
            cg.multiGenerate(
                "\"<warning descr=\"Unresolved key\">missing.default.translation</warning>\"",
                "`<warning descr=\"Unresolved key\">missing.default.in.\${template}</warning>`"
            ),
            "assets/none.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    fun testNotArg() {
        check(
            "defNsUnresolved.${cg.ext()}",
            cg.generateInvalid(
                "\"test:tst1.base5.single\""
            ),
            "assets/en-US.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    fun testExpressionInsideTranslation() {
        check(
            "expressionInTranslation.${cg.ext()}",
            cg.generate("isSelected ? \"<warning descr=\"Reference to object\">tst2.plurals</warning>\" : \"<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\""),
            "assets/en-US.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    fun testFirstComponentNs() = myFixture.runVueConfig(testConfig.copy(firstComponentNs = true)) {
        myFixture.addFileToProject("assets/en-US/tst2.${tg.ext()}", tg.generatePlural("plurals", "sub","value", "value1", "value2", "value5"))
        myFixture.configureByText("expressionInTranslation.${cg.ext()}", cg.generate("isSelected ? \"tst2.<warning descr=\"Reference to object\">plurals</warning>\" : \"<warning descr=\"Unresolved namespace\">unresolved</warning>.whole.key\""))
        myFixture.checkHighlighting(true, true, true, true)
    }
}
