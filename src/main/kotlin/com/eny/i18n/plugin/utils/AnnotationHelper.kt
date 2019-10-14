package com.eny.i18n.plugin.utils

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors

/**
 * Annotation helper methods
 */
class AnnotationHelper(val holder: AnnotationHolder, val facade: AnnotationFacade) {
    private val RESOLVED_COLOR = DefaultLanguageHighlighterColors.LINE_COMMENT
    fun annotateResolved(fullKey: FullKey) {
        holder.createInfoAnnotation(facade.compositeKeyFullBounds(fullKey), null).textAttributes = RESOLVED_COLOR
    }
    fun annotateReferenceToJson(fullKey: FullKey) {
        holder.createErrorAnnotation(facade.compositeKeyFullBounds(fullKey), "Reference to Json object")
    }
    fun annotateReferenceToPlural(fullKey: FullKey) {
        holder.createInfoAnnotation(facade.compositeKeyFullBounds(fullKey),"Reference to plural value").textAttributes = RESOLVED_COLOR
    }
    fun annotateUnresolved(fullKey: FullKey, resolvedPath: List<Literal>) {
        holder.createErrorAnnotation(facade.unresolvedKey(fullKey, resolvedPath), "Unresolved property")//.registerFix(CreatePropertyQuickFix(fullKey))
    }
    fun annotatePartiallyResolved(fullKey: FullKey, resolvedPath: List<Literal>) {
        holder.createInfoAnnotation(facade.unresolvedKey(fullKey, resolvedPath), "Partially resolved").textAttributes = RESOLVED_COLOR
    }
    fun annotateFileUnresolved(fullKey: FullKey) {
        holder.createErrorAnnotation(facade.unresolvedNs(fullKey), "Unresolved file")//.registerFix(CreateJsonFileQuickFix(fileName))
    }
}