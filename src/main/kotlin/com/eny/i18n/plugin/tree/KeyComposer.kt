package com.eny.i18n.plugin.tree

import com.eny.i18n.plugin.utils.headTail
import com.eny.i18n.plugin.utils.whenMatches
import com.eny.i18n.plugin.utils.whenMatchesDo

/**
 * Separators configuration
 */
data class Separators(val ns: String, val key: String, val plural: String)

/**
 * Composes key from element's location in tree
 */
interface KeyComposer<T> {

    private fun fixPlural(item:String, pluralSeparator: String): String =
        item.whenMatchesDo(
            {listOf(1,2,5).any {item.endsWith(pluralSeparator + it)}},
            {it.substringBeforeLast(pluralSeparator)}
        )

    /**
     * Composes string representation of key by given path
     */
    fun composeKey(parents: List<String>, separators: Separators = Separators(":", ".", "-"), defaultNs: List<String> = listOf("translation"), dropRoot: Boolean = false, firstComponentNs: Boolean = false): String {
        val (head, tail) = parents.headTail()
        return listOf(
            head.whenMatches {!(defaultNs.contains(it) || dropRoot)},
            tail?.joinToString(separators.key)?.let {fixPlural(it, separators.plural)}
        )
            .mapNotNull {it}
            .joinToString(if (firstComponentNs) separators.key else separators.ns)
    }
}