package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.PlatformBaseTest
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.junit.Test

fun CodeInsightTestFixture.customCheck(fileName: String, code: String, translationName: String, translation: String) {
    this.addFileToProject(translationName, translation)
    this.configureByText(fileName, code)
    this.checkHighlighting(true, true, true, true)
}
//
//class CodeHighlightingTestBase: PlatformBaseTest() {
// @TODO 1

//    @ParameterizedTest
//    @ArgumentsSource(CodeTranslationGenerators::class)
//    fun testReferenceToObject(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customCheck(
//        "refToObject.${cg.ext()}",
//        cg.generate("\"test:<warning descr=\"Reference to object\">tst2.plurals</warning>\""),
//        "test.${tg.ext()}",
//        tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
//    )
//
//    @ParameterizedTest
//    @ArgumentsSource(CodeTranslationGenerators::class)
//    fun testReferenceToObjectDefaultNs(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customCheck(
//        "refToObjectDefNs.${cg.ext()}",
//        cg.generate("\"<warning descr=\"Reference to object\">tst2.plurals</warning>\""),
//        "assets/translation.${tg.ext()}",
//        tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
//    )
//
//    @ParameterizedTest
//    @ArgumentsSource(CodeTranslationGenerators::class)
//    fun testResolved(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customCheck(
//        "resolved.${cg.ext()}",
//        cg.generate("\"test:tst1.base.single\""),
//        "assets/translation.${tg.ext()}",
//        tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
//    )
//
//    @ParameterizedTest
//    @ArgumentsSource(CodeTranslationGenerators::class)
//    fun testNotArg(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customCheck(
//        "defNsUnresolved.${cg.ext()}",
//        cg.generateInvalid(
//            "\"test:tst1.base5.single\""
//        ),
//        "assets/test.${tg.ext()}",
//        tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
//    )
//
//    @ParameterizedTest
//    @ArgumentsSource(CodeTranslationGenerators::class)
//    fun testExpressionInsideTranslation(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customCheck(
//        "expressionInTranslation.${cg.ext()}",
//        cg.generate("isSelected ? \"test:<warning descr=\"Reference to object\">tst2.plurals</warning>\" : \"test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\""),
//        "test.${tg.ext()}",
//        tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
//    )
//
//    @Test
//    fun testPartiallyTranslatedInspection() = myFixture.runCommonConfig(Pair(CommonSettings::partialTranslationInspectionEnabled, true)) {
//        val cg = TsxCodeGenerator()
//        val tg = JsonTranslationGenerator()
//        myFixture.addFileToProject("en/test.json",
//            tg.generateNamedBlock(
//                "root",
//                tg.generateNamedBlock(
//                    "sub",
//                    tg.generateNamedBlock("base", "\"Partially defined translation\""),
//                    1
//                )
//            )
//        )
//        myFixture.addFileToProject("ru/test.json",
//            tg.generateNamedBlock(
//                "root",
//                    tg.generateNamedBlock(
//                    "another", "\"value\""
//                )
//            )
//        )
//        myFixture.configureByText("sample.tsx", cg.generate("'test:root.<warning descr=\"Partially translated key\">sub.base</warning>'"))
//        myFixture.checkHighlighting(true, true, true, true)
//    }
//}
//
class JsDialectCodeHighlightingTestBase: PlatformBaseTest() {
//
//    @ParameterizedTest
//    @ArgumentsSource(JsCodeAndTranslationGeneratorsNs::class)
//    fun testDefNsUnresolved(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customCheck(
//        "defNsUnresolved.${cg.ext()}",
//        cg.multiGenerate(
//            "\"<warning descr=\"Missing default translation file\">missing.default.translation</warning>\"",
//            "`<warning descr=\"Missing default translation file\">missing.default.in.\${template}</warning>`"
//        ),
//        "assets/test.${tg.ext()}",
//        tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
//    )
//
//    @ParameterizedTest
//    @ArgumentsSource(JsCodeAndTranslationGeneratorsNs::class)
//    fun testUnresolvedKey(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customCheck(
//        "unresolvedKey.${cg.ext()}",
//        cg.multiGenerate(
//            "\"test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key</warning>\"",
//            "\"test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\"",
//            "`test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.key.\${arg}</warning>`",
//            "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${arg}</warning>`",
//            "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${arg}</warning>`",
//            "`test:<warning descr=\"Unresolved key\">unresolved.whole.\${b ? 'key' : 'key2'}</warning>`",
//            "`test:tst1.<warning descr=\"Unresolved key\">unresolved.part.of.\${b ? 'key' : 'key2'}</warning>`"
//        ),
//        "test.${tg.ext()}",
//        tg.generateContent("tst1", "base", "single", "only one value")
//    )
//
//    @ParameterizedTest
//    @ArgumentsSource(JsCodeAndTranslationGeneratorsNs::class)
//    fun testUnresolvedNs(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customCheck(
//        "unresolvdNs.${cg.ext()}",
//        cg.multiGenerate(
//            "\"<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base\"",
//            "`<warning descr=\"Unresolved namespace\">unresolved</warning>:tst1.base.\${arg}`"
//        ),
//        "test.${tg.ext()}",
//        tg.generateContent("root", "first", "key", "value")
//    )
//
//    @Test
//listOf(JsCodeGenerator(), TsCodeGenerator(), JsxCodeGenerator(), TsxCodeGenerator())
//    fun testResolvedTemplate(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.customCheck(
//        "resolvedTemplate.${cg.ext()}",
//        cg.generate("`test:tst1.base.\${arg}`"),
//        "assets/translation.${tg.ext()}",
//        tg.generateContent("tst1", "base", "value", "translation")
//    )
}

