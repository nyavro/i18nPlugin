package com.eny.i18n.plugin.utils

import java.util.*

class Tokenizer(val nsSeparator: String, val keySeparator: String) {

    fun tokenizeAll(elements: List<KeyElement>): List<Token> = elements.flatMap {item -> tokenize(item)}

    fun tokenize(element: KeyElement): List<Token> {
        return when {
            element.type == KeyElementType.LITERAL -> tokenizeLiteral(element.text)
            element.resolvedTo != null -> listOf(TemplateExpression(element.text, tokenizeLiteral(element.resolvedTo)))
            else -> listOf(Asterisk)
        }
    }

    private fun tokenizeLiteral(literal: String): List<Token> {
        return StringTokenizer(literal, nsSeparator + keySeparator, true).toList().map {
            token ->
                when (token) {
                    nsSeparator -> NsSeparator(nsSeparator)
                    keySeparator -> KeySeparator(keySeparator)
                    else -> Literal(token.toString())
                }
        }
    }
}