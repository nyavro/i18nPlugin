package com.eny.i18n.plugin.key.parser

import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.*
import com.eny.i18n.plugin.parser.KeyNormalizer
import com.eny.i18n.plugin.parser.KeyNormalizerImpl
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.KeyElementType

/**
 * Parses list of normalized key elements into FullKey
 */
class KeyParser(private val normalizer: KeyNormalizer = KeyNormalizerImpl()) {

    /**
     * Parses text to i18n key
     */
    fun parse(text: String, nsSeparator: String, keySeparator: String, emptyNamespace: Boolean) =
        parse(Pair(listOf(KeyElement.literal(text)), null), nsSeparator, keySeparator, emptyNamespace)

    /**
     * Parses list of key elements into i18n key
     */
    fun parse(
        pair: Pair<List<KeyElement>, List<String>?>,
        nsSeparator: String = ":",
        keySeparator: String = ".",
        emptyNamespace: Boolean = false
    ): FullKey? {
        val elements = pair.first
        val namespaces = pair.second
        val normalized = normalizer.normalize(elements)
        val source = normalized.fold(""){ acc, item -> acc + item.text }
        val startState = if (emptyNamespace) {
            WaitingLiteral(file = null, key = emptyList())
        } else {
            Start(null)
        }
        val isTemplate = normalized.any { it.type == KeyElementType.TEMPLATE }
        return Tokenizer(nsSeparator, keySeparator)
            .tokenizeAll(normalized)
            .fold(startState) { state, token ->
                state.next(token)
            }
            .fullKey(isTemplate, source, namespaces)
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
    fun fullKey(isTemplate: Boolean, source: String, namespaces: List<String>?): FullKey? = null
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
private class Start(private val init: Literal?) : State {
    override fun next(token: Token): State =
        when {
            token is NsSeparator && init != null  -> WaitingLiteral(init, listOf())
            token is KeySeparator && init != null -> WaitingLiteral(null, listOf(init))
            token is Literal -> Start(init?.merge(token) ?: token)
            else -> Error("Invalid ns separator position (0)") // Never get here
        }
    override fun fullKey(isTemplate: Boolean, source: String, namespaces: List<String>?): FullKey? =
        init?.let {FullKey(source, null, listOf(it), isTemplate, namespaces)}
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
    override fun fullKey(isTemplate: Boolean, source: String, namespaces: List<String>?): FullKey? =
        FullKey(source, file, key + Literal("", 0), isTemplate, namespaces)
}

/**
 * Waiting literal or separator
 */
private class WaitingLiteralOrSeparator(val file: Literal?, val key: List<Literal>) : State {
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
    override fun fullKey(isTemplate: Boolean, source: String, namespaces: List<String>?): FullKey? =
        FullKey(source, file, key, isTemplate, namespaces)
}