package utils

import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.Literal

interface TestBase {
    fun textWithLength(token: Literal?): String = if (token != null) "${token.text}{${token.length}}" else ""
    fun extractTextWithLength(list: List<Literal>?): String = (list ?: listOf()).map { token -> textWithLength(token)}.joinToString(".")
    fun toTestString(fullKey: FullKey?): String {
        return if (fullKey != null)
            listOf(textWithLength(fullKey.ns), extractTextWithLength(fullKey.compositeKey)).filter { v -> v.isNotEmpty()}.joinToString(":")
        else ""
    }
}