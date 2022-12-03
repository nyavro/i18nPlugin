package com.eny.i18n.plugin.tree

import com.eny.i18n.LocalizationSource
import com.eny.i18n.plugin.key.lexer.Literal
import com.intellij.psi.PsiElement

/**
 * Property reference represents PsiElement and it's path from translation root
 */
data class PropertyReference(
    val path: List<Literal>,
    val element: Tree<PsiElement>?,
    val unresolved: List<Literal>,
    val localizationSource: LocalizationSource,
    val isPlural: Boolean = false
)

/**
 * Key resolving utils
 */
interface CompositeKeyResolver<T> {

    fun resolve(compositeKey: List<Literal>, localizationSource: LocalizationSource, pluralSeparator: String): List<PropertyReference> =
        tryToResolvePlural(
            resolveCompositeKey(
                compositeKey,
                localizationSource
            ),
            pluralSeparator,
            localizationSource
        )

    /**
     * @param {List<String>} compositeKey Composite key(path) to resolve
     * @param {PsiElement?} root Root element to find property from
     * @param {FileType} type Localization source type
     * Returns PropertyReference by composite key
     */
    fun resolveCompositeKey(compositeKey: List<Literal>, localizationSource: LocalizationSource): PropertyReference {
        return compositeKey.fold(PropertyReference(listOf(), localizationSource.tree, listOf(), localizationSource)) {
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
    fun tryToResolvePlural(propertyReference: PropertyReference, pluralSeparator: String, localizationSource: LocalizationSource): List<PropertyReference> {
        return if (propertyReference.unresolved.size == 1 && propertyReference.element != null && propertyReference.element.isTree()) {
            val singleUnresolvedKey = propertyReference.unresolved[0]
            val plurals = listOf("1","2","5").mapNotNull {
                propertyReference.element.findChild("${singleUnresolvedKey.text}${pluralSeparator}$it")
            }.map {
                PropertyReference(propertyReference.path + singleUnresolvedKey, it, listOf(), localizationSource, true)
            }
            if (plurals.isEmpty()) listOf(propertyReference) else plurals
        } else listOf(propertyReference)
    }

    /**
     * Returns PsiElement by composite key from file's root node
     */
    fun resolveCompositeKeyProperty(compositeKey: List<Literal>, localizationSource: LocalizationSource): Tree<PsiElement>? =
        resolveCompositeKey(compositeKey, localizationSource).let {ref -> if (ref.unresolved.isNotEmpty()) null else ref.element}

    /**
     * Returns keys at current composite key position
     */
    fun listCompositeKeyVariants(fixedKey: List<Literal>, prefix: String, localizationSource: LocalizationSource): List<Tree<PsiElement>> =
        resolveCompositeKeyProperty(fixedKey, localizationSource)?.findChildren(prefix) ?: listOf()
}