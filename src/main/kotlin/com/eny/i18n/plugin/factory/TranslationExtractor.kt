package com.eny.i18n.plugin.factory

import com.eny.i18n.plugin.key.FullKey
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement

/**
 * Defines translation text extraction
 */
interface TranslationExtractor {

    /**
     * Checks if it is possible to extract translation from given element
     */
    fun canExtract(element: PsiElement): Boolean

    /**
     * Checks if translation already extracted
     */
    fun isExtracted(element: PsiElement): Boolean

    /**
     * Get text of translation
     */
    fun text(element: PsiElement): String

    /**
     * Get translation textRange
     */
    fun textRange(element: PsiElement): TextRange = element.parent.textRange

    /**
     * Get template to substitute translation with
     */
    fun template(element: PsiElement): (argument: String) -> String = {"i18n.t($it)"}

    fun postProcess(editor: Editor, offset: Int) {}
}

/**
 * Folding provider interface
 */
interface FoldingProvider {
    /**
     * First step of folding - collecting list of elements where foldable elements may reside.
     */
    fun collectContainers(root: PsiElement): List<PsiElement>

    /**
     * Second step of folding - collect i18n keys inside container
     *
     * return pair of list of collected i18n key elements and offset relative to container start
     */
    fun collectLiterals(container: PsiElement): Pair<List<PsiElement>, Int>

    /**
     * Returns folding range
     */
    fun getFoldingRange(container: PsiElement, offset: Int, psiElement: PsiElement): TextRange
}

/**
 * Reference assistant
 */
interface ReferenceAssistant {

    /**
     * Pattern for reference application
     */
    fun pattern(): ElementPattern<out PsiElement>

    /**
     * Extract i18n key from element
     */
    fun extractKey(element: PsiElement): FullKey?
}

