package com.eny.i18n.plugin

import com.eny.i18n.plugin.utils.CompositeKeyResolver
import com.eny.i18n.plugin.utils.JsonSearchUtil
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

class I18nReference(element: PsiElement, textRange: TextRange) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference, CompositeKeyResolver {
    private val i18nFullKey = I18nFullKey.parse(element.text.substring(textRange.getStartOffset(), textRange.getEndOffset()))
    private val search = JsonSearchUtil(element.project)

    private fun findProperties(): List<PsiElement> {
        return if (i18nFullKey != null && i18nFullKey.fileName != null) {
            search
                .findFilesByName(i18nFullKey.fileName)
                .mapNotNull { jsonRoot -> resolveCompositeKeyProperty(i18nFullKey.compositeKey, jsonRoot) }
        } else listOf()
    }

    override fun resolve(): PsiElement? {
        val res = multiResolve(false)
        return if (res.size == 1) {
            res.firstOrNull()?.element
        }
        else null
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
        findProperties()
            .map{property -> PsiElementResolveResult(property)}
            .toTypedArray()

    override fun getVariants(): Array<Any> =
        findProperties()
            .map {property -> LookupElementBuilder.create(property).withTypeText(property.getContainingFile().getName())}
            .toTypedArray()
}

/**
 * Provides navigation from i18n key to it's value in json
 */
class I18nReferenceContributor: PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(PsiElement::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                        if (element is PsiLiteralValue) {
                            val value = element.value
                            if (value is String) {
                                return arrayOf(I18nReference(element, TextRange(1, value.length + 1)))
                            }
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )
    }
}

