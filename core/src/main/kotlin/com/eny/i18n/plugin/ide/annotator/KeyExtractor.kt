package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.key.KeyElement
import com.eny.i18n.plugin.key.KeyElementType
import com.eny.i18n.plugin.key.KeyNormalizer
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.psi.PsiElement

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
    fun extract(element: PsiElement): Pair<List<KeyElement>, List<String>?>
}

/**
 * Normalizers element with template expression
 */
class ExpressionNormalizer: KeyNormalizer {
    private val dropItems = listOf("`", "{", "}", "$")
    override fun normalize(element: KeyElement): KeyElement? = when {
        dropItems.contains(element.text) -> null
        element.type == KeyElementType.TEMPLATE -> element.copy(text="\${${element.text}}")
        else -> element
    }
}

/**
 * Normalizes dummy key, used in key completion
 */
class DummyTextNormalizer: KeyNormalizer {
    override fun normalize(element: KeyElement): KeyElement? {
        return element.copy(
                text = element.text.replace(
                        CompletionInitializationContext.DUMMY_IDENTIFIER,
                        CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED
                )
        )
    }
}