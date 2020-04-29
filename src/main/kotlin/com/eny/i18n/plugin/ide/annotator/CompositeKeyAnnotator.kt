package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.parser.FullKeyExtractor
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.tree.PsiElementTree
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.KeyRangesCalculator
import com.eny.i18n.plugin.utils.LocalizationFileSearch
import com.eny.i18n.plugin.utils.isQuoted
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.psi.PsiElement

/**
 * Annotator for i18n keys
 */
class CompositeKeyAnnotator(private val keyExtractor: FullKeyExtractor):  CompositeKeyResolver<PsiElement> {

    fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val i18nKeyLiteral = keyExtractor.extractI18nKeyLiteral(element)
        if (i18nKeyLiteral != null) {
            annotateI18nLiteral(i18nKeyLiteral, element, holder)
        }
    }

    private fun annotateI18nLiteral(fullKey: FullKey, element: PsiElement, holder: AnnotationHolder) {
        val fileName = fullKey.ns?.text
        val compositeKey = fullKey.compositeKey
        val settings = Settings.getInstance(element.project)
        val annotationHelper = AnnotationHelper(holder, KeyRangesCalculator(element.textRange, element.text.isQuoted()), element.project)
        val files = LocalizationFileSearch(element.project).findFilesByName(fileName)
        if (files.isEmpty()) {
            if (fullKey.ns == null) {
                annotationHelper.unresolvedDefaultNs(fullKey)
            } else {
                annotationHelper.unresolvedNs(fullKey, fullKey.ns)
            }
        }
        else {
            val pluralSeparator = settings.pluralSeparator
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

