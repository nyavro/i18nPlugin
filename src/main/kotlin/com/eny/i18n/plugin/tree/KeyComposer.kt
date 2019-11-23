package com.eny.i18n.plugin.tree

interface KeyComposer<T> {
    fun composeKey(tree: FlippedTree<T>,
           nsSeparator: String=":", keySeparator: String=".", pluralSeparator: String="-", defaultNs: String = "translation", dropRoot: Boolean = false): String {
        val ancestors = tree.ancestors()
        val criteria = {node: FlippedTree<T> -> node.isRoot() && (node.name()==defaultNs || dropRoot)}
        val fixPlural = {node: FlippedTree<T> -> if(node.isRoot()) node.name() else node.name().substringBefore(pluralSeparator)}
        val processNode = {node: FlippedTree<T>, index: Int ->
            fixPlural(node) + if (index < ancestors.size - 1) if (node.isRoot()) nsSeparator else keySeparator else ""}
        val fixDefaultNs = {node: FlippedTree<T>, index: Int -> if(criteria(node)) "" else processNode(node, index)}
        return ancestors.foldIndexed("") {
            index, acc, item ->
                acc + fixDefaultNs(item, index)
        }
    }
}