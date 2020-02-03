package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.KeyComposer
import com.eny.i18n.plugin.tree.PsiProperty
import com.eny.i18n.plugin.utils.searchScope
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.UsageSearchContext
import com.intellij.util.ProcessingContext

/**
 * Accumulates references
 */
class ReferencesAccumulator {

    private val res = mutableListOf<PsiElement>()
    private var toDrop: PsiElement? = null

    /**
     * Processing function for PsiSearchHelper
     */
    fun process() = {
        entry: PsiElement, offset:Int ->
            val typeName = entry.node.elementType.toString()
            if (typeName == "JS:STRING_LITERAL") {
                process(entry)
            } else if (typeName == "JS:STRING_TEMPLATE_PART") {
                process(entry.parent)
            } else if (typeName == "JS:STRING_TEMPLATE_EXPRESSION"){
                process(entry)
            }
            true
    }

    /**
     * Returns collected entries
     */
    fun entries(): Collection<PsiElement> = res

    private fun getParentsOfType(element: PsiElement, types: Set<String>) {
        var cur = element.parent
        while(!(cur is PsiFile)) {
            val typeName = cur.node.elementType.toString()
            if (types.contains(typeName)) {
                toDrop = cur
            }
            cur = cur.parent
        }
    }

    private fun process(entry: PsiElement) {
        if (entry != toDrop) {
            getParentsOfType(entry, setOf("JS:STRING_LITERAL", "JS:STRING_TEMPLATE_PART", "JS:STRING_TEMPLATE_EXPRESSION"))
            res.add(entry)
        } else {
            toDrop = null
        }
    }
}

/**
 * Reference to key usage for json translation file
 */
class JsonI18nReference(element: PsiElement, textRange: TextRange, val composedKey: String) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    /**
     * Finds usages of json translation
     */
    fun findRefs(): Collection<PsiElement> {
        val project = element.project
        val referencesAccumulator = ReferencesAccumulator()
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
            .map {property -> PsiElementResolveResult(property)}
            .toTypedArray()
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
                        val key = composeKey(
                            PsiProperty.create(element),
                            settings.nsSeparator,
                            settings.keySeparator,
                            settings.pluralSeparator,
                            settings.defaultNs,
                            settings.vue && element.containingFile.parent?.name == settings.vueDirectory
                        )
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

