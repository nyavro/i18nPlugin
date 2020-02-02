package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.ide.quickfix.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors

/**
 * Annotation helper methods
 */
class AnnotationHelper(private val holder: AnnotationHolder, private val facade: AnnotationFacade) {
    private val RESOLVED_COLOR = DefaultLanguageHighlighterColors.LINE_COMMENT
    private val errorSeverity = HighlightSeverity.WARNING
    private val infoSeverity = HighlightSeverity.INFORMATION

    /**
     * Annotates resolved translation key
     */
    fun annotateResolved(fullKey: FullKey) {
        holder.createAnnotation(infoSeverity, facade.compositeKeyFullBounds(fullKey), null).textAttributes = RESOLVED_COLOR
    }

    /**
     * Annotates reference to object, not a leaf key in json/yaml
     */
    fun annotateReferenceToJson(fullKey: FullKey) {
        holder.createAnnotation(errorSeverity, facade.compositeKeyFullBounds(fullKey), "Reference to object")
    }

    /**
     * Annotates reference to plural value
     */
    fun annotateReferenceToPlural(fullKey: FullKey) {
        holder.createAnnotation(infoSeverity, facade.compositeKeyFullBounds(fullKey),"Reference to plural value").textAttributes = RESOLVED_COLOR
    }

    /**
     * Annotates unresolved reference
     */
    fun annotateUnresolved(fullKey: FullKey, resolvedPath: List<Literal>) {
        val unresolvedPropertyAnnotation = holder.createAnnotation(errorSeverity, facade.unresolvedKey(fullKey, resolvedPath), "Unresolved property")
        unresolvedPropertyAnnotation.registerFix(
            CreatePropertyQuickFix(fullKey, UserChoice(), "Create property"))
        unresolvedPropertyAnnotation.registerFix(
            CreatePropertyQuickFix(fullKey, AllFilesSelector(), "Create property in all localization files"))
    }

    /**
     * Annotates partially resolved reference
     */
    fun annotatePartiallyResolved(fullKey: FullKey, resolvedPath: List<Literal>) {
        holder.createAnnotation(infoSeverity, facade.unresolvedKey(fullKey, resolvedPath), "Partially resolved").textAttributes = RESOLVED_COLOR
    }

    /**
     * Annotates unresolved namespace
     */
    fun annotateFileUnresolved(fullKey: FullKey) {
        val annotation =
            if (fullKey.ns == null) holder.createAnnotation(errorSeverity, facade.unresolvedKey(fullKey, listOf()), "Missing default namespace")
            else holder.createAnnotation(errorSeverity, facade.unresolvedNs(fullKey), "Unresolved file")
        annotation.registerFix(CreateJsonFileQuickFix(fullKey))
        annotation.registerFix(CreateYamlFileQuickFix(fullKey))
    }

    /**
     * Annotates unresolved Vue key
     */
    fun annotateUnresolvedVueKey(fullKey: FullKey) {
        val annotation = holder.createAnnotation(errorSeverity, facade.unresolvedKey(fullKey, listOf()), "Missing localization")
        annotation.registerFix(CreateJsonFileQuickFix(fullKey, true))
        annotation.registerFix(CreateYamlFileQuickFix(fullKey, true))
    }
}