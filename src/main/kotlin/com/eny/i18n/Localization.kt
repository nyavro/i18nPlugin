package com.eny.i18n

import com.eny.i18n.plugin.ConfigurationProperty
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.tree.Tree
import com.intellij.icons.AllIcons
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import javax.swing.Icon

data class LocalizationFileType(val languageFileType: FileType, val auxExtensions: List<String> = listOf()) {
    fun extensions(): List<String> = auxExtensions + languageFileType.defaultExtension
}

interface LocalizationConfig {
    fun id(): String
    fun props(): List<ConfigurationProperty>
}

interface Localization <T: PsiElement> {
    fun types(): List<LocalizationFileType>
    fun contentGenerator(): ContentGenerator
    fun referenceAssistant(): TranslationReferenceAssistant<T>
    fun elementsTree(file: PsiElement): Tree<PsiElement>?
    fun matches(localizationFileType:LocalizationFileType, file: VirtualFile?, fileNames: List<String>): Boolean
    fun icon(): Icon = AllIcons.FileTypes.Unknown
    fun config(): LocalizationConfig
}

val SOURCE_ROOT = Key<Boolean>("i18n#sourceRoot")

interface TranslationReferenceAssistant<T: PsiElement>{
    /**
     * Defines translation reference pattern
     */
    fun pattern(): ElementPattern<out T>

    /**
     * Calculates element's references
     */
    fun references(element: T): List<PsiReference>
}

/**
 * Localization file content generator
 */
interface ContentGenerator {

    /**
     * Generates content by given i18n key
     */
    fun generateContent(fullKey: FullKey, value: String?): String = generateContent(fullKey.compositeKey, value ?: "TODO-${fullKey.source}")

    /**
     * Generates content by given composite key
     */

    fun generateContent(compositeKey: List<Literal>, value: String): String

    /**
     * Returns localization type
     */
    fun getType(): FileType

    /**
     * Returns language
     */
    fun getLanguage(): Language

    /**
     * Returns description
     */
    fun getDescription(): String

    /**
     * Checks if current generator is suitable for content generation in given node
     *
     * @param {PsiElement} element to check suitability
     */
    fun isSuitable(element: PsiElement): Boolean

    /**
     * Generates translation entry
     *
     * @param {PsiElement} parent element in translation file
     * @param {String} key Translation key
     * @param {String} value Translation value
     */
    fun generateTranslationEntry(item: PsiElement, key: String, value: String)

    /**
     * Generates content and psi element
     */
    fun generate(element: PsiElement, fullKey: FullKey, unresolved: List<Literal>, translationValue: String?)
}
