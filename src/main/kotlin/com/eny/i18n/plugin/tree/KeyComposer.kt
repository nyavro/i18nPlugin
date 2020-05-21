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
    fun composeKey(tree: FlippedTree<T>,
           nsSeparator: String=":", keySeparator: String=".", pluralSeparator: String="-", defaultNs: List<String> = listOf("translation"), dropRoot: Boolean = false): String {
        val pair = tree.parents().map {it.name()}.headTail()
        return listOf(
            pair.first.whenMatches {!(defaultNs.contains(it) || dropRoot)},
            pair.second?.joinToString(keySeparator)?.let {fixPlural(it, pluralSeparator)}
        )
            .mapNotNull {it}
            .joinToString(nsSeparator)
    }
}