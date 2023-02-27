package com.eny.i18n.extensions.localization.yaml

import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.JsCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture

private fun CodeInsightTestFixture.customCheck(fileName: String, code: String, translationName: String, translation: String) = this.runWithConfig(
    Config(defaultNs = "translation")
) {
    this.addFileToProject(translationName, translation)
    this.configureByText(fileName, code)
    this.checkHighlighting(true, true, true, true)
}

class CodeHighlightingTest2: BasePlatformTestCase() {
    val cg = JsCodeGenerator()
    val tg = YamlTranslationGenerator()

    fun testReferenceToObject() = myFixture.customCheck(
            "refToObject.${cg.ext()}",
            cg.generate("\"test:<warning descr=\"Reference to object\">tst2.plurals</warning>\""),
            "test.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

    fun testExpressionInsideTranslation() = myFixture.customCheck(
            "expressionInTranslation.${cg.ext()}",
            cg.generate("isSelected ? \"test:<warning descr=\"Reference to object\">tst2.plurals</warning>\" : \"test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\""),
            "test.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

    fun testResolved() = myFixture.customCheck(
            "resolved.${cg.ext()}",
            cg.generate("\"test:tst1.base.single\""),
            "assets/translation.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

    fun testReferenceToObjectDefaultNs() = myFixture.customCheck(
            "refToObjectDefNs.${cg.ext()}",
            cg.generate("\"<warning descr=\"Reference to object\">tst2.plurals</warning>\""),
            "assets/translation.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

    fun testNotArg() = myFixture.customCheck(
            "defNsUnresolved.${cg.ext()}",
            cg.generateInvalid(
                    "\"test:tst1.base5.single\""
            ),
            "assets/test.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
    )

}