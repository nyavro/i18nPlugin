package com.eny.i18n.plugin.factory

import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiElement

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
     * Returns file type
     */
    fun getFileType(): FileType

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
    fun generateTranslationEntry(element: PsiElement, key: String, value: String)

    /**
     * Generates content and psi element
     */
    fun generate(element: PsiElement, fullKey: FullKey, unresolved: List<Literal>, translationValue: String?)
}