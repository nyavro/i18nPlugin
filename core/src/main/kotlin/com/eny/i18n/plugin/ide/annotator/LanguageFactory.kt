package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.ide.annotator.CallContext
import com.eny.i18n.plugin.ide.annotator.FoldingProvider
import com.eny.i18n.plugin.ide.annotator.ReferenceAssistant
import com.eny.i18n.plugin.ide.annotator.TranslationExtractor

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