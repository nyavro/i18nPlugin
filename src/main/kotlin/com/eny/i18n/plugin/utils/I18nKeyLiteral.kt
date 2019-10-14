package com.eny.i18n.plugin.utils

enum class KeyElementType {
    LITERAL, TEMPLATE
}

data class FullKey(val source: String, val ns:Literal?, val compositeKey:List<Literal>, val isTemplate: Boolean = false) {
//    private val nsLength = ns?.length ?: 0
//    private val keyLength = tokensLength(compositeKey)
//    val length = nsLength + keyLength + (if (nsLength > 0) 1 else 0)
//    val hasAsterisk = ns?.text?.contains("*") ?: false || compositeKey.any { literal -> literal.text.contains("*") }
}

data class KeyElement(val text: String, val resolvedTo: String?, val type: KeyElementType) {
    companion object {
        fun literal(value: String): KeyElement = KeyElement(value, value, KeyElementType.LITERAL)
        fun unresolvedTemplate(expression: String): KeyElement = KeyElement(expression, null, KeyElementType.TEMPLATE)
        fun resolvedTemplate(expression: String, resolvedTo: String): KeyElement = KeyElement(expression, resolvedTo, KeyElementType.TEMPLATE)
    }
}