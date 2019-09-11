package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.I18nFullKey

enum class KeyElementType {
    LITERAL, TEMPLATE, SYNTHETIC
}

data class KeyElement(val text: String, val resolvedTo: String?, val type: KeyElementType) {
    companion object {
        fun fromLiteral(literal: String): KeyElement = KeyElement(literal, literal, KeyElementType.LITERAL)
        fun fromTemplateExpression(expression: String): KeyElement = KeyElement(expression, null, KeyElementType.TEMPLATE)
        fun synthetic(text: String, resolvedTo: String): KeyElement = KeyElement(text, resolvedTo, KeyElementType.SYNTHETIC)
    }
}

data class I18nKeyLiteral(val literal: String, val isTemplate: Boolean) {
    private val fullKey: I18nFullKey? = I18nFullKey.parse(literal)
    fun fullKey(): I18nFullKey? = fullKey
}