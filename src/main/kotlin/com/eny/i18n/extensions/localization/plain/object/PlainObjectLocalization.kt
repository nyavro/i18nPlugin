package com.eny.i18n.extensions.localization.plain.`object`

import com.eny.i18n.ContentGenerator
import com.eny.i18n.Localization
import com.eny.i18n.LocalizationFileType
import com.eny.i18n.plugin.factory.TranslationReferenceAssistant
import com.eny.i18n.plugin.tree.Tree
import com.intellij.psi.PsiElement
import com.jetbrains.localization.LocaleFileType
import com.jetbrains.localization.psi.impl.LocalePsiElement

class PlainObjectLocalization : Localization<PsiElement> {
    override fun types(): List<LocalizationFileType> = listOf(LocaleFileType.INSTANCE).map { LocalizationFileType(it) }
    override fun contentGenerator(): ContentGenerator = PlainObjectContentGenerator()
    override fun referenceAssistant(): TranslationReferenceAssistant<PsiElement> = PlainObjectReferenceAssistant()
    override fun elementsTree(root: PsiElement): Tree<PsiElement>? = PlainObjectTextTree(root)
}