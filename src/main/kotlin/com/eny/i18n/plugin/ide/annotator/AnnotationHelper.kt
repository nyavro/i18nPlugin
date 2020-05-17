package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.ide.quickfix.*
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.tree.PropertyReference
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.lexer.Literal
import com.eny.i18n.plugin.utils.PluginBundle
import com.eny.i18n.plugin.utils.RangesCalculator
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

/**
 * Annotation helper methods
 */
class AnnotationHelper(private val holder: AnnotationHolder, private val rangesCalculator: RangesCalculator, private val project: Project) {
    private val RESOLVED_COLOR = DefaultLanguageHighlighterColors.LINE_COMMENT
    private val errorSeverity = HighlightSeverity.WARNING
    private val infoSeverity = HighlightSeverity.INFORMATION

    /**
     * Annotates resolved translation key
     */
    fun annotateResolved(fullKey: FullKey) {
        holder.createAnnotation(infoSeverity, rangesCalculator.compositeKeyFullBounds(fullKey), null).textAttributes = RESOLVED_COLOR
    }

    /**
     * Annotates reference to object, not a leaf key in json/yaml
     */
    fun annotateReferenceToObject(fullKey: FullKey) {
        holder.createAnnotation(errorSeverity, rangesCalculator.compositeKeyFullBounds(fullKey), PluginBundle.getMessage("annotator.object.reference"))
    }

    /**
     * Annotates unresolved namespace
     */
    fun unresolvedNs(fullKey: FullKey, ns: Literal) {
        val annotation = holder.createAnnotation(
            errorSeverity,
            rangesCalculator.unresolvedNs(fullKey),
            PluginBundle.getMessage("annotator.unresolved.ns")
        )
        val fileName = ns.text
        val folderSelector = I18NextTranslationFolderSelector(project)
        Settings.getInstance(project).mainFactory().contentGenerators().forEach {
            annotation.registerFix(CreateTranslationFileQuickFix(fullKey, it, folderSelector, fileName))
        }
    }

    /**
     * Annotates unresolved default namespace
     */
    fun unresolvedDefaultNs(fullKey: FullKey) {
        holder.createAnnotation(
            errorSeverity,
            rangesCalculator.compositeKeyFullBounds(fullKey),
            PluginBundle.getMessage("annotator.missing.default.ns")
        )
    }

    /**
     * Annotates unresolved composite key
     */
    fun unresolvedKey(fullKey: FullKey, mostResolvedReference: PropertyReference<PsiElement>) {
        val unresolvedPropertyAnnotation = holder.createAnnotation(
            errorSeverity,
            rangesCalculator.unresolvedKey(fullKey, mostResolvedReference.path),
            PluginBundle.getMessage("annotator.unresolved.key"))
        val generators = Settings.getInstance(project).mainFactory().contentGenerators()
        unresolvedPropertyAnnotation.registerFix(
            CreateKeyQuickFix(fullKey, UserChoice(), PluginBundle.getMessage("quickfix.create.key"), generators))
        unresolvedPropertyAnnotation.registerFix(
            CreateKeyQuickFix(fullKey, AllSourcesSelector(), PluginBundle.getMessage("quickfix.create.key.in.files"), generators))
    }
}