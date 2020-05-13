package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.settings.Settings

abstract class TranslationFileGenerationBase(private val ext: String): ExtractionTestBase() {

    fun testTranslationFileGeneration() {
        val settings = Settings.getInstance(myFixture.project)
        settings.yamlContentGenerationEnabled = ext == "yml"
        settings.jsonContentGenerationEnabled = ext == "json"
        doRunUnknownNs(
            "js/simple.js",
            "js/unknownNsExtracted.js",
            "main.$ext",
            "assets/mainExpected.$ext",
            "main:component.header.title"
        )
    }

    fun testTranslationFileGenerationVue() {
        val settings = Settings.getInstance(myFixture.project)
        settings.yamlContentGenerationEnabled = ext == "yml"
        settings.jsonContentGenerationEnabled = ext == "json"
        settings.vue = true
        doRunUnknownNs(
            "vue/simpleVue.vue",
            "vue/unknownNsExtractedVue.vue",
            "locales/en.$ext",
            "locales/enExpected.$ext",
            "component.header.title",
            "locales/dummy.txt"
        )
        settings.vue = false
    }
}

class JsonTranslationGenerationTest: TranslationFileGenerationBase("json")