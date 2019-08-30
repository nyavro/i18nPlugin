package com.eny.i18n.plugin

import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.psi.PsiElement
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
}