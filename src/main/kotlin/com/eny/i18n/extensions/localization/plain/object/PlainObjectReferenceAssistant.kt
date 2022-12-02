package com.eny.i18n.extensions.localization.plain.`object`

import com.eny.i18n.plugin.factory.TranslationReferenceAssistant
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.jetbrains.localization.psi.impl.LocalePsiElement

class PlainObjectReferenceAssistant : TranslationReferenceAssistant<PsiElement> {
    override fun pattern(): ElementPattern<out PsiElement> {
        TODO("Not yet implemented")
    }

    override fun references(element: PsiElement): List<PsiReference> {
        TODO("Not yet implemented")
    }

}
