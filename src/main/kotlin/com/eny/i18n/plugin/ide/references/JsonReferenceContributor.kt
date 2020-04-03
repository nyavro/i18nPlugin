package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.KeyComposer
import com.eny.i18n.plugin.tree.PsiProperty
import com.eny.i18n.plugin.utils.searchScope
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.util.ProcessingContext

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
                            return arrayOf(TranslationToCodeReference(element, TextRange(1, element.textLength - 1), key))
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            }
        )
    }
}

