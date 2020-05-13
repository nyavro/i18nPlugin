package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.SettingsPack
import com.eny.i18n.plugin.ide.runVuePack
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

    fun testTranslationFileGenerationVue() = myFixture.runVuePack(
        SettingsPack()
            .with(Settings::yamlContentGenerationEnabled, ext == "yml")
            .with(Settings::jsonContentGenerationEnabled, ext == "json")) {
        myFixture.tempDirFixture.findOrCreateDir("locales")
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