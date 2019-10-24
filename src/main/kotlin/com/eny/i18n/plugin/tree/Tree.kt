package com.eny.i18n.plugin.tree

interface Tree<T> {
    fun findChild(name: String): Tree<T>?
    fun isLeaf(): Boolean = !isTree()
    fun isTree(): Boolean
    fun value(): T
    fun findChildren(regex: Regex): List<Tree<T>>
}

interface FlippedTree<T> {
    fun getParent(): FlippedTree<T>?
    fun name(): String
}

