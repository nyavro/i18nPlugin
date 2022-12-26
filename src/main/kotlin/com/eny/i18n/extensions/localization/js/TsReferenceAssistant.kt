package com.eny.i18n.extensions.localization.js

import com.eny.i18n.TranslationReferenceAssistant
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference

class TsReferenceAssistant : TranslationReferenceAssistant<PsiElement> {
    override fun pattern(): ElementPattern<out PsiElement> {
        TODO("Not yet implemented")
    }

    override fun references(element: PsiElement): List<PsiReference> {
        TODO("Not yet implemented")
    }

}
