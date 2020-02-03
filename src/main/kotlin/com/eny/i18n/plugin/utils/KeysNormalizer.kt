package com.eny.i18n.plugin.utils

/**
 * Normalizes expressions with special characters
 */
class KeysNormalizer {
    private val dropItems = listOf("`", "{", "}", "$")

    /**
     * Runs normalization
     */
    fun normalize(elements: List<KeyElement>): List<KeyElement> {
        return elements.mapNotNull {
            item -> when {
                dropItems.contains(item.text) -> null
                item.type == KeyElementType.TEMPLATE -> item.copy(text="\${${item.text}}")
                else -> item
            }
        }
    }
}