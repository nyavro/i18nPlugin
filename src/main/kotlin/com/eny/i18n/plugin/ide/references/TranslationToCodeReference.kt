package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.searchScope
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.UsageSearchContext

/**
 * Reference to key usage for json translation file
 */
class TranslationToCodeReference(element: PsiElement, textRange: TextRange, val composedKey: String) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    /**
     * Finds usages of json translation
     */
    fun findRefs(): Collection<PsiElement> {
        val project = element.project
        val referencesAccumulator = ReferencesAccumulator(composedKey)
        PsiSearchHelper.getInstance(project).processElementsWithWord(
            referencesAccumulator.process(), Settings.getInstance(project).searchScope(project), composedKey, UsageSearchContext.ANY, true
        )
        return referencesAccumulator.entries()
    }

    override fun resolve(): PsiElement? {
        val res = multiResolve(false)
        return if (res.size == 1) res.firstOrNull()?.element else null
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
        findRefs()
            .map {property -> PsiElementResolveResult(property) }
            .toTypedArray()
}