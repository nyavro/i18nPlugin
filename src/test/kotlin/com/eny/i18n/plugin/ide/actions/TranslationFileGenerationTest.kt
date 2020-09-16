package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import org.junit.jupiter.api.Test

abstract class TranslationFileGenerationBase(private val ext: String): ExtractionTestBase() {

    override fun getTestDataPath(): String = "src/test/resources/keyExtraction/translationFileGeneration"

    private val testConfig = Config(yamlContentGenerationEnabled = ext == "yml", jsonContentGenerationEnabled = ext == "json")

    @Test
    fun testTranslationFileGeneration() = myFixture.runWithConfig(testConfig) {
        doRunUnknownNs(
            "js/simple.js",
            "js/unknownNsExtracted.js",
            "main.$ext",
            "assets/mainExpected.$ext",
            "main:component.header.title"
        )
    }

    @Test
    fun testTranslationFileGenerationVue() = myFixture.runVueConfig(testConfig) {
        val locales = myFixture.tempDirFixture.findOrCreateDir("locales")
        doRunUnknownNs(
            "vue/simpleVue.vue",
            "vue/unknownNsExtractedVue.vue",
            "locales/en.$ext",
            "locales/enExpected.$ext",
            "component.header.title"
        )
    }
}

class JsonTranslationGenerationTest: TranslationFileGenerationBase("json")