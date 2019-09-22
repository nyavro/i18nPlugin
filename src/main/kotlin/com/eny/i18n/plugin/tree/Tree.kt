package com.eny.i18n.plugin.tree

interface Tree<T> {
    fun findChild(name: String): Tree<T>?
    fun isLeaf(): Boolean = !isTree()
    fun isTree(): Boolean
    fun value(): T
}

