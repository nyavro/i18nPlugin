package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.utils.generator.code.*
import com.eny.i18n.plugin.utils.generator.translation.Json5TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

private val tgs = listOf(JsonTranslationGenerator(), YamlTranslationGenerator(), Json5TranslationGenerator())
private val cgs = listOf(JsCodeGenerator(), TsCodeGenerator(), JsxCodeGenerator(), TsxCodeGenerator(), PhpCodeGenerator())

fun translationGenerator(ext: String): TranslationGenerator? = tgs.find {it.ext() == ext}

fun codeGenerator(ext: String): CodeGenerator? = cgs.find {it.ext() == ext}

class CodeGeneratorsWithNs : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        (cgs
            .flatMap {
                listOf(Arguments.of(it, true), Arguments.of(it, false))
            } + Arguments.of(VueCodeGenerator(), false)
        ).stream()
}

class CodeGenerators : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        cgs.map {Arguments.of(it)}.stream()
}

class JsonYamlCodeGenerators : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        listOf(JsonTranslationGenerator(), YamlTranslationGenerator()).flatMap {
            tg -> cgs.map {Arguments.of(it, tg)}
        }.stream()
}

class CodeAndTranslationGenerators : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        cgs.flatMap {cg -> tgs.map {Arguments.of(cg, it)}}.stream()
}

class TranslationGenerators: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
        tgs.map {Arguments.of(it)}.stream()
}