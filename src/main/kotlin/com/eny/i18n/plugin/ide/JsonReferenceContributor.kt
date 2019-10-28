package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.KeyComposer
import com.eny.i18n.plugin.tree.PsiProperty
import com.eny.i18n.plugin.utils.searchScope
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.UsageSearchContext
import com.intellij.util.ProcessingContext

class JsonI18nReference(element: PsiElement, textRange: TextRange, val composedKey: String) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    fun processSearchEntry(list: MutableList<PsiElement>) = {
//        val stopTypes = listOf(
//            "FILE", "JS:CALL_EXPRESSION", "JS:EXPRESSION_STATEMENT", "JS:END_OF_LINE_COMMENT", "JS:ARGUMENT_LIST",
//            "BLOCK_STATEMENT", "JS:TYPESCRIPT_FUNCTION", "JS:TYPESCRIPT_CLASS"
//        )
        entry: PsiElement, offset:Int ->
            if (listOf("JS:STRING_LITERAL", "JS_STRING_TEMPLATE_PART").contains(entry.node.elementType.toString())) {
                list.add(entry)
            } else {
            }
            true
    }

    fun findRefs(): List<PsiElement> {
        val project = element.project
        val settings = Settings.getInstance(project)
        val searchScope = settings.searchScope(project)
        val list = mutableListOf<PsiElement>()
        PsiSearchHelper.getInstance(project).processElementsWithWord(
                processSearchEntry(list), searchScope, composedKey, UsageSearchContext.IN_CODE, true
        )
        return list
    }

    override fun resolve(): PsiElement? {
        val res = multiResolve(false)
        return if (res.size == 1) {
            res.firstOrNull()?.element
        }
        else null
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val findRefs = findRefs()
        return findRefs
            .map {property -> PsiElementResolveResult(property)}
            .toTypedArray()
    }

    override fun getVariants(): Array<Any> {
        val findRefs = findRefs()
        return findRefs
                .map { property ->
                    LookupElementBuilder
                            .create(property.text)
                            .withTypeText(property.containingFile.name + " " + property.text)
                }
                .toTypedArray()
    }
}

/**
 * Provides navigation from i18n key to it's value in json
 */
class JsonReferenceContributor: PsiReferenceContributor(), KeyComposer<PsiElement> {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    if (element is JsonStringLiteral && element.isPropertyName) {
                        val project = element.project
                        val settings = Settings.getInstance(project)
                        val key = composeKey(PsiProperty(element), settings.nsSeparator, settings.keySeparator)
                        if (PsiSearchHelper.SearchCostResult.FEW_OCCURRENCES==
                                PsiSearchHelper.getInstance(project).isCheapEnoughToSearch(key, settings.searchScope(project), null, null)) {
                            return arrayOf(JsonI18nReference(element, TextRange(1, element.textLength - 1), key))
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            }
        )
    }
}

