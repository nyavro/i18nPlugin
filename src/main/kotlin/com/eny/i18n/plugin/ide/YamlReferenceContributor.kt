package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.KeyComposer
import com.eny.i18n.plugin.tree.PsiProperty
import com.eny.i18n.plugin.utils.searchScope
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLKeyValue

/**
 * Provides navigation from i18n key to it's value in json
 */
class YamlReferenceContributor: PsiReferenceContributor(), KeyComposer<PsiElement> {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    if (element is YAMLKeyValue) {
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
                            val keyElement = element.key
                            return if(keyElement != null) arrayOf(JsonI18nReference(element, getRange(keyElement), key)) else PsiReference.EMPTY_ARRAY
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                    return PsiReference.EMPTY_ARRAY
                }
            }
        )
    }

    private fun getRange(key: PsiElement): TextRange {
        val text = key.text
        return TextRange(if (text.startsWith("\"")) 1 else 0, key.textLength - (if (text.endsWith("\"")) 1 else 0))
    }
}