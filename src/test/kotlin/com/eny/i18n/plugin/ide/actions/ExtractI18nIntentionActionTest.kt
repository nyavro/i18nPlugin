package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.*
import org.junit.jupiter.api.Test
import kotlin.concurrent.thread

abstract class ExtractI18nIntentionActionBase(private val language: String, private val translationFormat: String, private val codeGenerator: CodeGenerator): ExtractionTestBase() {

    protected val testConfig = Config(jsonContentGenerationEnabled = translationFormat == "json", yamlContentGenerationEnabled = translationFormat == "yml")

    @Test
    fun testKeyExtraction() = myFixture.runWithConfig(testConfig) {
        doRun(
            "$language/simple.$language",
            "$language/simpleKeyExtracted.$language",
            "assets/test.$translationFormat",
            "assets/testKeyExtracted.$translationFormat",
            "test:ref.value3")
    }

    @Test
    fun testRightBorderKeyExtraction() = myFixture.runWithConfig(testConfig) {
        doRun(
            "$language/rightBorder.$language",
            "$language/simpleKeyExtracted.$language",
            "assets/test.$translationFormat",
            "assets/testKeyExtracted.$translationFormat",
            "test:ref.value3")
    }

    @Test
    fun testDefNsKeyExtraction() = myFixture.runWithConfig(testConfig) {
        doRun(
            "$language/simple.$language",
            "$language/simpleDefNsKeyExtracted.$language",
            "assets/translation.$translationFormat",
            "assets/translationKeyExtracted.$translationFormat",
            "ref.value.sub1")
    }

    @Test
    fun testRootSource() {
        thread {
            doRun("jsx/strange.jsx",
                    "jsx/strangeKeyExtracted.jsx",
                    "assets/test.json",
                    "assets/testKeyExtracted.json",
                    "test:ref.value3"
            )
        }
    }
}

class ExtractI18nIntentionActionJsJsonTest: ExtractI18nIntentionActionBase("js","json", JsCodeGenerator())
class ExtractI18nIntentionActionTsJsonTest: ExtractI18nIntentionActionBase("ts", "json", TsCodeGenerator())
class ExtractI18nIntentionActionTsxJsonTest: ExtractI18nIntentionActionBase("tsx", "json", TsxCodeGenerator())
class ExtractI18nIntentionActionJsxJsonTest: ExtractI18nIntentionActionBase("jsx", "json", JsxCodeGenerator())
class ExtractI18nIntentionActionJsYamlTest: ExtractI18nIntentionActionBase("js","yml", JsCodeGenerator())
class ExtractI18nIntentionActionTsYamlTest: ExtractI18nIntentionActionBase("ts", "yml", TsCodeGenerator())
class ExtractI18nIntentionActionTsxYamlTest: ExtractI18nIntentionActionBase("tsx", "yml", TsxCodeGenerator())
class ExtractI18nIntentionActionJsxYamlTest: ExtractI18nIntentionActionBase("jsx", "yml", JsxCodeGenerator())

abstract class ExtractI18nIntentionActionPhpBase(private val translationFormat: String): ExtractI18nIntentionActionBase("php", translationFormat, PhpCodeGenerator()) {

    fun testKeyExtractionSingleQuoted() = myFixture.runWithConfig(testConfig) {
        doRun(
            "php/simpleSingleQuoted.php",
            "php/simpleKeyExtracted.php",
            "assets/test.$translationFormat",
            "assets/testKeyExtracted.$translationFormat",
            "test:ref.value3")
    }

    fun testRightBorderSingleQuoted() = myFixture.runWithConfig(testConfig) {
        doRun(
            "php/rightBorderSingleQuoted.php",
            "php/simpleKeyExtracted.php",
            "assets/test.$translationFormat",
            "assets/testKeyExtracted.$translationFormat",
            "test:ref.value3")
    }

    fun testDefNsKeyExtractionSingleQuoted() = myFixture.runWithConfig(testConfig) {
        doRun(
            "php/simpleSingleQuoted.php",
            "php/simpleDefNsKeyExtracted.php",
            "assets/translation.$translationFormat",
            "assets/translationKeyExtracted.$translationFormat",
            "ref.value.sub1")
    }
}
class ExtractI18nIntentionActionPhpJsonTest: ExtractI18nIntentionActionPhpBase("json")
class ExtractI18nIntentionActionPhpYamlTest: ExtractI18nIntentionActionPhpBase("yml")

abstract class ExtractI18nIntentionActionVueI18nBase(private val translationFormat: String): ExtractionTestBase() {

    private val testConfig = Config(jsonContentGenerationEnabled = translationFormat == "json", yamlContentGenerationEnabled = translationFormat == "yml")

    fun testKeyExtraction() = myFixture.runVueConfig(testConfig) {
        doRun(
            "vue/simpleVue.vue",
            "vue/simpleKeyExtractedVue.vue",
            "locales/en-US.$translationFormat",
            "locales/en-USKeyExtracted.$translationFormat",
            "ref.value3"
        )
    }

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