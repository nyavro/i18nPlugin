package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.concurrent.thread

class ExtractI18nIntentionActionTest: ExtractionTestBase() {

    @ParameterizedTest
    @ArgumentsSource(JsonYamlCodeGenerators::class)
    fun testKeyExtraction(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext())) {
        runTestCase(
            "simple.${cg.ext()}",
            cg.generateNotExtracted("<caret>I want to move it to translation"),
            cg.generate("'test:ref.avalue3'"),
            "assets/test.${tg.ext()}",
            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
            tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("avalue3", "I want to move it to translation")),
            predefinedTextInputDialog("test:ref.avalue3")
        )
    }

    @ParameterizedTest
    @ArgumentsSource(JsonYamlCodeGenerators::class)
    fun testKeyExtractionSortedFirst(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext(), true)) {
        runTestCase(
            "simple.${cg.ext()}",
            cg.generateNotExtracted("<caret>I want to move it to translation"),
            cg.generate("'test:ref.dvalue3'"),
            "assets/test.${tg.ext()}",
            tg.generate("ref", arrayOf("section", "key", "Reference in json")),
            tg.generate("ref", arrayOf("dvalue3", "I want to move it to translation"), arrayOf("section", "key", "Reference in json")),
            predefinedTextInputDialog("test:ref.dvalue3")
        )
    }

    @ParameterizedTest
    @ArgumentsSource(JsonYamlCodeGenerators::class)
    fun testKeyExtractionSortedMiddle(cg: CodeGenerator, tg: TranslationGenerator) = myFixture.runWithConfig(config(tg.ext(), true)) {
        runTestCase(
            "simple.${cg.ext()}",
            cg.generateNotExtracted("Mid<caret>dle!!!"),
            cg.generate("'test:ref.mkey'"),
            "assets/test.${tg.ext()}",
            tg.generate("ref", arrayOf("akey", "The first one"), arrayOf("zkey", "The last one")),
            tg.generate("ref", arrayOf("akey", "The first one"), arrayOf("mkey", "Middle!!!"), arrayOf("zkey", "The last one")),
            predefinedTextInputDialog("test:ref.mkey")
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

    class Provider: ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return listOf(
                "<caret>I want to move it to translation",
                "I want to mov<caret>e it to translation",
                "I want to move it to translation<caret>").flatMap {
                text -> listOf(JsonTranslationGenerator(), YamlTranslationGenerator()).map {Arguments.of(text, it)}
            }.stream()
        }
    }

    @ParameterizedTest
    @ArgumentsSource(Provider::class)
    fun testKeyExtraction(text: String, tg: TranslationGenerator) {
        thread {
            myFixture.runVueConfig(config(tg.ext())) {
                runTestCase(
                    "simple.${cg.ext()}",
                    cg.generateNotExtracted(text),
                    cg.generate("'ref.value3'"),
                    "locales/en-US.${tg.ext()}",
                    tg.generate("ref", arrayOf("section", "key", "Reference in json")),
                    tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
                    predefinedTextInputDialog("ref.value3")
                )
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(Provider::class)
    fun testKeyExtractionTemplate(text: String, tg: TranslationGenerator) {
        thread {
            myFixture.runVueConfig(config(tg.ext())) {
                runTestCase(
                    "App.${cg.ext()}",
                    cg.generateTemplate(text),
                    cg.generateTemplate("{{ \$t('ref.value3') }}"),
                    "locales/en-US.${tg.ext()}",
                    tg.generate("ref", arrayOf("section", "key", "Reference in json")),
                    tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
                    predefinedTextInputDialog("ref.value3")
                )
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(Provider::class)
    fun testScriptExtraction(text: String, tg: TranslationGenerator) {
        thread {
            myFixture.runVueConfig(config(tg.ext())) {
                runTestCase(
                    "App.${cg.ext()}",
                    cg.generateScript("\"$text\""),
                    cg.generateScript("this.\$t('ref.value3')"),
                    "locales/en-US.${tg.ext()}",
                    tg.generate("ref", arrayOf("section", "key", "Reference in json")),
                    tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
                    predefinedTextInputDialog("ref.value3")
                )
            }
        }
    }
}