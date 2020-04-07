package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.ide.quickfix.*
import com.eny.i18n.plugin.tree.PropertyReference
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.Literal
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
        annotation.registerFix(CreateTranslationFileQuickFix(fullKey, JsonContentGenerator(), folderSelector, fileName))
        annotation.registerFix(CreateTranslationFileQuickFix(fullKey, YamlContentGenerator(), folderSelector, fileName))
    }

    /**
     * Annotates unresolved namespace
     */
    fun unresolvedDefaultNs(fullKey: FullKey) {
        holder.createAnnotation(
            errorSeverity,
            rangesCalculator.compositeKeyFullBounds(fullKey),
            PluginBundle.getMessage("annotator.missing.default.ns")
        )
    }

    fun unresolvedKey(fullKey: FullKey, mostResolvedReference: PropertyReference<PsiElement>) {
        val unresolvedPropertyAnnotation = holder.createAnnotation(
            errorSeverity,
            rangesCalculator.unresolvedKey(fullKey, mostResolvedReference.path),
            PluginBundle.getMessage("annotator.unresolved.key"))
        val generators = listOf(
            ContentGeneratorAdapter(YamlContentGenerator(), YamlPsiContentGenerator()),
            ContentGeneratorAdapter(JsonContentGenerator(), JsonPsiContentGenerator())
        )
        unresolvedPropertyAnnotation.registerFix(
            CreateKeyQuickFix(fullKey, UserChoice(), PluginBundle.getMessage("quickfix.create.key"), generators))
        unresolvedPropertyAnnotation.registerFix(
            CreateKeyQuickFix(fullKey, AllFilesSelector(), PluginBundle.getMessage("quickfix.create.key.in.files"), generators))
    }
}