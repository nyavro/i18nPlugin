package utils

import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.Token

interface TestBase {
    fun textWithLength(token: Token?): String = if (token != null) "${token.text()}{${token.textLength()}}" else ""
    fun extractTextWithLength(list: List<Token>?): String = (list ?: listOf()).map { token -> textWithLength(token)}.joinToString(".")
    fun toTestString(fullKey: FullKey?): String {
        return if (fullKey != null)
            listOf(textWithLength(fullKey.ns), extractTextWithLength(fullKey.compositeKey)).filter { v -> v.isNotEmpty()}.joinToString(":")
        else ""
    }
}