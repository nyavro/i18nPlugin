package com.eny.i18n.plugin.key

/**
 * Tree wrapper
 */
interface Tree<T> {
    /**
     * Finds child by name
     */
    fun findChild(name: String): Tree<T>?

    /**
     * Checks if current tree node is a leaf
     */
    fun isLeaf(): Boolean = !isTree()

    /**
     * Checks if current tree node is a tree
     */
    fun isTree(): Boolean

    /**
     * Gets current node underlying value
     */
    fun value(): T

    /**
     * Finds children by name starting with prefix
     */
    fun findChildren(prefix: String): List<Tree<T>>
}