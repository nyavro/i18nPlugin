package com.eny.i18n.plugin.ide.references.code

import com.eny.i18n.plugin.factory.ReferenceAssistant
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PropertyReference
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.LocalizationSourceSearch
import com.eny.i18n.plugin.utils.unQuote
import com.eny.i18n.plugin.utils.whenMatches
import com.eny.i18n.plugin.utils.whenNotEmpty
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

data class ReferenceDescriptor(val reference: PropertyReference<PsiElement>, val host: Pair<PsiElement, String>?)

/**
 * I18nReference to json/yaml translation
 */
class I18nReference(element: PsiElement, textRange: TextRange, val references: List<ReferenceDescriptor>) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    private fun filterMostResolved(): List<ReferenceDescriptor> {
        val mostResolved = references.maxBy {it.reference.path.size}?.reference?.path?.size
        return references.filter {it.reference.path.size == mostResolved}
    }

    private fun findProperties(): List<PsiElement> =
        filterMostResolved()
            .mapNotNull { item -> item.reference.element?.let {
                    val res = if (it.isTree()) {
                        val parent = it.value().parent
                        (parent as? PsiFile) ?: parent.firstChild
                    } else it.value()
                    item.host?.first ?: res
                }
            }

    override fun resolve(): PsiElement? = multiResolve(false).whenMatches {it.size==1}?.first()?.element

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
        findProperties().map(::PsiElementResolveResult).toTypedArray()
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
                            .findFilesByNames(fullKey.allNamespaces(), element)
                            .map {
                                ReferenceDescriptor(resolveCompositeKey(fullKey.compositeKey, PsiElementTree.create(it.element)), it.host)
                            }
                            .filter { it.reference.path.isNotEmpty() }
                            .whenNotEmpty { listOf(I18nReference(element, TextRange(1 + element.text.unQuote().indexOf(fullKey.source), element.textLength - 1), it)) }
                    } ?: emptyList()
                }
            }
        )
    }
}
