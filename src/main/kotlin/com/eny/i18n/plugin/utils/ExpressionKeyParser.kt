package com.eny.i18n.plugin.utils

interface State {
    fun next(token: Token): State
    fun fullKey(isTemplate: Boolean, source: String): FullKey? = null
}

class Error(private val msg: String): State {
    override fun next(token: Token): State = this
    override fun toString(): String = "Error <$msg>"
}

class Start(private val init: Literal?) : State {
    override fun next(token: Token): State =
        when {
            token is NsSeparator && init != null  -> WaitingLiteral(init, listOf())
            token is KeySeparator && init != null -> WaitingLiteral(null, listOf(init))
            token is Literal  -> Start(init?.merge(token) ?: token)
            else -> Error("Invalid ns separator position (0)") // Never get here
        }
}

class WaitingLiteral(private val file: Literal?, val key: List<Literal>) : State {
    override fun next(token: Token): State =
        when (token) {
            is Literal -> WaitingLiteralOrSeparator(file, key + token)
            else -> Error("Invalid token $token")
        }
    override fun fullKey(isTemplate: Boolean, source: String): FullKey? = FullKey(source, file, key + Literal(""), isTemplate)
}

class WaitingLiteralOrSeparator(val file: Literal?, val key: List<Literal>) : State {
    override fun next(token: Token): State =
        when (token) {
            is KeySeparator -> WaitingLiteral(file, key)
            is Literal -> {
                val last = key.last().merge(token)
                val init = key.dropLast(1)
                WaitingLiteralOrSeparator(file, init + last)
            }
            else -> Error("Invalid token $token")
        }
    override fun fullKey(isTemplate: Boolean, source: String): FullKey? = FullKey(source, file, key, isTemplate)
}

class ExpressionKeyParser {
    fun parse(
        normalized: List<KeyElement>,
        isTemplate: Boolean = false,
        nsSeparator: String = ":",
        keySeparator: String = ".",
        stopCharacters: String = ""
    ): FullKey? {
        val source = normalized.fold(""){acc, item -> acc + item.text}
        val regex = "\\s".toRegex()
        if (source.contains(regex) || stopCharacters.any {char -> source.contains(char)}) {
            return null
        }
        return Tokenizer(nsSeparator, keySeparator)
            .tokenizeAll(normalized)
            .fold(Start(null) as State) { state, token ->
                state.next(token)
            }
            .fullKey(isTemplate, source)
    }
}
