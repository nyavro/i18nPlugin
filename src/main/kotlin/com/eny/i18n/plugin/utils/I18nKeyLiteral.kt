package com.eny.i18n.plugin.utils

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
 * Represents translation key
 */
data class FullKey(val source: String, val ns:Literal?, val compositeKey:List<Literal>, val isTemplate: Boolean = false)

/**
 * Represents element of translation key
 */
data class KeyElement(val text: String, val resolvedTo: String?, val type: KeyElementType) {
    companion object {
        /**
         * Creates Literal key element
         */
        fun literal(value: String): KeyElement = KeyElement(value, value, KeyElementType.LITERAL)

        /**
         * Creates unresolved template key element
         */
        fun unresolvedTemplate(expression: String): KeyElement = KeyElement(expression, null, KeyElementType.TEMPLATE)

        /**
         * Creates resolved template key element
         */
        fun resolvedTemplate(expression: String, resolvedTo: String): KeyElement = KeyElement(expression, resolvedTo, KeyElementType.TEMPLATE)
    }
}