package com.eny.i18n.plugin.ide.references.code

import com.eny.i18n.plugin.factory.ReferenceAssistant
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PropertyReference
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.tree.Tree
import com.eny.i18n.plugin.utils.LocalizationSourceSearch
import com.eny.i18n.plugin.utils.whenMatches
import com.eny.i18n.plugin.utils.whenNonEmpty
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

/**
 * I18nReference to json/yaml translation
 */
class I18nReference(element: PsiElement, textRange: TextRange, val references: List<PropertyReference<PsiElement>>) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    private fun filterMostResolved(list: List<PropertyReference<PsiElement>>): List<PropertyReference<PsiElement>> {
        val mostResolved = list.maxBy {ref -> ref.path.size}?.path?.size
        return list.filter { ref -> ref.path.size == mostResolved}
    }

    private fun findProperties(): List<Tree<PsiElement>> = filterMostResolved(references).mapNotNull {item -> item.element}

    override fun resolve(): PsiElement? = multiResolve(false).whenMatches {it.size==1}?.first()?.element

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
        findProperties()
            .map {property ->
                PsiElementResolveResult(
                    if (property.isTree()) {
                        val parent = property.value().parent
                        if (parent is PsiFile) parent
                        else parent.firstChild
                    }
                    else property.value()
                )
            }
            .toTypedArray()
}

/**
 * Provides navigation from i18n key to it's value in json
 */
abstract class ReferenceContributorBase(private val referenceContributor: ReferenceAssistant): PsiReferenceContributor(), CompositeKeyResolver<PsiElement> {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            referenceContributor.pattern(),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    return getReferencesList(element).toTypedArray()
                }
                private fun getReferencesList(element: PsiElement): List<PsiReference> {
                    return referenceContributor.extractKey(element)?.let { fullKey ->
                        LocalizationSourceSearch(element.project)
                            .findFilesByName(fullKey.ns?.text)
                            .map {resolveCompositeKey(fullKey.compositeKey, PsiElementTree.create(it.element))}
                            .filter {it.path.isNotEmpty()}
                            .whenNonEmpty {listOf(I18nReference(element, TextRange(1, element.textLength - 1), it))}
                    } ?: emptyList()
                }
            }
        )
    }
}

