package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.factory.TranslationFolderSelector
import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.FullKeyExtractor
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement

/**
 * Annotator for i18n keys
 */
abstract class CompositeKeyAnnotatorBase(private val keyExtractor: FullKeyExtractor, private val folderSelector: TranslationFolderSelector): Annotator, CompositeKeyResolver<PsiElement> {

    /**
     * Tries to parse element as i18n key and annotates it when succeeded
     */
    override fun annotate(element: PsiElement, holder: AnnotationHolder) =
        keyExtractor.extractFullKey(element).nullableToList().forEach {
            annotateI18nLiteral(it, element, holder)
        }

    private fun annotateI18nLiteral(fullKey: FullKey, element: PsiElement, holder: AnnotationHolder) {
        val annotationHelper = AnnotationHelper(
            holder,
            KeyRangesCalculator(element.textRange.shiftRight(element.text.unQuote().indexOf(fullKey.source)), element.text.isQuoted()),
            element.project,
            folderSelector
        )
        val sourceService = element.project.service<LocalizationSourceService>()
        val files = sourceService.findSources(fullKey.allNamespaces(), element.project)
        if (files.isEmpty()) {
            if (fullKey.ns == null) {
                annotationHelper.unresolvedDefaultNs(fullKey)
            } else {
                annotationHelper.unresolvedNs(fullKey, fullKey.ns)
            }
        }
        else {
            val config = Settings.getInstance(element.project).config()
            val pluralSeparator = config.pluralSeparator
            val references = files.flatMap {resolve(fullKey.compositeKey, it.tree, pluralSeparator, it.type)}
            val allEqual = references.zipWithNext().all { it.first.path == it.second.path }
            val mostResolvedReference = if (allEqual) references.first() else references.maxByOrNull { v -> v.path.size }!!
            if (mostResolvedReference.unresolved.isEmpty()) {
                if (!allEqual && config.partialTranslationInspectionEnabled) {
                    annotationHelper.annotatePartiallyTranslated(fullKey, references)
                } else {
                    if (mostResolvedReference.element?.isLeaf() ?: false) {
                        annotationHelper.annotateResolved(fullKey)
                    } else {
                        annotationHelper.annotateReferenceToObject(fullKey)
                    }
                }
            } else {
                annotationHelper.unresolvedKey(fullKey, mostResolvedReference)
            }
        }
    }
}

