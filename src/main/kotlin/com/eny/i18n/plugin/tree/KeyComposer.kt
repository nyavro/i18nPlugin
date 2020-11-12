package com.eny.i18n.plugin.tree

import com.eny.i18n.plugin.utils.headTail
import com.eny.i18n.plugin.utils.whenMatches
import com.eny.i18n.plugin.utils.whenMatchesDo

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
    fun composeKey(parents: List<String>,
           nsSeparator: String=":", keySeparator: String=".", pluralSeparator: String="-", defaultNs: List<String> = listOf("translation"), dropRoot: Boolean = false): String {
        val (head, tail) = parents.headTail()
        return listOf(
            head.whenMatches {!(defaultNs.contains(it) || dropRoot)},
            tail?.joinToString(keySeparator)?.let {fixPlural(it, pluralSeparator)}
        )
            .mapNotNull {it}
            .joinToString(nsSeparator)
    }
}