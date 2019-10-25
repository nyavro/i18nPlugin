package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.tree.*
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.JavaScriptUtil
import com.eny.i18n.plugin.utils.JsonSearchUtil
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.json.psi.JsonLiteral
import com.intellij.json.psi.JsonObject
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

class JsonI18nReference(element: PsiElement, textRange: TextRange, val i18nFullKey: FullKey) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference, CompositeKeyResolver<PsiElement> {
    private val search = JsonSearchUtil(element.project)

    private fun filterMostResolved(list: List<PropertyReference<PsiElement>>): List<PropertyReference<PsiElement>> {
        val mostResolved = list.maxBy {ref -> ref.path.size}?.path?.size
        return if (mostResolved != null)
            list.filter { ref -> ref.path.size == mostResolved}
        else
            list

    }

    private fun findProperties(): List<Tree<PsiElement>> {
        return if (i18nFullKey.ns != null) {
            filterMostResolved(
                search
                    .findFilesByName(i18nFullKey.ns.text)
                    .map { jsonRoot ->
                        resolveCompositeKey(i18nFullKey.compositeKey, PsiTreeUtil.getChildOfType(jsonRoot, JsonObject::class.java)?.let{ fileRoot -> PsiElementTree(fileRoot)})
                    }
            ).mapNotNull {item -> item.element}
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
class JsonReferenceContributor: PsiReferenceContributor(), KeyComposer<PsiElement> {
    private val jsUtil = JavaScriptUtil()

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    if (element is JsonLiteral) {
                        val key = composeKey(PsiProperty(element), ":", ".")
                        println(key)
                        return PsiReference.EMPTY_ARRAY
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            }
        )
    }
}

