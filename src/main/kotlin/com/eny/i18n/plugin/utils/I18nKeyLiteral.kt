package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.I18nFullKey

enum class KeyElementType {
    LITERAL, TEMPLATE
}

enum class TokenType {
    Literal, Template, KeySeparator, NsSeparator
}

interface Token {
    fun text(): String
    fun resolved(): List<Token> = listOf()
    fun type(): TokenType
    fun merge(token: Token): Token = this
    fun textLength(): Int = text().length
    fun dotCorrection(): Int = 0
}

data class NsSeparator(val separator: String): Token {
    override fun text() = separator
    override fun type() = TokenType.NsSeparator
}

data class KeySeparator(val separator: String): Token {
    override fun text(): String = separator
    override fun type() = TokenType.KeySeparator
}

data class Literal(private val literal: String, val len: Int, val dotCorrection: Int): Token {
    override fun text(): String = literal
    override fun type() = TokenType.Literal
    override fun merge(token: Token): Token {
        return Literal(literal + token.text(), len + token.textLength(), 0)
    }
    override fun textLength() = len
    override fun dotCorrection() = dotCorrection
}

data class TemplateExpression(private val expression: String, private val resolvedTo: List<Token>): Token {
    val resolved = fld(resolvedTo)
    override fun text(): String  {
        return expression
    }
    override fun resolved(): List<Token> = resolved
    override fun type() = TokenType.Template
    fun fld(rslvd: List<Token>): List<Token> {
        val endsWithSeparator = rslvd.last() is KeySeparator
        val fold = rslvd.foldIndexed(Pair(false, listOf<Token>())) { index, (isFirstLiteralSet, list), token ->
            val dotCorrection = if (!isFirstLiteralSet && token is Literal && (index > 0) || (index == rslvd.lastIndex-1) && endsWithSeparator) 1
            else 0
            val childToken = Literal(token.text(),
                    if (!isFirstLiteralSet && (token is Literal)) {
                        expression.length
                    } else 0,
                    dotCorrection
            )
            val tok = if (token.type() == TokenType.KeySeparator || token.type() == TokenType.NsSeparator) {
                token
            } else {
                childToken
            }
            Pair (
                isFirstLiteralSet || token is Literal,
                list + tok
            )
        }
        return fold.second
    }
}

data class KeyElement(val text: String, val resolvedTo: String?, val type: KeyElementType) {
    companion object {
        fun literal(value: String): KeyElement = KeyElement(value, value, KeyElementType.LITERAL)
        fun unresolvedTemplate(expression: String): KeyElement = KeyElement(expression, null, KeyElementType.TEMPLATE)
        fun resolvedTemplate(expression: String, resolvedTo: String): KeyElement = KeyElement(expression, resolvedTo, KeyElementType.TEMPLATE)
    }
}

data class I18nKeyLiteral(val literal: String, val isTemplate: Boolean) {
    private val fullKey: I18nFullKey? = I18nFullKey.parse(literal)
    fun fullKey(): I18nFullKey? = fullKey
}
