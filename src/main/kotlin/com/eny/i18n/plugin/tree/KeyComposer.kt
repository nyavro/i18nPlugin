package com.eny.i18n.plugin.tree

interface KeyComposer<T> {
    fun composeKey(tree: FlippedTree<T>, nsSeparator: String, keySeparator: String): String {
        fun compose(node: FlippedTree<T>, appendSeparator: Boolean = true): String {
            val ancestors = node.ancestors()
            return ancestors.foldIndexed("") {
                index, acc, item ->
                    acc + item.name() + (
                        if (index < ancestors.size - 1) {
                            if (item.isRoot()) nsSeparator else keySeparator
                        } else ""
                    )
            }
        }
        return compose(tree, false)
    }
}