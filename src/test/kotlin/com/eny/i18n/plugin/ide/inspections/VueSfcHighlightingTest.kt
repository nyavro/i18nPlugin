package com.eny.i18n.plugin.ide.inspections

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueSfcCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import org.junit.jupiter.api.Test

class VueSfcHighlightingTest : PlatformBaseTest() {

    private val codeGenerator = VueCodeGenerator()

    private val tg = JsonTranslationGenerator()

    private val testConfig = Config(vueDirectory = "assets", defaultNs = "translation")

    private val cg = VueSfcCodeGenerator(
        tg.generateNamedBlocks(
            Pair("en", tg.generate("tst3", arrayOf("sfc", "key", "value"))),
            Pair("ja", tg.generate("tst3", arrayOf("sfc", "key", "こんにちは、世界！")))
        )
    )

    @Test
    fun sfcReferenceResolved() =
        myFixture.runVueConfig(testConfig) {
            myFixture.configureByText(
                "sfc.${cg.ext()}",
                cg.generateBlock("message: {{ \$t(\"<info descr=\"null\">tst3.sfc.key</info>\") }}")
            )
            myFixture.checkHighlighting(true, true, true, true)
        }

    @Test
    fun sfcReferenceToObject() =
        myFixture.runVueConfig(testConfig) {
            myFixture.configureByText(
                "sfc.${cg.ext()}",
                cg.generateBlock("message: {{ \$t(\"<warning descr=\"Reference to object\">tst3.sfc</warning>\") }}")
            )
            myFixture.checkHighlighting(true, true, true, true)
        }

    @Test
    fun sfcUnresolvedKey() =
        myFixture.runVueConfig(testConfig) {
            myFixture.configureByText(
                "sfc.${cg.ext()}",
                cg.generateBlock("message: {{ \$t(\"tst3.sfc.<warning descr=\"Unresolved key\">km</warning>\") }}")
            )
            myFixture.checkHighlighting(true, true, true, true)
        }

    @Test
    fun sfcExpressionInsideTranslation() {
        val cg = VueSfcCodeGenerator(
            tg.generateNamedBlocks(
                Pair("en", tg.generatePlural("tst2", "plurals", "value", "value1", "value2", "value5")),
                Pair("ja", tg.generate("tst3", arrayOf("sfc", "key", "こんにちは、世界！")))
            )
        )
        myFixture.runVueConfig(testConfig) {
            myFixture.configureByText(
                "sfc.${cg.ext()}",
                cg.generateBlock(
                "\"message: {{ \$t(isSelected ? \"<warning descr=\"Reference to object\">tst2.plurals</warning>\" : isAbsent ? \"<warning descr=\"Unresolved key\">unresolved.whole.key</warning>\" : \"<info descr=\"null\">tst3.sfc.key</info>\") }}"
                )
            )
            myFixture.checkHighlighting(true, true, true, true)
        }
    }
}
