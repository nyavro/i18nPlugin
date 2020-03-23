package utils

import com.eny.i18n.plugin.parser.KeyNormalizer
import com.eny.i18n.plugin.utils.*
import com.intellij.openapi.util.TextRange

/**
 * Base class and utils for unit tests
 */
interface TestBase {
    /**
     * Gets string representation of full key
     */
    fun toTestString(fullKey: FullKey?): String =
        if (fullKey != null)
            listOf(textWithLength(fullKey.ns), extractTextWithLength(fullKey.compositeKey)).filter {v -> v.isNotEmpty()}.joinToString(":")
        else ""

    /**
     * Converts vararg string to list of literals
     */
    fun literalsList(vararg text: String): List<Literal> = text.toList().map {item -> Literal(item)}

    private fun textWithLength(token: Literal?): String = if (token != null) "${token.text}{${token.length}}" else ""
    private fun extractTextWithLength(list: List<Literal>?): String = (list ?: listOf()).map { token -> textWithLength(token)}.joinToString(".")

    /**
     * Parse utility
     */
    fun parse(elements: List<KeyElement>, isTemplate: Boolean = false, nsSeparator: String = ":", keySeparator: String = ".",
              stopCharacters: String = ""): FullKey? =
        ExpressionKeyParser(object: KeyNormalizer {}).parse(elements, isTemplate, nsSeparator, keySeparator, stopCharacters)

    /**
     * Utility for AnnotationHolder
     */
    fun facade(textRange: TextRange, isQuoted:Boolean = true) = KeyRangesCalculator(textRange, isQuoted)
}