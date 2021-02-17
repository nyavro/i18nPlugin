package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.utils.generator.code.PhpGetTextCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.PoTranslationGenerator

class PhpGettextHighlightingTest : PlatformBaseTest() {

    private val cg = PhpGetTextCodeGenerator("gettext")
    private val tg = PoTranslationGenerator()

    private fun check(fileName: String, code: String, translationName: String, translation: String) {
        myFixture.addFileToProject(translationName, translation)
        myFixture.configureByText(fileName, code)
        myFixture.checkHighlighting(true, true, true, true)
    }

    fun testUnresolved() {
        check(
            "defNsUnresolved.${cg.ext()}",
            cg.multiGenerate(
                "\"<warning descr=\"Unresolved key\">missing.default.translation</warning>\""
            ),
            "en-US/LC_MESSAGES/none.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    fun testNotArg() {
        check(
            "defNsUnresolved.${cg.ext()}",
            cg.generateInvalid(
                "\"tst1.base5.single\""
            ),
            "assets/en-US.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    fun testMissingTranslationFile() {
        check(
            "code.${cg.ext()}",
            cg.generate("\"<warning descr=\"Missing default translation file\">unresolved.whole.key</warning>\""),
            "en-US/INVALID_FOLDER/translation.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }
}
