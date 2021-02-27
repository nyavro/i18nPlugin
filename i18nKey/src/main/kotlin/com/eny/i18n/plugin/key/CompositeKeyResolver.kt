package com.eny.i18n.plugin.key

import com.eny.i18n.plugin.key.lexer.Literal

/**
 * Property reference represents PsiElement and it's path from Json file root
 */
data class PropertyReference<T, R>(
        val path: List<Literal>,
        val element: Tree<T>?,
        val unresolved: List<Literal>,
        val type: R,
        val isPlural: Boolean = false)

/**
 * Key resolving utils
 */
interface CompositeKeyResolver<T, R> {

    fun resolve(compositeKey: List<Literal>, root: Tree<T>?, pluralSeparator: String, type: R): Iterable<PropertyReference<T, R>> =
        tryToResolvePlural(
            resolveCompositeKey(
                compositeKey,
                root,
                type
            ),
            pluralSeparator,
            type
        )

    /**
     * @param {List<String>} compositeKey Composite key(path) to resolve
     * @param {PsiElement?} root Root element to find property from
     * @param {FileType} type Localization source type
     * Returns PropertyReference by composite key
     */
    fun resolveCompositeKey(compositeKey: List<Literal>, root: Tree<T>?, type: R): PropertyReference<T, R> {
        return compositeKey.fold(PropertyReference(listOf(), root, listOf(), type)) {
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
    fun tryToResolvePlural(propertyReference: PropertyReference<T, R>, pluralSeparator: String, type: R): List<PropertyReference<T, R>> {
        return if (propertyReference.unresolved.size == 1 && propertyReference.element != null && propertyReference.element.isTree()) {
            val singleUnresolvedKey = propertyReference.unresolved[0]
            val plurals = listOf("1","2","5").mapNotNull {
                propertyReference.element.findChild("${singleUnresolvedKey.text}${pluralSeparator}$it")
            }.map {
                PropertyReference(propertyReference.path + singleUnresolvedKey, it, listOf(), type, isPlural = true)
            }
            if (plurals.isEmpty()) listOf(propertyReference) else plurals
        } else listOf(propertyReference)
    }

    /**
     * Returns PsiElement by composite key from file's root node
     */
    fun resolveCompositeKeyProperty(compositeKey: List<Literal>, root: Tree<T>?, type: R): Tree<T>? =
        resolveCompositeKey(compositeKey, root, type).let {ref -> if (ref.unresolved.isNotEmpty()) null else ref.element}

    /**
     * Returns keys at current composite key position
     */
    fun listCompositeKeyVariants(fixedKey: List<Literal>, root: Tree<T>?, prefix: String, type: R): List<Tree<T>> =
        resolveCompositeKeyProperty(fixedKey, root, type)?.findChildren(prefix) ?: listOf()
}