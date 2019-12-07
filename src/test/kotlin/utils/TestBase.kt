package utils

import com.eny.i18n.plugin.utils.*

interface TestBase {
    fun toTestString(fullKey: FullKey?): String =
        if (fullKey != null)
            listOf(textWithLength(fullKey.ns), extractTextWithLength(fullKey.compositeKey)).filter {v -> v.isNotEmpty()}.joinToString(":")
        else ""
    fun literalsList(vararg text: String): List<Literal> = text.toList().map {item -> Literal(item)}

    private fun textWithLength(token: Literal?): String = if (token != null) "${token.text}{${token.length}}" else ""
    private fun extractTextWithLength(list: List<Literal>?): String = (list ?: listOf()).map { token -> textWithLength(token)}.joinToString(".")
    fun parse(elements: List<KeyElement>, isTemplate: Boolean = false, nsSeparator: String = ":", keySeparator: String = ".",
              stopCharacters: String = ""): FullKey? =
        ExpressionKeyParser().parse(elements, isTemplate, nsSeparator, keySeparator, stopCharacters)
}