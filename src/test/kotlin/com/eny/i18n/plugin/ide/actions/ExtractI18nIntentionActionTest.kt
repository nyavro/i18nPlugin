package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import kotlin.concurrent.thread

class ExtractI18nIntentionActionTest: ExtractionTestBase() {

    @ParameterizedTest
    @ArgumentsSource(JsonYamlCodeGenerators::class)
    fun testKeyExtraction(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext())) {
        runTestCase(
            "simple.${cg.ext()}",
            cg.generateNotExtracted("<caret>I want to move it to translation"),
            cg.generate("'test:ref.value3'"),
            "assets/test.${tg.ext()}",
            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
            tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
            predefinedTextInputDialog("test:ref.value3")
        )
    }

    @ParameterizedTest
    @ArgumentsSource(JsonYamlCodeGenerators::class)
    fun testDefNsKeyExtraction(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext())) {
        runTestCase(
            "simple.${cg.ext()}",
                cg.generateNotExtracted("<caret>I want to move it to translation"),
            cg.generate("'ref.value3'"),
            "assets/translation.${tg.ext()}",
            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
            tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
            predefinedTextInputDialog("ref.value3")
        )
    }

    @ParameterizedTest
    @ArgumentsSource(JsonYamlCodeGenerators::class)
    fun testRightBorderKeyExtraction(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext())) {
        runTestCase(
            "simple.${cg.ext()}",
            cg.generateNotExtracted("I want to move it to translation<caret>"),
            cg.generate("'test:ref.value3'"),
            "assets/test.${tg.ext()}",
            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
            tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
            predefinedTextInputDialog("test:ref.value3")
        )
    }

    @ParameterizedTest
    @ArgumentsSource(JsonYamlCodeGenerators::class)
    fun testRootSource(cg: CodeGenerator, tg: TranslationGenerator) {
        thread {
            myFixture.runWithConfig(config(tg.ext())) {
                runTestCase(
                    "simple.${cg.ext()}",
                    "I want to <caret>move it to translation",
                    "i18n.t<caret>('test:ref.value3')",
                    "assets/test.${tg.ext()}",
                    tg.generate("ref", arrayOf("section", "key", "Reference in json")),
                    tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
                    predefinedTextInputDialog("test:ref.value3")
                )
            }
        }
    }
}

class ExtractI18nIntentionActionVueTest: ExtractionTestBase() {

    private val cg = VueCodeGenerator()

    @ParameterizedTest
    @ArgumentsSource(JsonYamlTranslationGenerators::class)
    fun testKeyExtraction(tg: TranslationGenerator) {
        thread {
            myFixture.runVueConfig(config(tg.ext())) {
                runTestCase(
                    "simple.${cg.ext()}",
                    cg.generateNotExtracted("<caret>I want to move it to translation"),
                    cg.generate("'ref.value3'"),
                    "locales/en-US.${tg.ext()}",
                    tg.generate("ref", arrayOf("section", "key", "Reference in json")),
                    tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
                    predefinedTextInputDialog("ref.value3")
                )
            }
        }
    }
}

abstract class ExtractI18nIntentionActionVueI18nBase(private val translationFormat: String): ExtractionTestBase() {

    private val testConfig = Config(jsonContentGenerationEnabled = translationFormat == "json", yamlContentGenerationEnabled = translationFormat == "yml")

    fun testKeyExtraction2() = myFixture.runVueConfig(testConfig) {
        doRun(
            "vue/App.vue",
            "vue/AppExtracted.vue",
            "locales/en-US.$translationFormat",
            "locales/en-USKeyExtracted.$translationFormat",
            "ref.value3"
        )
    }

    fun testKeyExtractionBorder() = myFixture.runVueConfig(testConfig) {
        doRun(
            "vue/AppBorderVue.vue",
            "vue/AppExtracted.vue",
            "locales/en-US.$translationFormat",
            "locales/en-USKeyExtracted.$translationFormat",
            "ref.value3"
        )
    }

    fun testKeyExtractionLeftBorder() = myFixture.runVueConfig(testConfig) {
        doRun(
            "vue/AppLeftBorderVue.vue",
            "vue/AppExtracted.vue",
            "locales/en-US.$translationFormat",
            "locales/en-USKeyExtracted.$translationFormat",
            "ref.value3"
        )
    }

    fun testScriptKeyExtraction() = myFixture.runVueConfig(testConfig) {
        doRun(
            "vue/scriptVue.vue",
            "vue/scriptKeyExtractedVue.vue",
            "locales/en-US.$translationFormat",
            "locales/en-USKeyExtracted.$translationFormat",
            "ref.value3"
        )
    }
}

class ExtractI18nIntentionActionVueI18nJsonTest: ExtractI18nIntentionActionVueI18nBase("json")
class ExtractI18nIntentionActionVueI18nYamlTest: ExtractI18nIntentionActionVueI18nBase("yml")