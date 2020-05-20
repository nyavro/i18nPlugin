package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.FullKeyExtractor
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

/**
 * Annotator for i18n keys
 */
abstract class CompositeKeyAnnotatorBase(private val keyExtractor: FullKeyExtractor): Annotator, CompositeKeyResolver<PsiElement> {

    /**
     * Tries to parse element as i18n key and annotates it when succeeded
     */
    override fun annotate(element: PsiElement, holder: AnnotationHolder) =
        keyExtractor.extractFullKey(element).nullableToList().forEach {
            annotateI18nLiteral(it, element, holder)
        }

    private fun annotateI18nLiteral(fullKey: FullKey, element: PsiElement, holder: AnnotationHolder) {
        val fileName = fullKey.ns?.text
        val compositeKey = fullKey.compositeKey
        val config = Settings.getInstance(element.project).config()
        val annotationHelper = AnnotationHelper(holder, KeyRangesCalculator(element.textRange, element.text.isQuoted()), element.project)
        val files = LocalizationSourceSearch(element.project).findFilesByName(fileName)
        if (files.isEmpty()) {
            if (fullKey.ns == null) {
                annotationHelper.unresolvedDefaultNs(fullKey)
            } else {
                annotationHelper.unresolvedNs(fullKey, fullKey.ns)
            }
        }
        else {
            val pluralSeparator = config.pluralSeparator
            val mostResolvedReference = files
                .flatMap { resolve(compositeKey, PsiElementTree.create(it.element), pluralSeparator) }
                .maxBy { v -> v.path.size }!!
            when {
                mostResolvedReference.unresolved.isEmpty() && mostResolvedReference.element?.isLeaf() ?: false -> annotationHelper.annotateResolved(fullKey)
                mostResolvedReference.unresolved.isEmpty() -> annotationHelper.annotateReferenceToObject(fullKey)
                mostResolvedReference.isTemplateUnresolved() -> annotationHelper.annotateResolved(fullKey)
                else -> annotationHelper.unresolvedKey(fullKey, mostResolvedReference)
            }
        }
    }
}

