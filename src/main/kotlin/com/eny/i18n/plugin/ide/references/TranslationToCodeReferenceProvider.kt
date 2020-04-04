package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.KeyComposer
import com.eny.i18n.plugin.tree.PsiProperty
import com.eny.i18n.plugin.utils.searchScope
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.UsageSearchContext

internal class TranslationToCodeReferenceProvider : KeyComposer<PsiElement> {

    /**
     * @param element PsiElement to get references of.
     * @param textRange TextRange to highlight
     */
    fun getReferences(element: PsiElement, textRange: TextRange): Array<PsiReference> {
        val project = element.project
        val settings = Settings.getInstance(project)
        val key = composeKey(
                PsiProperty.create(element),
                settings.nsSeparator,
                settings.keySeparator,
                settings.pluralSeparator,
                settings.defaultNs,
                settings.vue && element.containingFile.parent?.name == settings.vueDirectory
        )
        if (PsiSearchHelper.SearchCostResult.FEW_OCCURRENCES ==
                PsiSearchHelper.getInstance(project).isCheapEnoughToSearch(key, settings.searchScope(project), null, null)) {
            return arrayOf(TranslationToCodeReference(element, textRange, key))
        }
        return PsiReference.EMPTY_ARRAY
    }
}

/**
 * Accumulates references
 */
class ReferencesAccumulator(private val key: String) {

    private val res = mutableListOf<PsiElement>()

    /**
     * Processing function for PsiSearchHelper
     */
    fun process() = {
        entry: PsiElement, offset:Int ->
        val typeName = entry.node.elementType.toString()
        if (entry.text.unQuote().startsWith(key)) {
            if (listOf("JS:STRING_LITERAL", "quoted string").any { item -> typeName.contains(item) }) {
                res.add(entry)
            } else if (typeName == "JS:STRING_TEMPLATE_PART") {
                res.add(entry.parent)
            } else if (typeName == "JS:STRING_TEMPLATE_EXPRESSION") {
                res.add(entry)
            }
        }
        true
    }

    /**
     * Returns collected entries
     */
    fun entries(): Collection<PsiElement> = res
}

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

    override fun resolve(): PsiElement? = multiResolve(false).firstOrNull()?.element

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
            findRefs()
                    .map {property -> PsiElementResolveResult(property) }
                    .toTypedArray()
}