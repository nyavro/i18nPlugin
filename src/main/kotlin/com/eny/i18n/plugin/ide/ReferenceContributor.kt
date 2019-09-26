package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.Tree
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.json.psi.JsonObject
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

class I18nReference(element: PsiElement, textRange: TextRange, val i18nFullKey: FullKey) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference, CompositeKeyResolver<PsiElement> {
    private val search = JsonSearchUtil(element.project)

    private fun findProperties(): List<Tree<PsiElement>> {
        return if (i18nFullKey.ns != null) {
            search
                .findFilesByName(i18nFullKey.ns.text)
                .mapNotNull { jsonRoot ->
                    resolveCompositeKey(i18nFullKey.compositeKey, PsiTreeUtil.getChildOfType(jsonRoot, JsonObject::class.java)?.let{ fileRoot -> PsiElementTree(fileRoot) }).element
                }
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

    override fun getVariants(): Array<Any> =
        findProperties()
            .map {property ->
                LookupElementBuilder
                    .create(property.value().text)
                    .withTypeText(
                        (property.value().containingFile.parent?.name ?: "" + "/") + property.value().containingFile.name)
            }
            .toTypedArray()
}

/**
 * Provides navigation from i18n key to it's value in json
 */
class ReferenceContributor: PsiReferenceContributor() {
    private val jsUtil = JavaScriptUtil()

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    val i18nKeyLiteral = jsUtil.extractI18nKeyLiteral(element)
                    if (i18nKeyLiteral != null) {
                        return arrayOf(I18nReference(element, TextRange(1, element.textLength - 1), i18nKeyLiteral))
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            }
        )
    }
}

