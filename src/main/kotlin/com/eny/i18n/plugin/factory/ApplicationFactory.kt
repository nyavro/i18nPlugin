package com.eny.i18n.plugin.factory

/**
 * Language components factory
 */
interface LanguageFactory {
    fun translationExtractor(): TranslationExtractor
    fun foldingProvider(): FoldingProvider
    fun callContext(): CallContext
}

/**
 * Plugin's components factory
 */
class MainFactory(private val languageFactories: List<LanguageFactory>) {

    fun translationExtractors(): List<TranslationExtractor> =
        languageFactories.map {it.translationExtractor()}
}