package com.eny.i18n.plugin.utils

/**
 * Parsing state machine's state
 */
interface State {
    /**
     * Process next token
     */
    fun next(token: Token): State

    /**
     * Get current parsed key
     */
    fun fullKey(isTemplate: Boolean, source: String): FullKey? = null
}

/**
 * Final error state
 */
class Error(private val msg: String): State {
    override fun next(token: Token): State = this
    override fun toString(): String = "Error <$msg>"
}

/**
 * Initial state
 */
class Start(private val init: Literal?) : State {
    override fun next(token: Token): State =
        when {
            token is NsSeparator && init != null  -> WaitingLiteral(init, listOf())
            token is KeySeparator && init != null -> WaitingLiteral(null, listOf(init))
            token is Literal  -> Start(init?.merge(token) ?: token)
            else -> Error("Invalid ns separator position (0)") // Never get here
        }
}

/**
 * Waiting literal state
 */
class WaitingLiteral(private val file: Literal?, val key: List<Literal>) : State {
    override fun next(token: Token): State =
        when (token) {
            is Literal -> WaitingLiteralOrSeparator(file, key + token)
            else -> Error("Invalid token $token")
        }
    override fun fullKey(isTemplate: Boolean, source: String): FullKey? = FullKey(source, file, key + Literal(""), isTemplate)
}

/**
 * Waiting literal or separator
 */
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

/**
 * Parses list of normalized key elements into FullKey
 */
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
