package com.eny.i18n.plugin.utils

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
    fun annotateReferenceToJson(fullKey: FullKey) {
        holder.createErrorAnnotation(
            TextRange(
                compositeKeyStartOffset(fullKey),
                textRange.endOffset - 1
            ),
            "Reference to Json object"
        )
    }
    fun annotateReferenceToPlural(fullKey: FullKey) {
        holder.createInfoAnnotation(
            TextRange(
                compositeKeyStartOffset(fullKey),
                textRange.endOffset - 1
            ),"Reference to plural value"
        ).textAttributes = RESOLVED_COLOR
    }
    fun annotateUnresolved(fullKey: FullKey, resolvedPath: List<Literal>) {
        val compositeKeyStartOffset = compositeKeyStartOffset(fullKey)
        val unresolvedStartOffset = compositeKeyStartOffset + 1 +
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
    fun annotatePartiallyResolved(fullKey: FullKey, resolvedPath: List<Literal>) {
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

    private fun resolvedPathLength(resolvedPath: List<Literal>) = tokensLength(resolvedPath)

    private fun compositeKeyStartOffset(fullKey: FullKey): Int {
        return textRange.startOffset +
            (fullKey.ns?.let { name -> name.length + 1 } ?: 0) + 1
    }
}