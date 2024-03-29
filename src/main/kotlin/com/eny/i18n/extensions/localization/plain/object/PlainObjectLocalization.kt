package com.eny.i18n.extensions.localization.plain.`object`

import com.eny.i18n.ContentGenerator
import com.eny.i18n.Localization
import com.eny.i18n.LocalizationConfig
import com.eny.i18n.LocalizationFileType
import com.eny.i18n.plugin.ConfigurationProperty
import com.eny.i18n.*
import com.eny.i18n.plugin.tree.Tree
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement

class PlainObjectLocalization : Localization<PsiElement> {
    override fun types(): List<LocalizationFileType> =
        listOf(FileTypeManager
            .getInstance()
            .getStdFileType("Locale"))
            .filter {it != PlainTextFileType.INSTANCE}
            .map {LocalizationFileType(it, listOf("pot"))}
    override fun contentGenerator(): ContentGenerator = PlainObjectContentGenerator()
    override fun referenceAssistant(): TranslationReferenceAssistant<PsiElement> = PlainObjectReferenceAssistant()
    override fun elementsTree(file: PsiElement): Tree<PsiElement> = PlainObjectTextTree(file)
    override fun matches(localizationFileType: LocalizationFileType, file: VirtualFile?, fileNames: List<String>): Boolean {
        return file?.parent?.name == "LC_MESSAGES"
    }
    override fun config(): LocalizationConfig {
        return object: LocalizationConfig {
            override fun id(): String = "plainObject"
            override fun props(): List<ConfigurationProperty> = listOf()
        }
    }
}