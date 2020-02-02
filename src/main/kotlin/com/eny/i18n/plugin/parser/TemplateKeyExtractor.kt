package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.utils.ExpressionKeyParser
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.KeyElementType
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralValue
import com.intellij.psi.PsiReference

/**
 * Extracts i18n key from JS string template expression
 */
class TemplateKeyExtractor : KeyExtractor {

    private val ResolveReferenceMaxDepth = 10
    /**
     * Checks if element is template expression, i.e. `literal ${reference} etc`
     */
    private fun isTemplateExpression(element: PsiElement):Boolean = element.type() == "JS:STRING_TEMPLATE_EXPRESSION"

    override fun canExtract(element: PsiElement): Boolean = isTemplateExpression(element)

    override fun extract(element: PsiElement, parser: ExpressionKeyParser, normalizer: KeyNormalizer, settings: Settings): FullKey? =
        resolveTemplateExpression(element, parser, normalizer, settings)

    /**
     * Resolves template expression
     *
     * Example
     * const key = 'element';
     * const expression = `fileName:root.${key}.key.subKey`; // Gets resolved to 'fileName:root.element.key.subKey'
     */
    private fun resolveTemplateExpression(element: PsiElement, parser: ExpressionKeyParser, normalizer: KeyNormalizer, settings: Settings): FullKey? {
        val transformed = element.node.getChildren(null).mapNotNull {
            item ->
            if (item.elementType.toString().contains("REFERENCE")) {
                KeyElement(
                    item.text,
                    resolveStringLiteralReference(item, setOf(), ResolveReferenceMaxDepth),
                    KeyElementType.TEMPLATE
                )
            } else KeyElement.literal(item.text)
        }
        return parser.parse(normalizer.normalize(transformed), true, settings.nsSeparator, settings.keySeparator, settings.stopCharacters)
    }

    /**
     * Resolves reference to it's String value, if possible, or null
     */
    private fun resolveStringLiteralReference(item: ASTNode, chain: Set<ASTNode>, maxDepth: Int): String? {
        if (item in chain || chain.size >= maxDepth) {
            return null
        }
        val resolved = item.psi?.reference?.resolve()?.lastChild
        if (resolved is PsiLiteralValue) {
            val v = resolved.value
            if (v is String)
                return v
        }
        else if (resolved is PsiReference) {
            return resolveStringLiteralReference(resolved.node, chain + item, maxDepth)
        }
        return null
    }
}