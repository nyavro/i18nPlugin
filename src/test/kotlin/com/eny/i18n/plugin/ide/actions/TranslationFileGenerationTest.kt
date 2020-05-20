package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config

abstract class TranslationFileGenerationBase(private val ext: String): ExtractionTestBase() {

    override fun getTestDataPath(): String = "src/test/resources/keyExtraction/translationFileGeneration"

    private val testConfig = Config(yamlContentGenerationEnabled = ext == "yml", jsonContentGenerationEnabled = ext == "json")

    fun testTranslationFileGeneration() = myFixture.runWithConfig(testConfig) {
        doRunUnknownNs(
            "js/simple.js",
            "js/unknownNsExtracted.js",
            "main.$ext",
            "assets/mainExpected.$ext",
            "main:component.header.title"
        )
    }

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