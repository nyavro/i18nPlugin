package com.eny.i18n.plugin.utils

import java.util.*

interface Token
interface Separator : Token
object NsSeparator: Separator
object KeySeparator: Separator

data class Literal(val text: String, val length: Int = text.length, val dot: Int = 0): Token {
    fun merge(token: Literal): Literal = Literal(text + token.text, length + token.length)
}

class Tokenizer(private val nsSeparator: String, private val keySeparator: String) {

    fun tokenizeAll(elements: List<KeyElement>): List<Token> = elements.flatMap {item -> tokenize(item)}

    fun tokenize(element: KeyElement): List<Token> =
        when {
            element.type == KeyElementType.LITERAL -> tokenizeLiteral(element.text)
            else -> flattenTemplateExpression(element.text, element.resolvedTo)
        }

    private fun flattenTemplateExpression(text: String, resolvedTo: String?): List<Token> =
        setChildLengths(
            text,
            if (resolvedTo != null) tokenizeLiteral(resolvedTo)
            else listOf(Literal("*", 1, 0))
        )

    private fun tokenizeLiteral(literal: String): List<Token> =
        StringTokenizer(literal, nsSeparator + keySeparator, true).toList().map {
            token ->
                when (token) {
                    nsSeparator -> NsSeparator
                    keySeparator -> KeySeparator
                    else -> Literal(token.toString(), token.toString().length)
                }
        }

    private fun setChildLengths(expression: String, resolved: List<Token>): List<Token> {
        val endsWithSeparator = resolved.last() is KeySeparator
        val startsWithSeparator = resolved.first() is KeySeparator
        return resolved.foldIndexed(Pair(false, listOf<Token>())) {
            index, (isFirstLiteralSet, result), token ->
                if (token is Literal)
                    Pair (
                        true,
                        result + Literal(
                            token.text,
                            if (isFirstLiteralSet) 0 else expression.length,
                            if (startsWithSeparator && (index == 1) || endsWithSeparator && (index == resolved.lastIndex - 1)) 1 else 0
                        )
                    )
                else Pair (isFirstLiteralSet, result + token)
        }.second
    }
}