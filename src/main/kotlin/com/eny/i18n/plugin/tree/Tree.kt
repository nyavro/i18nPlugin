package com.eny.i18n.plugin.tree

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
     * Finds children by name matching regex
     */
    fun findChildren(regex: Regex): List<Tree<T>>
}

/**
 * Reversed tree wrapper
 */
interface FlippedTree<T> {
    /**
     * Gets name of current node
     */
    fun name(): String

    /**
     * Checks if current node is root
     */
    fun isRoot(): Boolean

    /**
     * Gets current node parents
     */
    fun parents(): List<FlippedTree<T>>
}

