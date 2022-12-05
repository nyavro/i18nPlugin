package com.eny.i18n.plugin.ide.references.translation

import com.eny.i18n.*
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

/**
 * Provides navigation from i18n key to it's value in translation
 */
abstract class TranslationToCodeReferenceContributor<T : PsiElement>(private val translationToCode: TranslationReferenceAssistant<T>): PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            translationToCode.pattern(),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> =
                    @Suppress("UNCHECKED_CAST")
                    translationToCode.references(element as T).toTypedArray()
            }
        )
    }
}

