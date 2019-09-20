package com.eny.i18n.plugin.utils

data class FullKey(val ns:Token?, val compositeKey:List<Token>) {
    val nsLength = ns?.textLength() ?: 0
        get() = field
    val keyLength = ln(compositeKey, true, false)
        get() = field
    val length = nsLength + keyLength + (if (nsLength > 0) 1 else 0)
        get() = field
    private fun ln(tokens: List<Token>, correctDots: Boolean, correctNs: Boolean):Int {
        val lengths = tokens.map { n -> n.textLength()}.filter { v -> v > 0}
        val dotCorrection = if (correctDots) tokens.map { n -> n.dotCorrection()}.sum() else 0
        return lengths.sum() + (if (lengths.isEmpty()) 0 else (lengths.size - 1)) - dotCorrection
    }
}

interface State {
    fun next(token: Token): State
    fun fullKey(): FullKey? {
        return null
    }
    fun unwrapTemplateExpression(token: TemplateExpression): State = token.resolved().fold(this) {
        state, item -> state.next(item)
    }
}

class Error(val msg: String): State {
    override fun next(token: Token): State = this
    override fun fullKey(): FullKey? = null
    override fun toString(): String = "Error <$msg>"
}

class Start(val init: Token?) : State {
    private fun merge(token: Token): Token {
        return when {
            init != null -> init.merge(token)
            else -> token
        }
    }
    override fun next(token: Token): State {
        return when {
            token.type() == TokenType.NsSeparator && init != null  -> WaitingLiteral(init, listOf())
            token.type() == TokenType.KeySeparator && init != null -> WaitingLiteral(null, listOf(init))
            token.type() == TokenType.Literal  -> Start(merge(token))
            token.type() == TokenType.Template -> unwrapTemplateExpression(token as TemplateExpression)
            else -> Error("Invalid ns separator position (0)") // Never get here
        }
    }
}

class WaitingLiteral(val file: Token?, val key: List<Token>) : State {
    override fun next(token: Token): State {
        return when {
            token.type() == TokenType.Literal ->  WaitingLiteralOrSeparator(file, key + token)
            token.type() == TokenType.Template -> unwrapTemplateExpression(token as TemplateExpression)
            else -> Error("Invalid token " + token.text())
        }
    }
}

class WaitingLiteralOrSeparator(val file: Token?, val key: List<Token>) : State {
    override fun next(token: Token): State {
        return when {
            token.type() == TokenType.KeySeparator -> WaitingLiteral(file, key)
            token.type() == TokenType.Literal -> {
                val last = key.last().merge(token)
                val init = key.dropLast(1)
                WaitingLiteralOrSeparator(file, init + last)
            }
            token.type() == TokenType.Template -> unwrapTemplateExpression(token as TemplateExpression)
            else -> Error("Invalid token " + token.text())
        }
    }
    override fun fullKey(): FullKey? = FullKey(file, key)
}

class ExpressionKeyParser {
    fun parse(elements: List<KeyElement>): FullKey? {
        val tokens = Tokenizer(":", ".").tokenizeAll(elements)
        return tokens.fold(Start(null) as State) {
            state, token -> state.next(token)
        }.fullKey()
    }
}
