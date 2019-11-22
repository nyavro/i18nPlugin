package com.eny.i18n.plugin.tree

interface KeyComposer<T> {
    fun composeKey(tree: FlippedTree<T>,
           nsSeparator: String=":", keySeparator: String=".", pluralSeparator: String="-", defaultNs: String = "translation"): String {
        val ancestors = tree.ancestors()
        val fixPlural = {node: FlippedTree<T> -> if(node.isRoot()) node.name() else node.name().substringBefore(pluralSeparator)}
        val processNode = {node: FlippedTree<T>, index: Int ->
            fixPlural(node) + if (index < ancestors.size - 1) if (node.isRoot()) nsSeparator else keySeparator else ""}
        val fixDefaultNs = {node: FlippedTree<T>, index: Int -> if(node.isRoot() && node.name()==defaultNs) "" else processNode(node, index)}
        return ancestors.foldIndexed("") {
            index, acc, item ->
                acc + fixDefaultNs(item, index)
        }
    }
}