package com.eny.i18n.plugin.ide

import com.intellij.openapi.project.Project


/**
 * Plugin's components factory
 */
class MainFactory(private val languageFactories: List<LanguageFactory>, private val project: Project) {

    /**
     * Get available translation extractors
     */
    fun translationExtractors(): List<TranslationExtractor> =
        languageFactories.map {it.translationExtractor()}

    /**
     * Get available content generators
     */
    fun localizationFactories(): List<LocalizationFactory> = Extensions.LOCALIZATION_FACTORIES.getExtensionList()

    /**
     * Get available content generators
     */
    fun contentGenerators(): List<ContentGenerator> = localizationFactories().map {it.contentGenerator()}

    /**
     * Pick content generator by file type
     */
    fun contentGenerator(type: LocalizationType): ContentGenerator? =
        contentGenerators().find {it.getType() == type}

    /**
     * Get available localization providers
     */
    fun localizationSourcesProviders(): List<LocalizationSourcesProvider> = Extensions.LOCALIZATION_SOURCE_PROVIDERS.getExtensionList(project)
}