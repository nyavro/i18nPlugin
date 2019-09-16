package utils

import com.eny.i18n.plugin.utils.Token

interface TestBase {
    fun extractTexts(list: List<Token>) = list.map { token -> token.text()}
    fun extractLengths(list: List<Token>?): List<Int> = (list ?: listOf()).map { token -> token.textLength()}
}