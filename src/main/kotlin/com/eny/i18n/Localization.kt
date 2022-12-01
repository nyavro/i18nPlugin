package com.eny.i18n

import com.eny.i18n.plugin.factory.TranslationReferenceAssistant
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.tree.Tree
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.PsiElement

data class LocalizationFileType(val languageFileType: LanguageFileType, val auxExtensions: List<String> = listOf())

interface Localization <T: PsiElement> {
    fun types(): List<LocalizationFileType>
    fun contentGenerator(): ContentGenerator
    fun referenceAssistant(): TranslationReferenceAssistant<T>
    fun elementsTree(file: PsiElement): Tree<PsiElement>?
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