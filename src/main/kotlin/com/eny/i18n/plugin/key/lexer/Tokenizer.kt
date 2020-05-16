package com.eny.i18n.plugin.key.lexer

import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.KeyElementType
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
data class Literal(val text: String, val length: Int = text.length, val isTemplate: Boolean = false): Token {
    /**
     * Merges two tokens
     */
    fun merge(token: Literal): Literal = Literal(text + token.text, length + token.length)
}

/**
 * Tokenizer of translation keys
 */
class Tokenizer(private val nsSeparator: String, private val keySeparator: String) {

    /**
     * Tokenize list of key elements into list of tokens
     */
    fun tokenizeAll(elements: List<KeyElement>): List<Token> = elements.flatMap (::tokenize)

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
                    true
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