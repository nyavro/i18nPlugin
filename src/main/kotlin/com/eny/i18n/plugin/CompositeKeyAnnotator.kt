package com.eny.i18n.plugin

import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralValue
import com.intellij.psi.xml.XmlElementType

/**
 * Annotation helper methods
 */
class AnnotationHelper(val element: PsiElement, val holder: AnnotationHolder) {

    fun annotateResolved(fileName: String) {
        holder.createInfoAnnotation(
            TextRange(
                element.textRange.startOffset + fileName.length + 1,
                element.textRange.endOffset
            ), null
        ).textAttributes = DefaultLanguageHighlighterColors.LINE_COMMENT
    }
    fun annotateUnresolved(fileName: String?, compositeKey: List<String>, text: String) {
        holder.createErrorAnnotation(
            TextRange (
                element.textRange.startOffset + (fileName?.let{name -> name.length+2} ?: 1),
                element.textRange.endOffset - 1
            ), text
        ).registerFix(CreateI18nPropertyQuickFix (fileName, compositeKey))
    }
    fun annotateFileUnresolved(fileName: String, text: String) {
        holder.createErrorAnnotation(
            TextRange (
                element.textRange.startOffset + 1,
                element.textRange.startOffset + fileName.length + 1
            ), text
        ).registerFix(Create18nJsonFileQuickFix (fileName))
    }
}

/**
 * Annotator for i18n keys
 */
class CompositeKeyAnnotator : Annotator, CompositeKeyResolver {
    private val isAnnotated = Key<Boolean>("annotated")
    private val jsUtil = JavaScriptUtil()

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val i18nKeyLiteral = extractI18nKeyLiteral(element)
        if (i18nKeyLiteral != null) {
            pathToParentTemplateExpression(element)?.forEach{
                item -> item.manager.putUserData(isAnnotated, true)
            }
            annotateI18nLiteral(i18nKeyLiteral, element, holder)
        }
    }

    /**
     * Checks if element is template expression, i.e. `literal ${reference} etc`
     */
    private fun isTemplateExpression(element: PsiElement):Boolean = element.node?.elementType.toString() == "JS:STRING_TEMPLATE_EXPRESSION"

    /**
     * Collects parent elements until reaching outer template expression
     */
    private fun pathToParentTemplateExpression(element: PsiElement?): List<PsiElement>? =
        when {
            element == null -> listOf()
            isTemplateExpression(element) -> listOf(element)
            else -> pathToParentTemplateExpression(element.parent)?.plus(element)
        }

    /**
     * Converts element to it's literal value, if possible
     */
    private fun extractI18nKeyLiteral(element: PsiElement): String? {
        // Template expression, not annotated
        if (isTemplateExpression(element) && !isAnnotated(element)) {
            return jsUtil.resolveTemplateExpression(element)
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
     * Checks if element is already annotated
     */
    private fun isAnnotated(element: PsiElement) = element.manager.getUserData(isAnnotated) ?: false

    private fun annotateI18nLiteral(value: String, element: PsiElement, holder: AnnotationHolder) {
        val fullKey = I18nFullKey.parse(value)
        if (fullKey?.fileName != null) {
            val annotationHelper = AnnotationHelper(element, holder)
            val files = JsonSearchUtil(element.project).findFilesByName(fullKey.fileName)
            if (files.isEmpty()) annotationHelper.annotateFileUnresolved(fullKey.fileName, "Unresolved file")
            else {
                val mostResolvedReference = files
                        .map { jsonFile -> resolveCompositeKey(fullKey.compositeKey, jsonFile) }
                        .sortedByDescending { v -> v.path.size }
                        .first()
                if (mostResolvedReference.element != null && mostResolvedReference.element is JsonStringLiteral) {
                    annotationHelper.annotateResolved(fullKey.fileName)
                } else annotationHelper.annotateUnresolved(fullKey.fileName, mostResolvedReference.path, "Unresolved property")
            }
        }
    }
}