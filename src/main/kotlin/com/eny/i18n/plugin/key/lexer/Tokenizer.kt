package com.eny.i18n.plugin.key.lexer

import com.eny.i18n.plugin.parser.KeyNormalizer
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.KeyElementType
import com.eny.i18n.plugin.utils.foldWhileAccum
import java.util.*

/**
 * Represents i18n token
 */
interface Token

/**
 * Token - separator
 */
interface Separator : Token

/**
 * Namespace separator
 */
object NsSeparator: Separator

/**
 * Key separator
 */
object KeySeparator: Separator

/**
 * Represents key literal
 */
data class Literal(val text: String, val length: Int = text.length, val isTemp: Boolean = false): Token {
    /**
     * Merges two tokens
     */
    fun merge(token: Literal): Literal = Literal(text + token.text, length + token.length)
}

/**
 * Tokenizer interface
 */
interface Tokenizer {
    fun tokenize(elements: List<KeyElement>): Pair<String, List<Token>>
}

/**
 * Class connects Tokenizer and Normalizers
 */
class NormalizingTokenizer(private val tokenizer: Tokenizer, private val normalizers: List<KeyNormalizer>): Tokenizer {
    private fun normalize(element: KeyElement): KeyElement? = normalizers.foldWhileAccum(element, { acc, item -> item.normalize(acc) })
    private fun normalize(elements: List<KeyElement>) = elements.mapNotNull {normalize(it)}
    override fun tokenize(elements: List<KeyElement>): Pair<String, List<Token>> = tokenizer.tokenize(normalize(elements))
}

/**
 * Does no tokenization
 */
class NoTokenizer: Tokenizer {
    override fun tokenize(elements: List<KeyElement>): Pair<String, List<Token>> = Pair(
        elements.joinToString(""){it.text}, elements.map {Literal(it.text)}
    )
}

/**
 * Tokenizer of translation keys
 */
class NsKeyTokenizer(private val nsSeparator: String, private val keySeparator: String): Tokenizer {

    /**
     * Tokenize list of key elements into list of tokens
     */
    override fun tokenize(elements: List<KeyElement>): Pair<String, List<Token>> {
        return Pair(elements.joinToString(""){it.text}, elements.flatMap (::tokenize))
    }

    /**
     * Tokenizes single key element into list of tokens
     */
    fun tokenize(element: KeyElement): List<Token> =
        when {
            element.type == KeyElementType.LITERAL -> tokenizeLiteral(element.text)
            else -> listOf(
                Literal(
                    "*",
                    element.text.length,
                    false
                )
            )
        }

    private fun tokenizeLiteral(literal: String): List<Token> =
        StringTokenizer(literal, nsSeparator + keySeparator, true).toList().map {
            token ->
                when (token) {
                    nsSeparator -> NsSeparator
                    keySeparator -> KeySeparator
                    else -> Literal(token.toString(), token.toString().length)
                }
        }
}