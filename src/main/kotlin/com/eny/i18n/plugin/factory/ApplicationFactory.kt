package com.eny.i18n.plugin.factory

import com.eny.i18n.ContentGenerator
import com.eny.i18n.Extensions
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiElement

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

    /**
     * Localization format-specific reference assistant
     */
    fun referenceAssistant(): TranslationReferenceAssistant<out PsiElement>
}

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
    fun contentGenerators(): List<ContentGenerator> =
        Extensions.LOCALIZATION.extensionList.map {it.contentGenerator()}

    /**
     * Pick content generator by file type
     */
    fun contentGenerator(type: FileType): ContentGenerator? =
        contentGenerators().find {it.getType() == type}
}