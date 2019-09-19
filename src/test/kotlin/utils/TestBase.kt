package utils

import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.Token
import kotlin.test.assertEquals

interface TestBase {
    fun extractTexts(list: List<Token>) = list.map { token -> token.text()}
    fun extractLengths(list: List<Token>?): List<Int> = (list ?: listOf()).map { token -> token.textLength()}
    fun extractTextWithLength(list: List<Token>?): String = (list ?: listOf()).map { token -> "${token.text()}{${token.textLength()}}"}.joinToString(".")
    fun toTestString(fullKey: FullKey?): String {
        return if (fullKey != null)
            listOf(extractTextWithLength(fullKey.fileName), extractTextWithLength(fullKey.compositeKey)).filter {v -> v.isNotEmpty()}.joinToString(":")
        else ""
    }
}