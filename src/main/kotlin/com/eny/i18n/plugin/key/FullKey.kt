package com.eny.i18n.plugin.key

import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.utils.nullableToList

/**
 * Represents translation key
 */
data class FullKey(
    val source: String,
    val ns: Literal?,
    val compositeKey:List<Literal>,
    val namespaces: List<String>? = null,
    val keyPrefix: List<Literal> = listOf(),
    val keyPrefixSource: String? = null,
    val isPartial: Boolean = false
) {
    fun allNamespaces(): List<String> {
        return ns?.text.nullableToList() + (namespaces ?: listOf())
    }
}
