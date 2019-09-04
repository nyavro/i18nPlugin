package com.eny.i18n.plugin

import com.eny.i18n.plugin.quickfix.CreateJsonFileQuickFix
import com.eny.i18n.plugin.quickfix.CreatePropertyQuickFix
import com.eny.i18n.plugin.utils.CompositeKeyResolver
import com.eny.i18n.plugin.utils.I18nKeyLiteral
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
        ).textAttributes = DefaultLanguageHighlighterColors.CONSTANT
    }
    fun annotateReferenceToJson(fullKey: I18nFullKey) {
        val compositeKeyStartOffset = compositeKeyStartOffset(fullKey)
        holder.createErrorAnnotation(
            TextRange (
                    compositeKeyStartOffset,
                    element.textRange.endOffset - 1
            ),
            "Reference to Json object"
        ).textAttributes = DefaultLanguageHighlighterColors.CONSTANT
    }
    fun annotateUnresolved(fullKey: I18nFullKey, resolvedPath: List<String>) {
        val compositeKeyStartOffset = compositeKeyStartOffset(fullKey)
        val unresolvedStartOffset = compositeKeyStartOffset +
                resolvedPathLength(resolvedPath)
        val unresolvedEndOffset = element.textRange.endOffset - 1
        holder.createErrorAnnotation(
            TextRange (
                    unresolvedStartOffset,
                    unresolvedEndOffset
            ),
            "Unresolved property"
        ).registerFix(CreatePropertyQuickFix(fullKey))
    }
    fun annotatePartiallyResolved(fullKey: I18nFullKey, resolvedPath: List<String>) {
        val compositeKeyStartOffset = compositeKeyStartOffset(fullKey)
        val unresolvedStartOffset = compositeKeyStartOffset +
                resolvedPathLength(resolvedPath)
        holder.createInfoAnnotation (
                TextRange (
                        compositeKeyStartOffset,
                        unresolvedStartOffset
                ),
                "Partially resolved"
        ).textAttributes = DefaultLanguageHighlighterColors.CONSTANT
    }

    private fun resolvedPathLength(resolvedPath: List<String>) =
            if (resolvedPath.isEmpty()) 0
            else resolvedPath.joinToString(I18nFullKey.CompositeKeySeparator, postfix = I18nFullKey.CompositeKeySeparator).length

    private fun compositeKeyStartOffset(fullKey: I18nFullKey): Int {
        return element.textRange.startOffset +
            (fullKey.fileName?.let { name -> name.length + I18nFullKey.FileNameSeparator.length } ?: 0) + 1
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

    private fun annotateI18nLiteral(value: I18nKeyLiteral, element: PsiElement, holder: AnnotationHolder) {
        val fullKey = I18nFullKey.parse(value.literal)
        if (fullKey?.fileName != null) {
            val annotationHelper = AnnotationHelper(element, holder)
            val files = JsonSearchUtil(element.project).findFilesByName(fullKey.fileName)
            if (files.isEmpty()) annotationHelper.annotateFileUnresolved(fullKey.fileName, "Unresolved file")
            else {
                val mostResolvedReference = files
                        .flatMap { jsonFile ->
                            tryToResolvePlural(
                                resolveCompositeKey(fullKey.compositeKey, jsonFile)
                            )
                        }
                        .maxBy { v -> v.path.size }!!
                when {
                    mostResolvedReference.element is JsonStringLiteral -> annotationHelper.annotateResolved(fullKey.fileName)
                    mostResolvedReference.unresolved.isEmpty() -> annotationHelper.annotateReferenceToJson(fullKey)
                    value.isTemplate -> annotationHelper.annotatePartiallyResolved(fullKey, mostResolvedReference.path)
                    else -> annotationHelper.annotateUnresolved(fullKey, mostResolvedReference.path)
                }
            }
        }
    }
}