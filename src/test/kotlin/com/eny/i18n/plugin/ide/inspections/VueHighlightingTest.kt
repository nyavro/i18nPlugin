package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.JsonYamlCodeGenerators
import com.eny.i18n.plugin.ide.TranslationGenerators
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import kotlin.concurrent.thread

class VueHighlightingTest : PlatformBaseTest() {

    private val codeGenerator = VueCodeGenerator()

    private val testConfig = Config(vueDirectory = "assets", defaultNs = "translation")

    private fun check(fileName: String, code: String, translationName: String, translation: String) {
        myFixture.runVueConfig(testConfig) {
            myFixture.addFileToProject(translationName, translation)
            myFixture.configureByText(fileName, code)
            myFixture.checkHighlighting(true, true, true, true)
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TranslationGenerators::class)
    fun testReferenceToObjectVue(tg: TranslationGenerator) {
        check(
            "refToObject.${codeGenerator.ext()}",
            codeGenerator.generate("\"<warning descr=\"Reference to object\">tst2.plurals</warning>\""),
            "assets/en-US.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    @ParameterizedTest
    @ArgumentsSource(TranslationGenerators::class)
    fun testDefaultNsUnresolvedVue(tg: TranslationGenerator) {
        check(
            "defNsUnresolved.${codeGenerator.ext()}",
            codeGenerator.multiGenerate(
                    "\"<warning descr=\"Unresolved key\">missing.defau8lt.translation</warning>\"",
                    "`<warning descr=\"Unresolved key\">missing.default.in.\${template}</warning>`"
            ),
            "assets/none.${tg.ext()}",
            tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    @ParameterizedTest
    @ArgumentsSource(TranslationGenerators::class)
    fun testNotArg(tg: TranslationGenerator) {
        check(
                "defNsUnresolved.${codeGenerator.ext()}",
                codeGenerator.generateInvalid(
                        "\"test:tst1.base5.single\""
                ),
                "assets/en-US.${tg.ext()}",
                tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    @ParameterizedTest
    @ArgumentsSource(TranslationGenerators::class)
    fun testExpressionInsideTranslation(tg: TranslationGenerator) {
        check(
                "expressionInTranslation.${codeGenerator.ext()}",
                codeGenerator.generate("isSelected ? \"test:<warning descr=\"Reference to object\">tst2.plurals</warning>\" : \"test:<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\""),
                "assets/en-US.${tg.ext()}",
                tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")
        )
    }

    @Test
    fun sfcReferenceToObject() =
        myFixture.runVueConfig(testConfig) {
            val tg = JsonTranslationGenerator()
            myFixture.configureByText(
                "sfc.${codeGenerator.ext()}",
                codeGenerator.generateSfc(
                    "\"<warning descr=\"Reference to object\">tst3.sfc</warning>\"",
                    mapOf(
                        Pair("en", tg.generate("tst3", arrayOf("sfc", "key", "value"))),
                        Pair("ja", tg.generate("tst3", arrayOf("sfc", "key", "こんにちは、世界！")))
                    )
                )
            )
            myFixture.checkHighlighting(true, true, true, true)
        }

    @Test
    fun sfcUnresolvedKey() =
        myFixture.runVueConfig(testConfig) {
            val tg = JsonTranslationGenerator()
            myFixture.configureByText(
                    "sfc.${codeGenerator.ext()}",
                    codeGenerator.generateSfc(
                            "\"tst3.sfc.<warning descr=\"Unresolved key\">km</warning>\"",
                            mapOf(
                                    Pair("en", tg.generate("tst3", arrayOf("sfc", "key", "value"))),
                                    Pair("ja", tg.generate("tst3", arrayOf("sfc", "key", "こんにちは、世界！")))
                            )
                    )
            )
            myFixture.checkHighlighting(true, true, true, true)
        }
}