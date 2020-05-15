package com.eny.i18n.plugin.tree

/**
 * Composes key from element's location in tree
 */
interface KeyComposer<T> {
    /**
     * Composes string representation of key by given path
     */
    fun composeKey(tree: FlippedTree<T>,
           nsSeparator: String=":", keySeparator: String=".", pluralSeparator: String="-", defaultNs: List<String> = listOf("translation"), dropRoot: Boolean = false): String {
        val parents = tree.parents()
        val criteria = {node: FlippedTree<T> -> node.isRoot() && (defaultNs.contains(node.name()) || dropRoot)}
        val fixPlural = {node: FlippedTree<T> -> if(node.isRoot()) node.name() else node.name().substringBefore(pluralSeparator)}
        val processNode = {node: FlippedTree<T>, index: Int ->
            fixPlural(node) + if (index < parents.size - 1) if (node.isRoot()) nsSeparator else keySeparator else ""}
        val fixDefaultNs = {node: FlippedTree<T>, index: Int -> if(criteria(node)) "" else processNode(node, index)}
        return parents.foldIndexed("") {
            index, acc, item ->
                acc + fixDefaultNs(item, index)
        }
    }
}