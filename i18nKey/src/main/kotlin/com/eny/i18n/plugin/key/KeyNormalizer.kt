package com.eny.i18n.plugin.key

/**
 * Key normalization utils interface
 */
interface KeyNormalizer {

    /**
     * Normalizes single key element
     */
    fun normalize(element: KeyElement): KeyElement? = element
}




