package com.eny.i18n.extensions.lang.js.extractors

import com.eny.i18n.plugin.parser.KeyExtractor
import com.eny.i18n.plugin.parser.RawKey
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.type
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSDestructuringElement
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Extracts i18n key from js string literal
 */
class ReactUseTranslationHookExtractor: KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean {
        return resolveHook(element)?.methodExpression?.text=="useTranslation"
    }

    override fun extract(element: PsiElement): RawKey {
        val arguments = resolveHook(element)?.arguments?.filter{it.type()=="JS:LITERAL_EXPRESSION"}?.map{it.text.unQuote()}?: listOf()
        return RawKey(listOf(KeyElement.literal(element.text.unQuote())), arguments)
    }

    private fun resolveTranslationFunctionDefinition(element: PsiElement): PsiElement? {
        return PsiTreeUtil
            .getChildOfType(
                PsiTreeUtil.getParentOfType(element, JSCallExpression::class.java),
                JSReferenceExpression::class.java
            )
            ?.reference
            ?.resolve()
    }

    private fun resolveDestructuringElement(t: PsiElement?): JSDestructuringElement? {
        return PsiTreeUtil.getParentOfType(t, JSDestructuringElement::class.java)
    }

    private fun resolveHook(literal: PsiElement): JSCallExpression? {
        return resolveTranslationFunctionDefinition(literal)
            ?.let { resolveDestructuringElement(it) }
            ?.let { it.initializer as? JSCallExpression}
    }
}