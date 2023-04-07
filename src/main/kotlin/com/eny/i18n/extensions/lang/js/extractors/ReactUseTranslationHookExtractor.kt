package com.eny.i18n.extensions.lang.js.extractors

import com.eny.i18n.plugin.parser.KeyExtractor
import com.eny.i18n.plugin.parser.RawKey
import com.eny.i18n.plugin.utils.*
import com.intellij.lang.javascript.psi.*
import com.intellij.lang.javascript.psi.impl.JSPropertyImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
 * Extracts i18n key from js string literal
 */
class ReactUseTranslationHookExtractor: KeyExtractor {

    override fun canExtract(element: PsiElement): Boolean {
        return isKeyPrefix(element) || resolveHook(element)?.methodExpression?.text=="useTranslation"
    }

    override fun extract(element: PsiElement): RawKey {
        return if (isKeyPrefix(element)) createRawKey(
            element,
            getHookAsParent(element),
            false
        ) else createRawKey(
            element,
            resolveHook(element),
            true
        )
    }

    private fun getHookAsParent(element: PsiElement): JSCallExpression? {
        return PsiTreeUtil.findFirstParent(element){it is JSCallExpression} as? JSCallExpression
    }

    private fun createRawKey(element: PsiElement, hook: JSCallExpression?, passKeyPrefix: Boolean): RawKey {
        return RawKey(
            listOf(KeyElement.literal(element.text.unQuote())),
            hook?.arguments?.safeGet(0)?.let {getAsList(it)} ?: emptyList(),
            if(passKeyPrefix)
                (hook?.arguments?.safeGet(1) as? JSObjectLiteralExpression)
                    ?.findProperty("keyPrefix")
                    ?.value
                    ?.text
                    ?.unQuote()
                    ?.let { KeyElement.literal(it) }
                    .nullableToList()
            else emptyList(),
            !passKeyPrefix
        )
    }

    private fun getAsList(expression: JSExpression): List<String> =
        if(expression is JSArrayLiteralExpression) expression.expressions.map {it.text.unQuote()}
        else listOf(expression.text.unQuote())

    private fun isKeyPrefix(element: PsiElement): Boolean {
        return (element.type() == "JS:STRING_LITERAL" || element.type() == "JS:LITERAL_EXPRESSION")
            && (PsiTreeUtil.findFirstParent(element){it is JSPropertyImpl } as? JSPropertyImpl)?.name == "keyPrefix"
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
