package utils

import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.Literal

interface TestBase {
    fun toTestString(fullKey: FullKey?): String =
        if (fullKey != null)
            listOf(textWithLength(fullKey.ns), extractTextWithLength(fullKey.compositeKey)).filter {v -> v.isNotEmpty()}.joinToString(":")
        else ""
    fun literalsList(vararg text: String): List<Literal> = text.toList().map {item -> Literal(item)}

    private fun textWithLength(token: Literal?): String = if (token != null) "${token.text}{${token.length}}" else ""
    private fun extractTextWithLength(list: List<Literal>?): String = (list ?: listOf()).map { token -> textWithLength(token)}.joinToString(".")
}