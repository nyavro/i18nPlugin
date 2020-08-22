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
    val isTemplate: Boolean = false,
    val namespaces: List<String>? = null
) {
    fun allNamespaces(): List<String> {
        return ns?.text.nullableToList() + (namespaces ?: listOf())
    }
}