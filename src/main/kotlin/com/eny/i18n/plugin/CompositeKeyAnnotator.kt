package com.eny.i18n.plugin

import com.eny.i18n.plugin.quickfix.CreateJsonFileQuickFix
import com.eny.i18n.plugin.quickfix.CreatePropertyQuickFix
import com.eny.i18n.plugin.utils.CompositeKeyResolver
import com.eny.i18n.plugin.utils.JavaScriptUtil
import com.eny.i18n.plugin.utils.JsonSearchUtil
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
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
        ).registerFix(CreatePropertyQuickFix(fileName, compositeKey))
    }
    fun annotateFileUnresolved(fileName: String, text: String) {
        holder.createErrorAnnotation(
            TextRange (
                element.textRange.startOffset + 1,
                element.textRange.startOffset + fileName.length + 1
            ), text
        ).registerFix(CreateJsonFileQuickFix(fileName))
    }
}

/**
 * Annotator for i18n keys
 */
class CompositeKeyAnnotator : Annotator, CompositeKeyResolver {
    private val jsUtil = JavaScriptUtil()

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val i18nKeyLiteral = extractI18nKeyLiteral(element)
        if (i18nKeyLiteral != null) {
            annotateI18nLiteral(i18nKeyLiteral, element, holder)
        }
    }

    /**
     * Checks if element is template expression, i.e. `literal ${reference} etc`
     */
    private fun isTemplateExpression(element: PsiElement):Boolean = element.node?.elementType.toString() == "JS:STRING_TEMPLATE_EXPRESSION"

    /**
     * Converts element to it's literal value, if possible
     */
    private fun extractI18nKeyLiteral(element: PsiElement): String? {
        // Template expression
        if (isTemplateExpression(element)) {
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