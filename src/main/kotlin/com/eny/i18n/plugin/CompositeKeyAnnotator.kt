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
    fun annotateReferenceToJson(fullKey: I18nFullKey) {
        val compositeKeyStartOffset = element.textRange.startOffset +
            (fullKey.fileName?.let { name -> name.length + I18nFullKey.FileNameSeparator.length } ?: 0) + 1
        holder.createErrorAnnotation(
            TextRange (
                    compositeKeyStartOffset,
                    element.textRange.endOffset - 1
            ),
            "Reference to Json object"
        )
    }
    fun annotateUnresolved(fullKey: I18nFullKey, resolvedPath: List<String>) {
        val compositeKeyStartOffset = element.textRange.startOffset +
                (fullKey.fileName?.let { name -> name.length + I18nFullKey.FileNameSeparator.length } ?: 0) + 1
        val unresolvedStartOffset = compositeKeyStartOffset +
                resolvedPath.joinToString(I18nFullKey.CompositeKeySeparator, postfix = I18nFullKey.CompositeKeySeparator).length
        val unresolvedEndOffset = element.textRange.endOffset - 1
        holder.createErrorAnnotation(
            TextRange (
                    unresolvedStartOffset,
                    unresolvedEndOffset
            ),
            "Unresolved property"
        ).registerFix(CreatePropertyQuickFix(fullKey))
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
        val i18nKeyLiteral = jsUtil.extractI18nKeyLiteral(element)
        if (i18nKeyLiteral != null) {
            annotateI18nLiteral(i18nKeyLiteral, element, holder)
        }
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
                when {
                    mostResolvedReference.element is JsonStringLiteral -> annotationHelper.annotateResolved(fullKey.fileName)
                    mostResolvedReference.unresolved.isEmpty() -> annotationHelper.annotateReferenceToJson(fullKey)
                    else -> annotationHelper.annotateUnresolved(fullKey, mostResolvedReference.path)
                }
            }
        }
    }
}