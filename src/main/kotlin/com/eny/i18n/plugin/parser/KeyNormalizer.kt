package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.KeyElementType
import com.eny.i18n.plugin.utils.foldWhileAccum
import com.intellij.codeInsight.completion.CompletionInitializationContext
import kotlin.random.Random

/**
 * Key normalization utils interface
 */
interface KeyNormalizer {

    /**
     * Normalizes single key element
     */
    fun normalize(element: KeyElement): KeyElement? = element
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




