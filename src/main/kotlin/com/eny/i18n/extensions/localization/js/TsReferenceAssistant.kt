package com.eny.i18n.extensions.localization.js

import com.eny.i18n.SOURCE_ROOT
import com.eny.i18n.TranslationReferenceAssistant
import com.eny.i18n.extensions.lang.php.PsiUserDataPatterns
import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReferenceProvider
import com.intellij.lang.javascript.patterns.JSPatterns
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSProperty
import com.intellij.openapi.components.service
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parents

class TsReferenceAssistant : TranslationReferenceAssistant<PsiElement> {

    override fun pattern(): ElementPattern<out PsiElement> {
        return PlatformPatterns.psiElement().inFile(PsiUserDataPatterns.withUserData(SOURCE_ROOT, true)).and(JSPatterns.jsProperty())
    }

    override fun references(element: PsiElement): List<PsiReference> {
        return element.project.service<TranslationToCodeReferenceProvider>().getReferences(element, TextRange(1, element.textLength - 1), parents(element))
    }

    private fun parents(element: PsiElement): List<String> {
        val elements = element.parents(true).toList()
        val parents = elements.mapNotNull {
            when {
                it is JSProperty -> it.name
                it is JSFile -> it.name
                else -> null
            }
        }.reversed()
        return parents
    }
}
