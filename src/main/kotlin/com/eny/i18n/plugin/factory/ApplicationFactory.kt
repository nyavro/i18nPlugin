package com.eny.i18n.plugin.factory

import com.eny.i18n.plugin.extension.Extensions

/**
 * Plugin's components factory
 */
class MainFactory(private val languageFactories: List<LanguageFactory>) {

    /**
     * Get available translation extractors
     */
    fun translationExtractors(): List<TranslationExtractor> =
        languageFactories.map {it.translationExtractor()}

    /**
     * Get available content generators
     */
    fun contentGenerators(): List<ContentGenerator> = Extensions.CONTENT_GENERATORS.getExtensionList()

    /**
     * Pick content generator by file type
     */
    fun contentGenerator(type: LocalizationType): ContentGenerator? =
        contentGenerators().find {it.getType() == type}

    /**
     * Get available localization providers
     */
    fun localizationSourcesProviders(): List<LocalizationSourcesProvider> = Extensions.LOCALIZATION_SOURCE_PROVIDERS.getExtensionList()
}