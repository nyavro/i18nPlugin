package com.eny.i18n.extensions.lang.js.extractors

import com.eny.i18n.plugin.parser.KeyExtractor
import com.eny.i18n.plugin.parser.RawKey
import com.eny.i18n.plugin.parser.type
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.KeyElementType
import com.intellij.lang.javascript.psi.JSExpression
import com.intellij.psi.PsiElement

/**
 * Extracts i18n key from JS string template expression
 */
class TemplateKeyExtractor : KeyExtractor {

    private fun isTemplateExpression(element: PsiElement):Boolean = element.type() == "JS:STRING_TEMPLATE_EXPRESSION"

    override fun canExtract(element: PsiElement): Boolean = isTemplateExpression(element)

    override fun extract(element: PsiElement): RawKey =
        RawKey(
            element.node.getChildren(null).map {
                KeyElement(it.text, if (it is JSExpression) KeyElementType.TEMPLATE else KeyElementType.LITERAL)
            }
        )
}