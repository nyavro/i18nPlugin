package com.eny.i18n.plugin.key

/**
 * Key element types
 *
 * LITERAL - string literal
 * TEMPLATE - contains string interpolation
 */
enum class KeyElementType {
    LITERAL, TEMPLATE
}

/**
 * Represents element of translation key
 */
data class KeyElement(val text: String, val type: KeyElementType) {
    companion object {
        /**
         * Creates Literal key element
         */
        fun literal(value: String): KeyElement = KeyElement(value, KeyElementType.LITERAL)

        /**
         * Creates unresolved template key element
         */
        fun template(expression: String): KeyElement = KeyElement(expression, KeyElementType.TEMPLATE)
    }
}