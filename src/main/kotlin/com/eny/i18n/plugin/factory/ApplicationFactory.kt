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
     * Assistant for code to translation navigation
     */
    fun referenceAssistant(): ReferenceAssistant
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
}