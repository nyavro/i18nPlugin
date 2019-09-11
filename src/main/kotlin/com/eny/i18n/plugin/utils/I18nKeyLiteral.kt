package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.I18nFullKey

enum class KeyElementType {
    LITERAL, TEMPLATE
}

interface Token {
    fun text(): String
    fun resolved(): String? = text()
}

data class FileNameSeparator(val separator: String): Token {
    override fun text() = separator
}

data class KeySeparator(val separator: String): Token {
    override fun text(): String = separator
}

data class Literal(val literal: String): Token {
    override fun text(): String = literal
}

data class TemplateExpression(val expression: String, val resolvedTo: String?): Token {
    override fun text(): String = expression
    override fun resolved(): String? = resolvedTo

}

data class KeyElement(val text: String, val resolvedTo: String?, val type: KeyElementType) {
    companion object {
        fun fromLiteral(literal: String): KeyElement = KeyElement(literal, literal, KeyElementType.LITERAL)
        fun fromTemplateExpression(expression: String): KeyElement = KeyElement(expression, null, KeyElementType.TEMPLATE)
    }
}

data class I18nKeyLiteral(val literal: String, val isTemplate: Boolean) {
    private val fullKey: I18nFullKey? = I18nFullKey.parse(literal)
    fun fullKey(): I18nFullKey? = fullKey
}

/**

Expected
    FullKey(
        fileName=[KeyElement(text=${fileExpr}, resolvedTo=sample, type=TEMPLATE), KeyElement(text=, resolvedTo=, type=LITERAL)],
        compositeKey=[KeyElement(text=ROOT.Key1.Key31, resolvedTo=ROOT.Key1.Key31, type=LITERAL)]
    )
actual
    FullKey(
        fileName=[KeyElement(text=${fileExpr}, resolvedTo=sample, type=TEMPLATE)],
        compositeKey=[KeyElement(text=ROOT, resolvedTo=ROOT, type=LITERAL), KeyElement(text=Key1, resolvedTo=Key1, type=LITERAL), KeyElement(text=Key31, resolvedTo=Key31, type=LITERAL)]
    )

 */