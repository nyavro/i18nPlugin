package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.key.KeyElement
import com.eny.i18n.plugin.key.KeyElementType
import com.intellij.lang.javascript.psi.JSExpression
import com.intellij.psi.PsiElement

/**
 * Extracts i18n key from JS string template expression
 */
class TemplateKeyExtractor : KeyExtractor {

    private fun isTemplateExpression(element: PsiElement):Boolean = element.type() == "JS:STRING_TEMPLATE_EXPRESSION"

    override fun canExtract(element: PsiElement): Boolean = isTemplateExpression(element)

    override fun extract(element: PsiElement): Pair<List<KeyElement>, List<String>?> =
        Pair(
            element.node.getChildren(null).mapNotNull {
                KeyElement(it.text, if (it is JSExpression) KeyElementType.TEMPLATE else KeyElementType.LITERAL)
            },
            null
        )
}