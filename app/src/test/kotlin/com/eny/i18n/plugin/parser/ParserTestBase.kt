package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.plugin.utils.KeyElement

/**
 * Base class and utils for parser unit tests
 */
interface ParserTestBase {
    /**
     * Gets string representation of full key
     */
    fun toTestString(fullKey: FullKey?): String =
        if (fullKey != null)
            listOf(textWithLength(fullKey.ns), extractTextWithLength(fullKey.compositeKey)).filter {v -> v.isNotEmpty()}.joinToString(":")
        else ""

    private fun textWithLength(token: Literal?): String = if (token != null) "${token.text}{${token.length}}" else ""
    private fun extractTextWithLength(list: List<Literal>?): String = (list ?: listOf()).map { token -> textWithLength(token)}.joinToString(".")

    /**
     * Parse utility
     */
    fun parse(elements: List<KeyElement>, isTemplate: Boolean = false, nsSeparator: String = ":", keySeparator: String = ".",
              stopCharacters: String = "", emptyNamespace: Boolean = false): FullKey? =
        KeyParserBuilder.withSeparators(nsSeparator, keySeparator).build().parse(Pair(elements, null), emptyNamespace)
}