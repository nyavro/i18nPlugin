package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import com.intellij.psi.PsiElement

data class RawKey(val keyElements: List<KeyElement>, val arguments: List<String> = emptyList())

/**
 * Extractor of i18n key from PsiElement
 */
interface KeyExtractor {
    /**
     * Checks if current key extractor is applicable to given psi element
     */
    fun canExtract(element: PsiElement): Boolean

    /**
     * Extracts key from psi element
     */
    fun extract(element: PsiElement): RawKey
}