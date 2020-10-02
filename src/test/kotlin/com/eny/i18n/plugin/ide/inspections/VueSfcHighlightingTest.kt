package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.TranslationGenerators
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class VueSfcHighlightingTest : PlatformBaseTest() {

    private val codeGenerator = VueCodeGenerator()

    private val tg = JsonTranslationGenerator()

    private val testConfig = Config(vueDirectory = "assets", defaultNs = "translation")

    private fun check(fileName: String, code: String, translationName: String, translation: String) {
        myFixture.runVueConfig(testConfig) {
            myFixture.addFileToProject(translationName, translation)
            myFixture.configureByText(fileName, code)
            myFixture.checkHighlighting(true, true, true, true)
        }
    }

    @Test
    fun sfcReferenceResolved() =
        myFixture.runVueConfig(testConfig) {
            myFixture.configureByText(
                "sfc.${codeGenerator.ext()}",
                codeGenerator.generateSfc(
                    "\"<info descr=\"null\">tst3.sfc.key</info>\"",
                    mapOf(
                        Pair("en", tg.generate("tst3", arrayOf("sfc", "key", "value"))),
                        Pair("ja", tg.generate("tst3", arrayOf("sfc", "key", "こんにちは、世界！")))
                    )
                )
            )
            myFixture.checkHighlighting(true, true, true, true)
        }

    @Test
    fun sfcReferenceToObject() =
        myFixture.runVueConfig(testConfig) {
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

    @Test
    fun sfcExpressionInsideTranslation() {
        myFixture.runVueConfig(testConfig) {
            myFixture.configureByText(
                "sfc.${codeGenerator.ext()}",
                codeGenerator.generateSfc(
                    "isSelected ? \"<warning descr=\"Reference to object\">tst2.plurals</warning>\" : isAbsent ? \"<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\" : \"<info descr=\"null\">tst3.sfc.key</info>\"",
                    mapOf(
                        Pair("en", tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")),
                        Pair("ja", tg.generate("tst3", arrayOf("sfc", "key", "こんにちは、世界！")))
                    )
                )
            )
            myFixture.checkHighlighting(true, true, true, true)
        }
    }
}
