package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runCommonConfig
import com.eny.i18n.plugin.ide.annotator.CommonSettings
import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.junit.Test

fun CodeInsightTestFixture.customCheck(fileName: String, code: String, translationName: String, translation: String) {
    this.addFileToProject(translationName, translation)
    this.configureByText(fileName, code)
    this.checkHighlighting(true, true, true, true)
}

fun CodeInsightTestFixture.customCheck(fileName: String, code: String) {
    this.configureByText(fileName, code)
    this.checkHighlighting(true, true, true, true)
}

class CodeHighlightingTestBase: PlatformBaseTest() {

    val tg = JsonTranslationGenerator()
    val cgs = listOf(JsCodeGenerator(), TsCodeGenerator(), JsxCodeGenerator(), TsxCodeGenerator(), PhpSingleQuoteCodeGenerator(), PhpDoubleQuoteCodeGenerator())

    @Test
    fun testReferenceToObject() {
        cgs.forEachIndexed { i, cg ->
            myFixture.customCheck(
                "refToObject.${cg.ext()}",
                cg.generate("\"test${i}:<warning descr=\"Reference to object\">tst2.plurals</warning>\""),
                "test${i}.${tg.ext()}",
                tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
            )
        }
    }

    @Test
    fun testReferenceToObjectDefaultNs() {
        myFixture.addFileToProject(
            "assets/translation.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
        cgs.forEachIndexed { i, cg ->
            myFixture.customCheck(
                "refToObjectDefNs.${cg.ext()}",
                cg.generate("\"<warning descr=\"Reference to object\">tst2.plurals</warning>\"")
            )
        }
    }

    @Test
    fun testResolved() {
        myFixture.addFileToProject(
            "assets/translation.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
        cgs.forEach {cg ->
            myFixture.customCheck(
                    "resolved.${cg.ext()}",
                    cg.generate("\"test:tst1.base.single\"")
            )
        }
    }

    @Test
    fun testNotArg() = cgs.forEachIndexed { i, cg ->
        myFixture.customCheck(
            "defNsUnresolved.${cg.ext()}",
            cg.generateInvalid(
                    "\"test${i}:tst1.base5.single\""
            ),
            "assets/test${i}.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    @Test
    fun testExpressionInsideTranslation() = cgs.forEachIndexed { i, cg ->
        myFixture.customCheck(
            "expressionInTranslation.${cg.ext()}",
            cg.generate("isSelected ? \"test${i}:<warning descr=\"Reference to object\">tst2.plurals</warning>\" : \"test${i}:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\""),
            "test${i}.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    @Test
    fun testPartiallyTranslatedInspection() = myFixture.runCommonConfig(Pair(CommonSettings::partialTranslationInspectionEnabled, true)) {
        val cg = TsxCodeGenerator()
        val tg = JsonTranslationGenerator()
        myFixture.addFileToProject("en/test.json",
            tg.generateNamedBlock(
                "root",
                tg.generateNamedBlock(
                    "sub",
                    tg.generateNamedBlock("base", "\"Partially defined translation\""),
                    1
                )
            )
        )
        myFixture.addFileToProject("ru/test.json",
            tg.generateNamedBlock(
                "root",
                    tg.generateNamedBlock(
                    "another", "\"value\""
                )
            )
        )
        myFixture.configureByText("sample.tsx", cg.generate("'test:root.<warning descr=\"Partially translated key\">sub.base</warning>'"))
        myFixture.checkHighlighting(true, true, true, true)
    }
}

