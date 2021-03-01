package com.eny.i18n.plugin.ide

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