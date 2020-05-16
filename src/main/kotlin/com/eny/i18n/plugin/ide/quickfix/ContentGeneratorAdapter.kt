package com.eny.i18n.plugin.ide.quickfix

import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.intellij.psi.PsiElement

/**
 * Adapter to tie content generator and psi content generator
 */
class ContentGeneratorAdapter(private val contentGenerator: ContentGenerator, private val psiContentGenerator: PsiContentGenerator) {

    /**
     * Checks if given element is suitable for this adapter
     */
    fun isSuitable(element: PsiElement) = psiContentGenerator.isSuitable(element)

    /**
     * Generates content and psi element
     */
    fun generate(element: PsiElement, fullKey: FullKey, unresolved: List<Literal>, translationValue: String?) =
        psiContentGenerator.generateTranslationEntry(
            element,
            unresolved.first().text,
            contentGenerator.generateContent(unresolved.drop(1), translationValue ?: fullKey.source)
        )
}