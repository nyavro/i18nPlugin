package com.eny.i18n.plugin.utils

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralValue
import com.intellij.psi.PsiReference
import com.intellij.psi.xml.XmlElementType

class JavaScriptUtil {

    private val ResolveReferenceMaxDepth = 10

    /**
     * Converts element to it's literal value, if possible
     */
    fun extractI18nKeyLiteral(element: PsiElement): String? {
        // Template expression
        if (isTemplateExpression(element)) {
            return resolveTemplateExpression(element)
        }
        // String literal
        else if (element is PsiLiteralValue && element.node.elementType != XmlElementType.XML_ATTRIBUTE_VALUE) {
            val value: Any? = element.value
            if (value is String) {
                return value
            }
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
    private fun resolveTemplateExpression(element: PsiElement): String? {
        val transformed = element.node.getChildren(null).map {
            item ->
            if (item.elementType.toString().contains("REFERENCE"))
                resolveStringLiteralReference(item, setOf(), ResolveReferenceMaxDepth)
            else item.text
        }
        return if (transformed.contains(null)) null
        else transformed.joinToString("").filterDollarBracesWrappers()
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

    /**
     * Removes symbols `${}` from string
     */
    private fun String.filterDollarBracesWrappers(): String {
        val toRemove = setOf('$', '{', '}', '`')
        return this.filter {ch -> ch !in toRemove}
    }
}