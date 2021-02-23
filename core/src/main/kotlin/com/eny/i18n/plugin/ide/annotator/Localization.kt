package com.eny.i18n.plugin.ide.annotator

import com.intellij.psi.PsiElement
import javax.swing.Icon

/**
 * Localization components factory
 */
interface LocalizationFactory {

    /**
     * Content generator
     */
    fun contentGenerator(): ContentGenerator

    /**
     * Localization format-specific reference assistant
     */
    fun referenceAssistant(): TranslationReferenceAssistant<out PsiElement>

    /**
     * Element tree
     */
    fun elementTreeFactory(): (file: PsiElement) -> PsiElementTree?

    /**
     * Options
     */
    fun options(): LocalizationOptions
}

interface LocalizationOptions {
    fun icon(): Icon
}
