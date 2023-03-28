package com.eny.i18n.plugin.key.parser

import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.*
import com.eny.i18n.plugin.parser.RawKey

data class CompositeKey(val ns: Literal?, val key: List<Literal>)

/**
 * Parses list of normalized key elements into FullKey
 */
class KeyParser(private val tokenizer: Tokenizer) {

    /**
     * Parses list of key elements into i18n key
     */
    fun parse(
        rawKey: RawKey,
        emptyNamespace: Boolean = false,
        firstComponentNamespace: Boolean = false,
        keyPrefix: RawKey? = null
    ): FullKey? {
        val startState = if (emptyNamespace) {
            if (firstComponentNamespace) {
                Start(null, KeySeparator)
            }
            else {
                WaitingLiteral(file = null, key = emptyList())
            }
        } else {
            Start(null)
        }
        val (source, tokenized) = tokenizer.tokenize(rawKey.keyElements)
        return tokenized
            .fold(startState) { state, token -> state.next(token) }
            .fullKey()?.let {
                (ns, key) -> FullKey(source, ns, key, rawKey.arguments, listOf(), null)
            }
    }
}

/**
 * Parsing state machine's state
 */
private interface State {
    /**
     * Process next token
     */
    fun next(token: Token): State

    /**
     * Get current parsed key
     */
    fun fullKey(): CompositeKey? = null
}

/**
 * Final error state
 */
private data class Error(val msg: String): State {
    override fun next(token: Token): State = this
}

/**
 * Initial state
 */
private class Start(private val init: Literal?, private val nsSeparator: Separator = NsSeparator) : State {
    override fun next(token: Token): State =
        when {
            token == nsSeparator && init != null  -> WaitingLiteral(init, listOf())
            token is KeySeparator && init != null -> WaitingLiteral(null, listOf(init))
            token is Literal -> Start(init?.merge(token) ?: token, nsSeparator)
            else -> Error("Invalid ns separator position (0)") // Never get here
        }
    override fun fullKey(): CompositeKey? = init?.let {CompositeKey(null, listOf(it))}
}

/**
 * Waiting literal state
 */
private class WaitingLiteral(private val file: Literal?, val key: List<Literal>) : State {
    override fun next(token: Token): State =
        when (token) {
            is Literal -> WaitingLiteralOrSeparator(file, key + token)
            else -> Error("Invalid token $token")
        }
    override fun fullKey(): CompositeKey = CompositeKey(file, key + Literal("", 0))
}

/**
 * Waiting literal or separator
 */
private class WaitingLiteralOrSeparator(val file: Literal?, val key: List<Literal>) : State {
    override fun next(token: Token): State =
        when (token) {
            is Literal -> WaitingLiteralOrSeparator(file, key.dropLast(1) + key.last().merge(token))
            is KeySeparator -> WaitingLiteral(file, key)
            else -> Error("Invalid token $token")
        }
    override fun fullKey(): CompositeKey = CompositeKey(file, key)
}
