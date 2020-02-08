package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.ide.quickfix.*
import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.project.Project

/**
 * Annotation helper methods
 */
class AnnotationHelper(private val holder: AnnotationHolder, private val facade: AnnotationFacade, private val project: Project) {
    private val RESOLVED_COLOR = DefaultLanguageHighlighterColors.LINE_COMMENT
    private val errorSeverity = HighlightSeverity.WARNING
    private val infoSeverity = HighlightSeverity.INFORMATION
    private val settings = Settings.getInstance(project)

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
        val generators = listOf(
            ContentGeneratorAdapter(YamlContentGenerator(), YamlPsiContentGenerator()),
            ContentGeneratorAdapter(JsonContentGenerator(), JsonPsiContentGenerator())
        )
        unresolvedPropertyAnnotation.registerFix(
            CreatePropertyQuickFix(fullKey, UserChoice(), "Create property", generators))
        unresolvedPropertyAnnotation.registerFix(
            CreatePropertyQuickFix(fullKey, AllFilesSelector(), "Create property in all localization files", generators))
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
        val fileName = fullKey.ns?.text ?: settings.defaultNs
        val folderSelector = I18NextTranslationFolderSelector(project)
        annotation.registerFix(CreateTranslationFileQuickFix(fullKey, JsonContentGenerator(), folderSelector, fileName))
        annotation.registerFix(CreateTranslationFileQuickFix(fullKey, YamlContentGenerator(), folderSelector, fileName))
    }

    /**
     * Annotates unresolved Vue key
     */
    fun annotateUnresolvedVueKey(fullKey: FullKey) {
        val annotation = holder.createAnnotation(errorSeverity, facade.unresolvedKey(fullKey, listOf()), "Missing localization")
        val fileName = "en"
        val folderSelector = Vue18nTranslationFolderSelector(project)
        annotation.registerFix(
            CreateTranslationFileQuickFix(fullKey, JsonContentGenerator(), folderSelector, fileName)
        )
        annotation.registerFix(
            CreateTranslationFileQuickFix(fullKey, YamlContentGenerator(), folderSelector, fileName)
        )
    }
}