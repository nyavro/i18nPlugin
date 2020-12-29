package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.runVueConfig
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

class ExtractI18nIntentionActionVueTest: ExtractionTestBase() {

    private val cg = VueCodeGenerator()

    class Provider: ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return listOf(
                "<caret>I want to move it to translation",
                "I want to mov<caret>e it to translation",
                "I want to move it to translation<caret>")
                .flatMap {
                    text -> listOf(JsonTranslationGenerator(), YamlTranslationGenerator()).map {Arguments.of(text, it)}
                }.stream()
        }
    }

//    @ParameterizedTest
//    @ArgumentsSource(Provider::class)
//    fun testKeyExtraction(text: String, tg: TranslationGenerator) {
//        myFixture.runVueConfig(config(tg.ext())) {
//            runTestCase(
//                "simple.${cg.ext()}",
//                cg.generateBlock(text),
//                cg.generate("'ref.value3'"),
//                "locales/en-US.${tg.ext()}",
//                tg.generate("ref", arrayOf("section", "key", "Reference in json")),
//                tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
//                predefinedTextInputDialog("ref.value3")
//            )
//        }
//    }

//    @ParameterizedTest
//    @ArgumentsSource(Provider::class)
//    fun testKeyExtractionTemplate(text: String, tg: TranslationGenerator) {
//        myFixture.runVueConfig(config(tg.ext())) {
//            runTestCase(
//                "App.${cg.ext()}",
//                cg.generateTemplate(text),
//                cg.generateTemplate("{{ \$t('ref.value3') }}"),
//                "locales/en-US.${tg.ext()}",
//                tg.generate("ref", arrayOf("section", "key", "Reference in json")),
//                tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
//                predefinedTextInputDialog("ref.value3")
//            )
//        }
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(Provider::class)
//    fun testScriptExtraction(text: String, tg: TranslationGenerator) {
//        myFixture.runVueConfig(config(tg.ext())) {
//            runTestCase(
//                "App.${cg.ext()}",
//                cg.generateScript("\"$text\""),
//                cg.generateScript("this.\$t('ref.value3')"),
//                "locales/en-US.${tg.ext()}",
//                tg.generate("ref", arrayOf("section", "key", "Reference in json")),
//                tg.generate("ref", arrayOf("section", "key", "Reference in json"), arrayOf("value3", "I want to move it to translation")),
//                predefinedTextInputDialog("ref.value3")
//            )
//        }
//    }
}