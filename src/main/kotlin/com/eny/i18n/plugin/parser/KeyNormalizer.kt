package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.KeyElementType
import com.intellij.codeInsight.completion.CompletionInitializationContext

/**
 * Key normalization utils interface
 */
interface KeyNormalizer {
    /**
     * Normalizes list of Key elements
     */
    fun normalize(elements: List<KeyElement>): List<KeyElement> = elements.mapNotNull {item -> normalize(item)}

    /**
     * Normalizes single key element
     */
    fun normalize(element: KeyElement?): KeyElement? = element
}

/**
 * Combined normalizer - aggregates other normalizers
 */
class KeyNormalizerImpl: KeyNormalizer {
    private val normalizers = listOf(ExpressionNormalizer(), DummyTextNormalizer())
    override fun normalize(element: KeyElement?) = normalizers.fold(element, {
        item, normalizer -> item?.let {value -> normalizer.normalize(value)}
    })
}

private class ExpressionNormalizer: KeyNormalizer {
    private val dropItems = listOf("`", "{", "}", "$")
    override fun normalize(element: KeyElement?): KeyElement? = when {
        dropItems.contains(element?.text) -> null
        element?.type == KeyElementType.TEMPLATE -> element.copy(text="\${${element.text}}")
        else -> element
    }
}

private class DummyTextNormalizer: KeyNormalizer {
    override fun normalize(element: KeyElement?): KeyElement? =
        element?.let {
            value ->
                val fixed = value.text.replace(
                    CompletionInitializationContext.DUMMY_IDENTIFIER,
                    CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED
                )
                value.copy(
                    text = fixed,
                    resolvedTo = value.resolvedTo?.let {fixed}
                )
        }
}




