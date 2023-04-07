package com.eny.i18n

import com.eny.i18n.plugin.factory.FoldingProvider
import com.eny.i18n.plugin.factory.TranslationExtractor
import com.eny.i18n.plugin.parser.RawKey
import com.intellij.psi.PsiElement

interface Lang {

    fun canExtractKey(element: PsiElement, translationFunctions: List<TranslationFunction>): Boolean

    fun extractRawKey(element: PsiElement): RawKey?

    /**
     * Get folding provider
     */
    fun foldingProvider(): FoldingProvider

    /**
     * Get translation extractor object
     */
    fun translationExtractor(): TranslationExtractor

    fun resolveLiteral(entry: PsiElement): PsiElement?
}
