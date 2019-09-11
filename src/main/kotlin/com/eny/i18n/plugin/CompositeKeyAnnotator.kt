package com.eny.i18n.plugin

import com.eny.i18n.plugin.utils.*
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

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
        val fullKey = value.fullKey()
        val fileName = fullKey?.fileName
        val compositeKey = fullKey?.compositeKey
        if (fileName != null && compositeKey != null) {
            val annotationHelper = AnnotationHelper(element.textRange, holder)
            val files = JsonSearchUtil(element.project).findFilesByName(fileName)
            if (files.isEmpty()) annotationHelper.annotateFileUnresolved(fileName)
            else {
                val mostResolvedReference = files
                        .flatMap { jsonFile ->
                            tryToResolvePlural(
                                resolveCompositeKey(compositeKey, jsonFile)
                            )
                        }
                        .maxBy { v -> v.path.size }!!
                when {
                    mostResolvedReference.element is JsonStringLiteral -> annotationHelper.annotateResolved(fileName)
                    mostResolvedReference.unresolved.isEmpty() && mostResolvedReference.isPlural-> annotationHelper.annotateReferenceToPlural(fullKey)
                    mostResolvedReference.unresolved.isEmpty() -> annotationHelper.annotateReferenceToJson(fullKey)
                    value.isTemplate -> annotationHelper.annotatePartiallyResolved(fullKey, mostResolvedReference.path)
                    else -> annotationHelper.annotateUnresolved(fullKey, mostResolvedReference.path)
                }
            }
        }
    }
}