package com.eny.i18n.plugin.ide.references.translation

import com.eny.i18n.plugin.tree.KeyComposer
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

/**
 * Provides navigation from i18n key to it's value in json
 */
class JsonReferenceContributor: PsiReferenceContributor(), KeyComposer<PsiElement> {

    private val provider = TranslationToCodeReferenceProvider()

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    return if (element is JsonStringLiteral && element.isPropertyName && element.textLength > 1) {
                        provider.getReferences(element, TextRange(1, element.textLength - 1))
                    } else {
                        PsiReference.EMPTY_ARRAY
                    }
                }
            }
        )
    }
}

