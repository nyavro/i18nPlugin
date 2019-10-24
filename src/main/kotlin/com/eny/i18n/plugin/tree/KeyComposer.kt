package com.eny.i18n.plugin.tree

interface KeyComposer<T> {
    fun composeKey(tree: FlippedTree<T>, nsSeparator: String, keySeparator: String): String {
        fun compose(node: FlippedTree<T>, appendSeparator: Boolean = true): String {
            val parent = node.getParent()
            return if (parent==null) node.name() + if (appendSeparator) nsSeparator else ""
            else compose(parent) + node.name() + if (appendSeparator) keySeparator else ""
        }
        return compose(tree, false)
    }
}