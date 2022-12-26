package com.eny.i18n.extensions.localization.js

import com.eny.i18n.*
import com.eny.i18n.plugin.ConfigurationProperty
import com.eny.i18n.plugin.tree.Tree
import com.intellij.lang.javascript.TypeScriptFileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement

class TsLocalization : Localization<PsiElement> {
    override fun types(): List<LocalizationFileType> = listOf()
    override fun contentGenerator(): ContentGenerator = TsContentGenerator()
    override fun referenceAssistant(): TranslationReferenceAssistant<PsiElement> = TsReferenceAssistant()
    override fun elementsTree(file: PsiElement): Tree<PsiElement> = TsLocalizationTree(file)
    override fun matches(localizationFileType: LocalizationFileType, file: VirtualFile?, fileNames: List<String>): Boolean {
        return localizationFileType.languageFileType == TypeScriptFileType.INSTANCE
    }
    override fun config(): LocalizationConfig {
        return object: LocalizationConfig {
            override fun id(): String = "ts"
            override fun props(): List<ConfigurationProperty> = listOf()
        }
    }
}
