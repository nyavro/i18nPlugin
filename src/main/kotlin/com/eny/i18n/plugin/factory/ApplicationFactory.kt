package com.eny.i18n.plugin.factory

/**
 * Language components factory
 */
interface LanguageFactory {
    fun translationExtractor(): TranslationExtractor
    fun foldingProvider(): FoldingProvider
    fun callContext(): CallContext
    fun referenceAssistant(): ReferenceAssistant
}

/**
 * Localization components factory
 */
interface LocalizationFactory {
    fun contentGenerator(): ContentGenerator
}

/**
 * Plugin's components factory
 */
class MainFactory(private val languageFactories: List<LanguageFactory>, private val localizationFactories: List<LocalizationFactory>) {

    fun translationExtractors(): List<TranslationExtractor> =
        languageFactories.map {it.translationExtractor()}

    fun contentGenerators(): List<ContentGenerator> =
        localizationFactories.map {it.contentGenerator()}
}