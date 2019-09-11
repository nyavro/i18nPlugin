package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.I18nFullKey
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange

/**
 * Annotation helper methods
 */
class AnnotationHelper(val textRange: TextRange, val holder: AnnotationHolder) {
    private val RESOLVED_COLOR = DefaultLanguageHighlighterColors.LINE_COMMENT
    fun annotateResolved(fileName: String) {
        holder.createInfoAnnotation(
            TextRange(
                textRange.startOffset + fileName.length + 1,
                textRange.endOffset - 1
            ), null
        ).textAttributes = RESOLVED_COLOR
    }
    fun annotateReferenceToJson(fullKey: I18nFullKey) {
        holder.createErrorAnnotation(
            TextRange(
                compositeKeyStartOffset(fullKey),
                textRange.endOffset - 1
            ),
            "Reference to Json object"
        )
    }
    fun annotateReferenceToPlural(fullKey: I18nFullKey) {
        holder.createInfoAnnotation(
            TextRange(
                compositeKeyStartOffset(fullKey),
                textRange.endOffset - 1
            ),"Reference to plural value"
        ).textAttributes = RESOLVED_COLOR
    }
    fun annotateUnresolved(fullKey: I18nFullKey, resolvedPath: List<KeyElement>) {
        val compositeKeyStartOffset = compositeKeyStartOffset(fullKey)
        val unresolvedStartOffset = compositeKeyStartOffset +
                resolvedPathLength(resolvedPath)
        val unresolvedEndOffset = textRange.endOffset - 1
        holder.createErrorAnnotation(
            TextRange(
                unresolvedStartOffset,
                unresolvedEndOffset
            ),
            "Unresolved property"
        )//.registerFix(CreatePropertyQuickFix(fullKey))
    }
    fun annotatePartiallyResolved(fullKey: I18nFullKey, resolvedPath: List<KeyElement>) {
        val compositeKeyStartOffset = compositeKeyStartOffset(fullKey)
        val unresolvedStartOffset = compositeKeyStartOffset +
                resolvedPathLength(resolvedPath)
        holder.createInfoAnnotation (
            TextRange(
                compositeKeyStartOffset,
                unresolvedStartOffset
            ),
                "Partially resolved"
        ).textAttributes = RESOLVED_COLOR
    }

    fun annotateFileUnresolved(fileName: String) {
        holder.createErrorAnnotation(
            TextRange(
                textRange.startOffset + 1,
                textRange.startOffset + fileName.length + 1
            ), "Unresolved file"
        )//.registerFix(CreateJsonFileQuickFix(fileName))
    }

    private fun resolvedPathLength(resolvedPath: List<KeyElement>) =
        if (resolvedPath.isEmpty()) 0
        else resolvedPath.map{item -> item.text}.joinToString(I18nFullKey.CompositeKeySeparator, postfix = I18nFullKey.CompositeKeySeparator).length

    private fun compositeKeyStartOffset(fullKey: I18nFullKey): Int {
        return textRange.startOffset +
            (fullKey.fileName?.let { name -> name.length + I18nFullKey.FileNameSeparator.length } ?: 0) + 1
    }
}