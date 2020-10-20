package com.eny.i18n.plugin.ide.hint

import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlText

/**
 * Shows key's translation as a hint.
 * Works for very simple case only - when there is exactly one translation corresponding to key.
 */
class HintProvider : DocumentationProvider {
    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        return if (element is XmlText) null else element?.text?.unQuote()
    }
}