package com.eny.i18n.plugin.factory

/**
 * Language components factory
 */
interface LanguageFactory {
    /**
     * Get translation extractor object
     */
    fun translationExtractor(): TranslationExtractor
    /**
     * Get folding provider
     */
    fun foldingProvider(): FoldingProvider
    /**
     * Get pattern to extract i18n keys from code
     */
    fun callContext(): CallContext
    /**
     * Assistant for code to translation navigation
     */
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