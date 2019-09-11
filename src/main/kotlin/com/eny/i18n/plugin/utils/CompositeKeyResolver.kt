package com.eny.i18n.plugin.utils

import com.intellij.json.JsonElementTypes
import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil

/**
 * Property reference represents PsiElement and it's path from Json file root
 */
data class PropertyReference(val path: List<KeyElement>, val element: PsiElement?, val unresolved: List<KeyElement>, val isPlural: Boolean = false)

interface CompositeKeyResolver {

    /**
     * @param {List<String>} compositeKey Composite key(path) to resolve
     * @param {PsiElement} fileNode Root element to find property from
     * Returns PropertyReference by composite key
     */
    fun resolveCompositeKey(compositeKey: List<KeyElement>, fileNode: PsiElement): PropertyReference {
        val root: PsiElement? = PsiTreeUtil.getChildOfType(fileNode, JsonObject::class.java)
        return compositeKey.fold(PropertyReference(listOf(), root, listOf())) { propertyReference, key ->
            if (propertyReference.element is JsonObject) {
                val value = propertyReference.element.findProperty(key.text)?.value
                if (value == null) propertyReference.copy(unresolved = propertyReference.unresolved + key)
                else propertyReference.copy(path = propertyReference.path + key, element = value)
            } else propertyReference.copy(unresolved = propertyReference.unresolved + key)
        }
    }

    /**
     * Fix for plural key reference.
     * #
     * Consider composite key 'sample:root.key.plural'
     * and corresponding tree in sample.json:
     * "root": {
     *   "key": {
     *      "plural-1": "plu1",
     *      "plural-2": "plu2",
     *      "plural-5": "plu5",
     *   }
     * }
     * PropertyReference for this case is PropertyReference(path = ["root", "key"], element[key], unresolved = ["plural"])
     */
    fun tryToResolvePlural(propertyReference: PropertyReference): List<PropertyReference> {
        return if (propertyReference.unresolved.size == 1 && propertyReference.element is JsonObject) {
            val singleUnresolvedKey = propertyReference.unresolved.get(0)
            val plurals = listOf("1","2","5").mapNotNull {
                pluralIndex -> propertyReference.element.findProperty("${singleUnresolvedKey.text}-$pluralIndex")
            }.map {
                plural -> PropertyReference(propertyReference.path + singleUnresolvedKey, plural, listOf(), isPlural = true)
            }
            if (plurals.isEmpty()) listOf(propertyReference) else plurals
        } else listOf(propertyReference)
    }

    /**
     * Returns PsiElement by composite key from file's root node
     */
    fun resolveCompositeKeyProperty(compositeKey: List<KeyElement>, fileNode: PsiElement): PsiElement? {
        val root: PsiElement? = PsiTreeUtil.getChildOfType(fileNode, JsonObject::class.java)
        return compositeKey.fold(root) {
            node, key -> if (node != null && node is JsonObject) node.findProperty(key.text)?.value else node
        }
    }

    /**
     * Returns keys at current composite key position
     */
    fun listCompositeKeyVariants(compositeKey: List<KeyElement>, fileNode: PsiElement, substringSearch: Boolean): List<KeyElement> {
        val searchPrefix = if (substringSearch) compositeKey.last().text else ""
        val fixedKey = if (substringSearch) {
            compositeKey.dropLast(1)
        } else compositeKey
        return resolveCompositeKeyProperty(fixedKey, fileNode)?.
                    node?.
                    getChildren(TokenSet.create(JsonElementTypes.PROPERTY))?.
                    asList()?.
                    map { node -> node.firstChildNode.text.unQuote()}?.
                    filter { key -> key.startsWith(searchPrefix)}?.
                    map { key -> KeyElement.fromLiteral(key.substringAfter(searchPrefix))} ?:
            listOf()
    }

    private fun String.unQuote(): String =
            if (this.endsWith('\"') && this.startsWith('\"')) this.substring(1, this.length - 1)
            else this
}