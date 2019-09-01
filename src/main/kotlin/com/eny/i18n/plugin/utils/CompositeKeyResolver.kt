package com.eny.i18n.plugin.utils

import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil

/**
 * Property reference represents PsiElement and it's path from Json file root
 */
data class PropertyReference(val path: List<String>, val element: PsiElement?)

interface CompositeKeyResolver {

    /**
     * @param {List<String>} compositeKey Composite key(path) to resolve
     * @param {PsiElement} fileNode Root element to find property from
     * Returns PropertyReference by composite key
     */
    fun resolveCompositeKey(compositeKey: List<String>, fileNode: PsiElement): PropertyReference {
        val root: PsiElement? = PsiTreeUtil.getChildOfType(fileNode, JsonObject::class.java)
        return compositeKey.fold(PropertyReference(listOf<String>(), root)) { propertyReference, key ->
            if (propertyReference.element is JsonObject) {
                val value = propertyReference.element.findProperty(key)?.value
                PropertyReference(if (value != null) propertyReference.path + key else propertyReference.path, value)
            } else if (propertyReference.element is JsonStringLiteral) {
                propertyReference.copy(element = null)
            } else propertyReference
        }
    }

    /**
     * Returns PsiElement by composite key from file's root node
     */
    fun resolveCompositeKeyProperty(compositeKey: List<String>, fileNode: PsiElement): PsiElement? {
        val root: PsiElement? = PsiTreeUtil.getChildOfType(fileNode, JsonObject::class.java)
        return compositeKey.fold(root) {
            node, key -> if (node != null && node is JsonObject) node.findProperty(key)?.value else node
        }
    }

    private fun String.unQuote(): String =
        if (this.endsWith('\"') && this.startsWith('\"')) this.substring(1, this.length - 1)
        else this

    fun listCompositeKeyVariants(compositeKey: List<String>, fileNode: PsiElement, substringSearch: Boolean): List<String> {
        val searchPrefix = if (substringSearch) compositeKey.last() else ""
        val fixedKey = if (substringSearch) {
            compositeKey.dropLast(1)
        } else compositeKey
        return resolveCompositeKeyProperty(fixedKey, fileNode)?.
                    node?.
                    getChildren(TokenSet.create(JsonElementTypes.PROPERTY))?.
                    asList()?.
                    map { node -> node.firstChildNode.text.unQuote()}?.
                    filter { key -> key.startsWith(searchPrefix)}?.
                    map { key -> key.substringAfter(searchPrefix)} ?:
            listOf()
    }
}