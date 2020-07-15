package com.eny.i18n.plugin.ide.hint

import com.eny.i18n.plugin.language.js.JsCallContext
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.psi.PsiElement

class JsHintProvider : DocumentationProvider {

    private val callContext = JsCallContext()

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        return element?.text?.unQuote()
    }

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        if (originalElement !=null && callContext.accepts(originalElement) && element is JsonStringLiteral) {
            return element.value;
        }
        return null;
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return "hover";//generateDoc(element, originalElement)
    }
}