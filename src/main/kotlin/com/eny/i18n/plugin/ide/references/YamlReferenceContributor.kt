package com.eny.i18n.plugin.ide.references

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLKeyValue

/**
 * Provides navigation from i18n key to it's value in json
 */
class YamlReferenceContributor: PsiReferenceContributor() {

    private val provider = TranslationToCodeReferenceProvider()

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            object : PsiReferenceProvider() {

                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> =
                    when (element) {
                        is YAMLKeyValue -> provider.getReferences(element, getRange(element.key!!))
                        else -> PsiReference.EMPTY_ARRAY
                    }

                private fun getRange(key: PsiElement): TextRange {
                    val text = key.text
                    return TextRange(if (text.startsWith("\"")) 1 else 0, text.length - (if (text.endsWith("\"")) 1 else 0))
                }
            }
        )
    }
}