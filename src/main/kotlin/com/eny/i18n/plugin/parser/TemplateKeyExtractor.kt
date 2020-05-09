package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.FullKey
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

    override fun extract(element: PsiElement, parser: ExpressionKeyParser, settings: Settings): FullKey? =
        resolveTemplateExpression(element, parser, settings)


//     Resolves template expression
//
//     Example
//     const key = 'element';
//     const expression = `fileName:root.${key}.key.subKey`; // Gets resolved to 'fileName:root.element.key.subKey'
    private fun resolveTemplateExpression(element: PsiElement, parser: ExpressionKeyParser, settings: Settings): FullKey? {
        val transformed = element.node.getChildren(null).mapNotNull {
            val text = it.text
            if (it is JSExpression) {
                KeyElement(
                    text,
                    null,
                    KeyElementType.TEMPLATE
                )
            }
            else {
                KeyElement.literal(text)
            }
        }
        return parser.parse(
            transformed,
            true,
            settings.nsSeparator, settings.keySeparator, settings.stopCharacters,
            emptyNamespace = settings.vue
        )
    }
}