package com.eny.i18n.plugin.tree

import com.eny.i18n.plugin.utils.Literal

/**
 * Property reference represents PsiElement and it's path from Json file root
 */
data class PropertyReference<T>(val path: List<Literal>, val element: Tree<T>?, val unresolved: List<Literal>, val isPlural: Boolean = false)

interface CompositeKeyResolver<T> {

    /**
     * @param {List<String>} compositeKey Composite key(path) to resolve
     * @param {PsiElement?} root Root element to find property from
     * Returns PropertyReference by composite key
     */
    fun resolveCompositeKey(compositeKey: List<Literal>, root: Tree<T>?): PropertyReference<T> {
        return compositeKey.fold(PropertyReference(listOf(), root, listOf())) {
            propertyReference, key ->
                if (propertyReference.element != null && propertyReference.element.isTree() && propertyReference.unresolved.isEmpty()) {
                    val value = propertyReference.element.findChild(key.text)
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
    fun tryToResolvePlural(propertyReference: PropertyReference<T>): List<PropertyReference<T>> {
        return if (propertyReference.unresolved.size == 1 && propertyReference.element != null && propertyReference.element.isTree()) {
            val singleUnresolvedKey = propertyReference.unresolved.get(0)
            val plurals = listOf("1","2","5").mapNotNull {
                pluralIndex -> propertyReference.element.findChild("${singleUnresolvedKey.text}-$pluralIndex")
            }.map {
                plural -> PropertyReference(propertyReference.path + singleUnresolvedKey, plural, listOf(), isPlural = true)
            }
            if (plurals.isEmpty()) listOf(propertyReference) else plurals
        } else listOf(propertyReference)
    }
}