package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

private val tgs = listOf(JsonTranslationGenerator(), YamlTranslationGenerator())
private val cgs = listOf(JsCodeGenerator(), TsCodeGenerator(), JsxCodeGenerator(), TsxCodeGenerator(), PhpSingleQuoteCodeGenerator(), PhpDoubleQuoteCodeGenerator())
private val jsCgs = listOf(JsCodeGenerator(), TsCodeGenerator(), JsxCodeGenerator(), TsxCodeGenerator())

fun translationGenerator(ext: String): TranslationGenerator? = tgs.find {it.ext() == ext}

fun codeGenerator(ext: String): CodeGenerator? = cgs.find {it.ext() == ext}

class CodeGeneratorsWithNs : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        (cgs
            .flatMap {
                listOf(Arguments.of(it, true), Arguments.of(it, false))
            }
        ).stream()
}

class JsonYamlCodeGenerators : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        listOf(
            JsonTranslationGenerator(),
            YamlTranslationGenerator()).flatMap {
            tg -> cgs.map {Arguments.of(it, tg)}
        }.stream()
}

class JsCodeAndTranslationGenerators : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        jsCgs.flatMap { cg -> tgs.map {Arguments.of(cg, it)}}.stream()
}

class JsCodeAndTranslationGeneratorsNs : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        jsCgs.flatMap { cg -> tgs.map {Arguments.of(cg, it)}}.stream()
}

class PhpCodeAndTranslationGenerators : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        listOf(PhpSingleQuoteCodeGenerator(), PhpDoubleQuoteCodeGenerator()).flatMap { cg -> tgs.map {Arguments.of(cg, it)}}.stream()
}

class CodeTranslationGenerators: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        cgs.flatMap {cg -> tgs.map {Arguments.of(cg, it)}}.stream()
}
