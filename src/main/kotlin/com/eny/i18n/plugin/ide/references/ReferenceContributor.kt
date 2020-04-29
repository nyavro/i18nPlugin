package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.parser.DummyContext
import com.eny.i18n.plugin.parser.FullKeyExtractor
import com.eny.i18n.plugin.parser.KeyExtractorImpl
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PropertyReference
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.tree.Tree
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.LocalizationFileSearch
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

/**
 * I18nReference to json/yaml translation
 */
class I18nReference(element: PsiElement, textRange: TextRange, val i18nFullKey: FullKey) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference, CompositeKeyResolver<PsiElement> {
    private val search = LocalizationFileSearch(element.project)

    private fun filterMostResolved(list: List<PropertyReference<PsiElement>>): List<PropertyReference<PsiElement>> {
        val mostResolved = list.maxBy {ref -> ref.path.size}?.path?.size
        return if (mostResolved != null)
            list.filter { ref -> ref.path.size == mostResolved}
        else
            list
    }

    private fun findProperties(): List<Tree<PsiElement>> =
        filterMostResolved(
            search
                .findFilesByName(i18nFullKey.ns?.text)
                .map { resolveCompositeKey(i18nFullKey.compositeKey, PsiElementTree.create(it.element)) }
                .filter { res -> res.path.isNotEmpty()}
        ).mapNotNull {item -> item.element}

    override fun resolve(): PsiElement? = multiResolve(false).firstOrNull()?.element

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
class ReferenceContributor: PsiReferenceContributor() {
    private val keyExtractor = FullKeyExtractor(DummyContext(), KeyExtractorImpl())
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    val i18nKeyLiteral = keyExtractor.extractI18nKeyLiteral(element)
                    if (i18nKeyLiteral != null) {
                        return arrayOf(I18nReference(element, TextRange(1, element.textLength - 1), i18nKeyLiteral))
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            }
        )
    }
}

