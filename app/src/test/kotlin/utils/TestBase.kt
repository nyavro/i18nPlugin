package utils

import com.eny.i18n.plugin.ide.KeyRangesCalculator
import com.eny.i18n.plugin.key.lexer.Literal
import com.intellij.openapi.util.TextRange

/**
 * Base class and utils for unit tests
 */
interface TestBase {

    /**
     * Converts vararg string to list of literals
     */
    fun literalsList(vararg text: String): List<Literal> = text.toList().map {item -> Literal(item)}

    /**
     * Utility for AnnotationHolder
     */
    fun rangesCalculator(textRange: TextRange, isQuoted:Boolean = true) = KeyRangesCalculator(textRange, isQuoted)
}