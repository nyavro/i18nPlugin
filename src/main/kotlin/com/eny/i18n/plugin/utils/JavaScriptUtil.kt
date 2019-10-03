package com.eny.i18n.plugin.utils

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralValue
import com.intellij.psi.PsiReference
import com.intellij.psi.xml.XmlElementType

class JavaScriptUtil {

    private val ResolveReferenceMaxDepth = 10

    private val parser: ExpressionKeyParser = ExpressionKeyParser()

    /**
     * Converts element to it's literal value, if possible
     */
    fun extractI18nKeyLiteral(element: PsiElement): FullKey? {
        // Template expression
        if (isTemplateExpression(element)) {
            return resolveTemplateExpression(element)
        }
        // String literal
        else if (element is PsiLiteralValue && element.node.elementType != XmlElementType.XML_ATTRIBUTE_VALUE) {
            val value: Any? = element.value
            if (value is String) {
                return parser.parse(listOf(KeyElement.literal(value)))
            }
        }
        else if (element.node?.elementType.toString() == "JS:STRING_LITERAL") {
            val value = element.text.unQuote()
            return parser.parse(listOf(KeyElement.literal(value)))
        }
        return null
    }

    /**
     * Checks if element is template expression, i.e. `literal ${reference} etc`
     */
    private fun isTemplateExpression(element: PsiElement):Boolean = element.node?.elementType.toString() == "JS:STRING_TEMPLATE_EXPRESSION"

    /**
     * Resolves template expression
     *
     * Example
     * const key = 'element';
     * const expression = `fileName:root.${key}.key.subKey`; // Gets resolved to 'fileName:root.element.key.subKey'
     */
    private fun resolveTemplateExpression(element: PsiElement): FullKey? {
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
        val elements = parser.reduce(transformed)
        return parser.parse(elements, true)
    }

    /**
     * Resolves reference to it's String value, if possible, or null
     */
    private fun resolveStringLiteralReference(item: ASTNode, chain: Set<ASTNode>, maxDepth: Int): String? {
        if (item in chain || chain.size >= maxDepth) {
            return null
        }
        val resolved = item.getPsi()?.reference?.resolve()?.lastChild
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