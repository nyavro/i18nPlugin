package com.eny.i18n.plugin.factory

import com.eny.i18n.plugin.factory.extractor.TranslationExtractor

/**
 * Language components factory
 */
interface LanguageFactory {
    fun translationExtractor(): TranslationExtractor
}

/**
 * Plugin's components factory
 */
class MainFactory(private val languageFactories: List<LanguageFactory>) {

    fun translationExtractors(): List<TranslationExtractor> =
        languageFactories.map {it.translationExtractor()}
}