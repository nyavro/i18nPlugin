package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.I18nFullKey

enum class KeyElementType {
    LITERAL, TEMPLATE
}

enum class TokenType {
    Literal, Template, KeySeparator, NsSeparator, Asterisk
}

interface Token {
    fun text(): String
    fun resolved(): List<Token> = listOf()
    fun getParent(): Token? = null
    fun type(): TokenType
    fun merge(token: Token): Token = this
    fun textLength(): Int {
        val parent = getParent()
        if (parent != null) {
            return parent.text().length
        }
        return text().length
    }
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

data class Literal(private val literal: String): Token {
    override fun text(): String = literal
    override fun type() = TokenType.Literal
    override fun merge(token: Token): Token {
        if (token is Literal) {
            return Literal(literal + token.literal)
        } else if (token is ChildToken) {
            return token.copy(value = this.merge(token.value), len = textLength() + token.len)// token.copy(value = this.merge(token.value))
        } else {
            TODO()
        }
    }
}

data class ChildToken(val value: Token, private val parent: TemplateExpression, val len: Int, val dotCorrection: Int = 0): Token {
    override fun text(): String = value.text()
    override fun resolved(): List<Token> = value.resolved()
    override fun getParent(): Token? = parent
    override fun type() = value.type()
    override fun toString(): String = "ChildToken <${text()}>"
    override fun merge(token: Token): Token = ChildToken(value.merge(token), parent, len + token.textLength(), if (token is Literal) 1 else 0)
    override fun textLength(): Int {
        return len
    }
    override fun dotCorrection() = dotCorrection
}

data class TemplateExpression(private val expression: String, private val resolvedTo: List<Token>): Token {
    val resolved = fld()
    override fun text(): String = expression
    override fun resolved(): List<Token> = resolved
    override fun type() = TokenType.Template
    fun fld(): List<Token> {
        val endsWithSeparator = resolvedTo.last() is KeySeparator
        val fold = resolvedTo.foldIndexed(Pair(false, listOf<Token>())) { index, (isFirstLiteralSet, list), token ->
            val dotCorrection = if (!isFirstLiteralSet && token is Literal && (index > 0) || (index == resolvedTo.lastIndex-1) && endsWithSeparator) 1
            else 0
            Pair (
                isFirstLiteralSet || token is Literal,
                list + ChildToken(token, this,
                    if (!isFirstLiteralSet && token is Literal) {
                        expression.length
                    } else 0,
                    dotCorrection
                )
            )
        }
        return fold.second
    }
}

object Asterisk: Token {
    override fun text(): String = "*"
    override fun type(): TokenType = TokenType.Asterisk
    override fun merge(token: Token): Token {
        if (token.type() == TokenType.Literal)
            return Literal(text() + token.text())
        else return this
    }
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